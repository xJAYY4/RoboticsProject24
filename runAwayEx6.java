import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.robotics.subsumption.Behavior;

/**
 * This class implements the robot's behavior to "run away" when a loud sound is detected.
 * The robot turns around and moves away for 5 seconds while flashing the button lights.
 */
public class RunAwaySound implements Behavior {
    private final NXTRegulatedMotor engineR;
    private final NXTRegulatedMotor engineL;
    private boolean isActive = false;

    RunAwaySound(NXTRegulatedMotor motorR, NXTRegulatedMotor motorL) {
        engineR = motorR;
        engineL = motorL;
    }

    public void action() {
        isActive = true;

        // Set speed for escape movement
        engineL.setSpeed(BehaviorRobot.SPEED * 0.7F);
        engineR.setSpeed(BehaviorRobot.SPEED * 0.7F);

        // Turn around and move backward
        engineR.backward();
        engineL.backward();

        // Flash button lights and continue running for 5 seconds
        long endTime = System.currentTimeMillis() + 5000;
        while (System.currentTimeMillis() < endTime && isActive) {
            Button.LEDPattern(6); // Flashing LED pattern
            try {
                Thread.sleep(200);  // Flashing interval
                Button.LEDPattern(0);
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Stop movement
        engineL.stop(true);
        engineR.stop(true);
        Button.LEDPattern(0);  // Turn off LEDs
    }

    public void suppress() {
        isActive = false;
    }

    public boolean takeControl() {
        // Take control if a loud sound is detected
        return Sound.readVolume() > 70;  // Threshold for 'loud' sound
    }
}
