import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.sensor.NXTSoundSensor;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class SoundBehavior implements Behavior {
    
    // Declaring necessary variables
    private boolean isActive;
    private NXTSoundSensor soundSensor;
    private int soundThreshold = 60;  // Set a default sound threshold
    private int ultrasoundState;      // Variable to store Ultrasound_State (0 or 1)
    private final NXTRegulatedMotor engineR = new NXTRegulatedMotor(MotorPort.B);
    private final NXTRegulatedMotor engineL = new NXTRegulatedMotor(MotorPort.C);
    
    // Constructor initializing the sound sensor
    public SoundBehavior(NXTSoundSensor soundSensor, int ultrasoundState) {
        this.soundSensor = soundSensor;
        this.ultrasoundState = ultrasoundState;
        this.isActive = false;
    }

    // Action method: to execute behavior when triggered
    public void action() {
        isActive = true;
        LCD.clear();
        
        // Display the behavior number on the EV3 screen
        LCD.drawString("Behavior: Sound", 0, 0);
        
        // Continuously check for sound while active
        while (isActive) {
            // Read the sound level and compare with the threshold
            int soundLevel = getSoundLevel();
            LCD.drawInt(soundLevel, 0, 1);  // Display the current sound level on screen
            
            // Check if sound level exceeds threshold
            if (soundLevel > soundThreshold) {
                LCD.drawString("Sound detected!", 0, 2);

                // If Ultrasound_State = 0, rotate the robot 90° counterclockwise
                if (ultrasoundState == 0) {
                    LCD.drawString("Ultrasound_State = 0", 0, 3);
                    rotateCounterClockwise();
                }
                // If Ultrasound_State = 1, play victory dance and terminate the program
                else if (ultrasoundState == 1) {
                    LCD.drawString("Ultrasound_State = 1", 0, 3);
                    playVictoryDance();
                    terminateProgram();
                    break;  // Exit the loop if we terminate the program
                }
            }

            // Delay for a short period before checking again
            Delay.msDelay(500);  // Delay 500ms before the next check
        }
    }

    // Method to get the sound level from the sensor
    private int getSoundLevel() {
        // Get the sound level sample
        float[] sample = new float[soundSensor.sampleSize()];
        soundSensor.fetchSample(sample, 0);
        
        // Return the sound level in decibels (multiplied by 100 for clarity)
        return (int) (sample[0] * 100);
    }

    // Method to rotate the robot 90° counterclockwise
    private void rotateCounterClockwise() {
        engineR.setSpeed(200);  // Set motor speed
        engineL.setSpeed(200);
        
        // Rotate the robot 90° (assuming motors are set appropriately for rotation)
        engineR.rotate(-180);   // Rotate left motor counterclockwise
        engineL.rotate(180);    // Rotate right motor clockwise
    }

    // Method to play the victory dance and tune, then terminate the program
    private void playVictoryDance() {
        // Play a tune (simple victory melody)
        // You could use the sound sensor or any other function for a melody
        // This is just a simple example using the EV3's tone capabilities
        LCD.clear();
        LCD.drawString("Victory Dance!", 0, 0);
        // Play victory music (replace with actual music code)
        for (int i = 0; i < 3; i++) {
            // Example victory tune (replace with actual music code)
            Sound.playTone(440, 500);  // Play 440Hz tone for 500ms
            Delay.msDelay(200);
        }
    }

    // Method to terminate the program
    private void terminateProgram() {
        LCD.clear();
        LCD.drawString("Terminating Program", 0, 0);
        System.exit(0);  // Exit the program
    }

    // Method to suppress this behavior when not in control
    public void suppress() {
        isActive = false;  // Set isActive to false to stop the behavior loop
    }

    // Trigger condition for behavior
    public boolean takeControl() {
        // Control is taken when sound exceeds the threshold
        int soundLevel = getSoundLevel();
        return soundLevel > soundThreshold;
    }
}
