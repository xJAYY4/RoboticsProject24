import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Behavior;

public class LightBehavior implements Behavior {
    public boolean takeControl() {
        float[] sample = new float[BehaviorRobot.soundSensor.sampleSize()];
        BehaviorRobot.soundSensor.fetchSample(sample, 0);
        return sample[0] > BehaviorRobot.Sound_Threshold;
    }

    public void action() {
        LCD.drawString("Behavior 3: Light", 0, 1);
        BehaviorRobot.Ultrasound_State = 1;
        BehaviorRobot.Wall_Distance = 20;
        BehaviorRobot.ultrasonicSensor.rotate(180);
    }

    public void suppress() {}
}
