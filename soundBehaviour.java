import lejos.hardware.lcd.LCD;
import lejos.hardware.Sound;
import lejos.robotics.subsumption.Behavior;

public class SoundBehavior implements Behavior {
    private boolean suppressed = false;

    public boolean takeControl() {
        float[] sample = new float[BehaviorRobot.soundSensor.sampleSize()];
        BehaviorRobot.soundSensor.fetchSample(sample, 0);
        return sample[0] > BehaviorRobot.Sound_Threshold;
    }

    public void action() {
        suppressed = false;
        LCD.clear();
        LCD.drawString("Behavior 2: Sound", 0, 1);

        if (BehaviorRobot.Ultrasound_State == 0) {
            // Rotate 90° counterclockwise
            BehaviorRobot.motorLeft.rotate(-180, true);
            BehaviorRobot.motorRight.rotate(180);
        } else {
            // Play a "victory dance" tune and terminate
            Sound.beepSequenceUp();
            System.exit(0);
        }
    }

    public void suppress() {
        suppressed = true;
    }
}
