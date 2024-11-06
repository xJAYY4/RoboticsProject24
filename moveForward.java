import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.sensor.NXTUltrasonicSensor;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay; 

public class MoveForward implements Behavior  
{ 
    private final NXTRegulatedMotor engineR; 
    private final NXTRegulatedMotor engineL; 
    private final NXTUltrasonicSensor uSensor; 
    private float[] distanceSample; 
    
    private int Vc;                  // Wander_Speed (Set baseline velocity)
    private int sampleInterval;      // ms default value 
    private int distance;
    private final int noObject = 450; 
    private int minPower = 200, maxPower = 500;  
    private int leftSpeed = 0, rightSpeed = 0; 
    private IntHolder desiredDistance;

    private float error, Pgain;      // Proportional gain
    private boolean isActive = false; 

    // Constructor
    MoveForward(NXTUltrasonicSensor sensor, NXTRegulatedMotor motorR, NXTRegulatedMotor motorL, IntHolder desiredWallDistance)  
    { 
        Pgain = 1.0f;
        Vc = 350;  // Set Wander_Speed to 350
        sampleInterval = 25;
        desiredDistance = desiredWallDistance; 
        engineR = motorR; 
        engineL = motorL; 
        uSensor = sensor; 

        distanceSample = new float[uSensor.sampleSize()]; 
        leftSpeed = rightSpeed = Vc;  // Set the base speeds for left and right motors  
    } 

    @Override
    public void action()  
    { 
        isActive = true; 
        
        // Display the behavior number or name on the EV3 screen
        LCD.clear();
        LCD.drawString("Behavior: MoveForward", 0, 0);
        
        while (isActive) 
        {
            // Fetch the ultrasonic sample
            uSensor.fetchSample(distanceSample, 0); 
            distance = (int)(distanceSample[0]*100); 
            LCD.drawInt(distance, 0, 5);  

            // Adjust the distance if there’s no object detected within range
            if (Math.abs(distance) > noObject) 
                distance = desiredDistance.value + 40;

            error = (distance - desiredDistance.value); 

            LCD.drawString("                   ", 0, 5);  // Clear LCD
            LCD.drawString("                   ", 0, 6);  

            // Proportional controller to adjust motor speeds
            rightSpeed = (int)(Vc - 0.5 * ( Pgain * error ));
            leftSpeed = (int)(Vc + 0.5 * ( Pgain * error ));

            // Clamp motor speeds within limits
            rightSpeed = Math.min(rightSpeed, maxPower); 
            leftSpeed = Math.min(leftSpeed, maxPower); 
            rightSpeed = Math.max(rightSpeed, minPower); 
            leftSpeed = Math.max(leftSpeed, minPower); 

            engineL.setSpeed(leftSpeed); 
            engineR.setSpeed(rightSpeed); 

            LCD.drawInt(leftSpeed, 0, 5);  // Display speed on LCD
            LCD.drawInt(rightSpeed, 0, 6); 

            // Move the motors forward
            engineR.forward(); 
            engineL.forward();
            
            Delay.msDelay(sampleInterval); 
        }

        engineR.stop(true); 
        engineL.stop(true);
    } 

    @Override
    public void suppress()  
    { 
        isActive = false; 
    } 

    @Override
    public boolean takeControl()  
    { 
        // Always take control as this is the lowest priority behavior
        return true; 
    } 
}
