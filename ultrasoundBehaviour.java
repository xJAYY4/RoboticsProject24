import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.sensor.NXTUltrasonicSensor;
import lejos.robotics.subsumption.Behavior;

/**
 * This class implements the robot's behavior after an
 * obstacle is detected by the ultrasonic sensor.
 * The robot turns randomly to the left or to the right
 * until there’s at least 1m of free path in front of it.
 * 
 * The robot will also track the wall at the specified
 * `Wall_Distance` based on the value of `Ultrasound_State`.
 */
public class UltrasonicCollision implements Behavior {
    
    protected final NXTRegulatedMotor engineR;
    protected final NXTRegulatedMotor engineL;
    private final NXTUltrasonicSensor uSensor;
    private float[] distanceSample;
    private boolean isActive = false;
    
    // Additional parameters
    private int Wall_Distance = 20;   // Desired distance from the wall (in cm)
    private int Ultrasound_State = 0;  // 0 for tracking right, 1 for tracking left
    
    // Constructor method
    UltrasonicCollision(NXTUltrasonicSensor sensor, NXTRegulatedMotor motorR, NXTRegulatedMotor motorL) {
        engineR = motorR;
        engineL = motorL;
        uSensor = sensor;
        uSensor.setCurrentMode("Distance");
        distanceSample = new float[uSensor.sampleSize()];
    }
    
    @Override
    public void action() {
        isActive = true;
        
        // Randomly turn left or right at half speed if obstacle detected
        double rand = Math.random();
        engineR.setSpeed(BehaviorRobot.SPEED * 0.5F);
        engineL.setSpeed(BehaviorRobot.SPEED * 0.5F);
        
        if (rand < 0.5) {
            engineR.forward();
            engineL.backward();
        } else {
            engineL.forward();
            engineR.backward();
        }
        
        // Keep turning until a 1m free path is detected
        while (isActive && distanceSample[0] < 1.0) {
            uSensor.fetchSample(distanceSample, 0);
        }
        
        // After obstacle is avoided, stop the motors
        engineL.stop(true);
        engineR.stop(true);
        
        // Now track the wall based on the Ultrasound_State
        if (Ultrasound_State == 0) {
            // Track the wall on the right with the sensor
            trackWall(true);  // Pass true for right-side tracking
        } else if (Ultrasound_State == 1) {
            // Track the wall on the left with the sensor
            trackWall(false);  // Pass false for left-side tracking
        }
    }
    
    // Helper method to track the wall at a specified distance
    private void trackWall(boolean trackRight) {
        // Set the robot's orientation to track the wall with the sensor on the appropriate side
        // For right, the sensor points to the right, for left it points to the left
        
        int turnDirection = trackRight ? 1 : -1;  // 1 for right, -1 for left
        while (isActive) {
            uSensor.fetchSample(distanceSample, 0);
            float currentDistance = distanceSample[0] * 100; // convert to cm
            
            // If the robot is too far from the wall, turn to correct the position
            if (currentDistance < Wall_Distance) {
                // Wall is too close, turn slightly to increase distance
                engineR.setSpeed(BehaviorRobot.SPEED);
                engineL.setSpeed(BehaviorRobot.SPEED);
                engineR.forward();
                engineL.backward();
            } else if (currentDistance > Wall_Distance) {
                // Wall is too far, turn slightly to reduce distance
                engineR.setSpeed(BehaviorRobot.SPEED);
                engineL.setSpeed(BehaviorRobot.SPEED);
                engineR.backward();
                engineL.forward();
            } else {
                // Wall is at the correct distance, move forward
                engineR.setSpeed(BehaviorRobot.SPEED);
                engineL.setSpeed(BehaviorRobot.SPEED);
                engineR.forward();
                engineL.forward();
            }
        }
    }
    
    @Override
    public void suppress() {
        isActive = false;
    }
    
    @Override
    public boolean takeControl() {
        // Trigger condition: only take control if the robot is very close to an obstacle
        uSensor.fetchSample(distanceSample, 0);
        return distanceSample[0] < 0.30;
    }
}
