import lejos.hardware.lcd.LCD;
import lejos.hardware.Sound;
import lejos.robotics.subsumption.Behavior;

public class TouchBehavior implements Behavior {
    public boolean takeControl() {
        float[] sample = new float[BehaviorRobot.touchSensor.sampleSize()];
        BehaviorRobot.touchSensor.fetchSample(sample, 0);
        return sample[0] == 1;
    }

    public void action() {
        LCD.clear();
        LCD.drawString("Behavior 4: Touch", 0, 1);

        // Reverse with P control
        BehaviorRobot.motorLeft.backward();
        BehaviorRobot.motorRight.backward();
        Sound.beepSequence();
        Thread.sleep(3000); // Pause for 3 seconds
    }

    public void suppress() {}
}
