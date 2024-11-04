import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Behavior;

public class SoundBehavior implements Behavior {
    private boolean suppressed = false;

    public boolean takeControl() {
        float[] sample = new float[BehaviorRobot.soundSensor.sampleSize()];
        BehaviorRobot.soundSensor.fetchSample(sample, 0);
        return sample[0] > BehaviorRobot.Sound_Threshold;
    }

    public void action() {
        LCD.drawString("Behavior 2: Sound", 0, 1);
        if (BehaviorRobot.Ultrasound_State == 0) {
            motorLeft.rotate(180);
            motorRight.rotate(-180);
        } else {
            motorLeft.stop();
            motorRight.stop();
            System.out.println("Victory Dance!");
            System.exit(0);
        }
    }

    public void suppress() {
        suppressed = true;
    }
}
