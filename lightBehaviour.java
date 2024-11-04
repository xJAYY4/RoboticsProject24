import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Behavior;

public class LightBehavior implements Behavior {
    public boolean takeControl() {
        float[] sample = new float[BehaviorRobot.soundSensor.sampleSize()];
        BehaviorRobot.soundSensor.fetchSample(sample, 0);
        return sample[0] > BehaviorRobot.Sound_Threshold;
    }

    public void action() {
        LCD.clear();
        LCD.drawString("Behavior 3: Light", 0, 1);
        BehaviorRobot.Ultrasound_State = 1;
        BehaviorRobot.Wall_Distance = 20;
        // Simulate rotating the sensor 180 degrees
        BehaviorRobot.ultrasonicSensor.close();
    }

    public void suppress() {}
}
