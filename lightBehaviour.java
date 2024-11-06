import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.NXTLightSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;
import lejos.hardware.sensor.NXTSoundSensor;
import lejos.hardware.port.SensorPort;

public class LightBehaviour implements Behavior  {
    // Declaring any variables here (i.e. within the class, but outside the methods (functions)
    // means that the variables are accessible by all the methods of the class. 
    // The 'private' qualifier  implies that they are 'global' to the class methods
    // - but not accessible by other class objects.
    final int intLightThreshold = 50; 
    final int WallMax = 50;
    final int WallMin = 20;
    private boolean isActive;
    private NXTLightSensor light;
    private float[] lightValue = new float[1];
    private SampleProvider lightAmbient;
    private IntHolder desiredDistance;
    
    // Sound sensor and threshold for Behavior 3
    private NXTSoundSensor soundSensor;
    private final int Sound_Threshold = 60;  // The sound threshold value
    
    // Ultrasound state and wall distance
    private int Ultrasound_State = 0;   // Default state
    private int Wall_Distance = 0;      // Default distance

    // This is a Constructor method - same name as the class defines it as such.
    public LightBehaviour(NXTLightSensor lightSensor, IntHolder desiredWallDistance, NXTSoundSensor soundSensor) {   
        isActive = false;
        light = lightSensor;
        light.setCurrentMode("Ambient");
        lightAmbient = light.getMode("Ambient");
        desiredDistance = desiredWallDistance;
        this.soundSensor = soundSensor;
    }
    
    // This is the code that is executed if the behaviour is granted control of the robot.
    @Override
    public void action() {
        // 'isActive' is a flag that we use to signify when the behaviour is in control.
        isActive = true; 
        LCD.clear();
        
        // Display behavior number
        LCD.drawString("Behavior: Light", 0, 0);
        
        // If the distance is too small, adjust it
        if (desiredDistance.value < WallMax)
            desiredDistance.value += 5;  
        else
            desiredDistance.value = WallMin;
        
        // Check the sound level and act accordingly for Behavior 3
        int soundLevel = getSoundLevel();
        if (soundLevel > Sound_Threshold) {
            // Display behavior number on the screen
            LCD.drawString("Behavior: Sound", 0, 1);
            
            // Set Ultrasound_State to 1 and Wall_Distance to 20
            Ultrasound_State = 1;
            Wall_Distance = 20;
            
            // Rotate the ultrasound sensor 180 degrees
            rotateUltrasoundSensor();
            
            // We don't need to continuously monitor the sound, just exit after the change
            return;  // exit the action method after handling the sound detection
        }
        
        // Use this loop to keep the behaviour action focus here for extended operations, if required...
        // Otherwise, perform other actions here if needed
        while (isActive) {
            // Optionally, check light value and adjust desired distance here (if needed)
            lightAmbient.fetchSample(lightValue, 0);
            if (lightValue[0] > intLightThreshold) {
                // Perform light-related actions here (e.g., moving robot, changing direction)
            }
        }
    }
    
    // Housekeeping when the behaviour no longer requires control of the robot
    @Override
    public void suppress() {
        isActive = false;
    }
    
    // Trigger condition for behaviour...
    @Override
    public boolean takeControl() {
        // Trigger behavior when light value exceeds threshold
        lightAmbient.fetchSample(lightValue, 0);
        return (lightValue[0] > intLightThreshold);  // Trigger condition for light
    }
    
    // Method to get the sound level from the sound sensor
    private int getSoundLevel() {
        // Get the sound level sample
        float[] sample = new float[soundSensor.sampleSize()];
        soundSensor.fetchSample(sample, 0);
        
        // Return the sound level in decibels (multiplied by 100 for clarity)
        return (int) (sample[0] * 100);
    }
    
    // Method to rotate the ultrasound sensor 180 degrees
    private void rotateUltrasoundSensor() {
        // This method will rotate the ultrasound sensor 180 degrees (you may need to adjust depending on your setup)
        // Assuming the ultrasound sensor is attached to a motor
        // Set motor speed (adjust as needed)
        NXTRegulatedMotor ultrasoundMotor = new NXTRegulatedMotor(MotorPort.D);
        ultrasoundMotor.setSpeed(50);
        
        // Rotate the sensor 180 degrees
        ultrasoundMotor.rotate(180);
    }
    
    // Housekeeping
    @Override
    protected void finalize() {
        // Any necessary cleanup (e.g., closing sensors, releasing resources)
    }  
}
