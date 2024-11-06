import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.sensor.NXTTouchSensor;
import lejos.hardware.Sound;
import lejos.robotics.subsumption.Behavior;
import lejos.hardware.lcd.LCD;

/**
 * Behavior that handles the robot after a bumper collision.
 * It reverses to a distance of 20 cm, then beeps sorrowfully for 3 seconds.
 */
public class BumperCollision implements Behavior {
    // Declare the motors and touch sensor
    protected final NXTRegulatedMotor engineR;
    protected final NXTRegulatedMotor engineL;
    private final NXTTouchSensor tSensorR;

    // Create variables for touch sensor input and control
    private float[] rBumperSample;
    private boolean bRightBumper = false;
    private boolean isActive = false;

    // Constants for controlling the behavior
    private final float SPEEDFACTOR = 0.5F;
    private final int LIMITANGLE = 180;  // Degrees to move backward (180°)

    private final float DISTANCE_TARGET = 20.0F;  // Target reverse distance in cm
    private final float P_GAIN = 0.5F;  // Proportional gain for controller

    private final int REVERSE_SPEED = 200;  // Speed for reversing

    // Constructor for BumperCollision behavior
    BumperCollision(NXTTouchSensor touchR, NXTRegulatedMotor motorR, NXTRegulatedMotor motorL) {
        engineR = motorR;
        engineL = motorL;
        tSensorR = touchR;
        tSensorR.setCurrentMode("Touch");
        rBumperSample = new float[tSensorR.sampleSize()];
    }

    // Reset the bumper condition
    private void resetBoolean() {
        bRightBumper = false;
    }

    // Method for the P controller to reverse the robot to the target distance
    private void reverseWithPController(float targetDistance) {
        engineR.resetTachoCount();
        engineL.resetTachoCount();

        // Reverse the robot while using the P controller to approach the target distance
        while (engineR.getTachoCount() < targetDistance * 10) {  // Assuming each 1 degree of tacho count is ~ 0.1 cm
            float error = targetDistance - (engineR.getTachoCount() / 10); // Distance error
            float speedAdjustment = error * P_GAIN;

            int adjustedSpeed = REVERSE_SPEED + (int) speedAdjustment;

            // Ensure speed is non-negative
            if (adjustedSpeed < 0) adjustedSpeed = 0;

            engineR.setSpeed(adjustedSpeed);
            engineL.setSpeed(adjustedSpeed);

            engineR.backward();
            engineL.backward();
        }

        // Stop the motors once the desired distance is reached
        engineR.stop();
        engineL.stop();
    }

    // Action method for when this behavior is active
    @Override
    public void action() {
        isActive = true;
        
        // Display behavior number on EV3 screen
        LCD.clear();
        LCD.drawString("Behavior 4", 0, 0);
        
        // Use the P controller to reverse to 20 cm
        reverseWithPController(DISTANCE_TARGET);

        // Beep sorrowfully for 3 seconds
        Sound.playTone(440, 500); // Sorrowful beep (A4 tone)
        Delay.msDelay(3000); // Wait for 3 seconds
        
        // Resume operation
    }

    // Housekeeping when the behavior no longer requires control
    @Override
    public void suppress() {
        isActive = false;
    }

    // Trigger condition for this behavior (when the touch sensor is pressed)
    @Override
    public boolean takeControl() {
        resetBoolean();
        tSensorR.fetchSample(rBumperSample, 0);
        bRightBumper = (rBumperSample[0] == 1.0);

        // Control is granted if the touch sensor is pressed
        return bRightBumper;
    }
}
