import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Behavior;

public class TouchBehavior implements Behavior {
    public boolean takeControl() {
        float[] sample = new float[BehaviorRobot.touchSensor.sampleSize()];
        BehaviorRobot.touchSensor.fetchSample(sample, 0);
        return sample[0] == 1;
    }

    public void action() {
        LCD.drawString("Behavior 4: Touch", 0, 1);
        motorLeft.backward();
        motorRight.backward();
        Thread.sleep(3000);
        motorLeft.stop();
        motorRight.stop();
    }

    public void suppress() {}
}
