// File: UltrasoundBehavior.java
import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Behavior;

public class UltrasoundBehavior implements Behavior {
    private boolean suppressed = false;

    public boolean takeControl() {
        float[] sample = new float[BehaviorRobot.ultrasonicSensor.sampleSize()];
        BehaviorRobot.ultrasonicSensor.fetchSample(sample, 0);
        float distance = sample[0] * 100; // convert to cm
        return (BehaviorRobot.Ultrasound_State == 0 && distance < 25) ||
               (BehaviorRobot.Ultrasound_State == 1 && distance < 15);
    }

    public void action() {
        LCD.drawString("Behavior 1: Ultrasound", 0, 1);
        float distance;
        do {
            BehaviorRobot.ultrasonicSensor.fetchSample(sample, 0);
            distance = sample[0] * 100; // convert to cm
            if (BehaviorRobot.Ultrasound_State == 0) {
                motorLeft.forward();
                motorRight.backward();
            } else {
                motorLeft.backward();
                motorRight.forward();
            }
        } while (!suppressed && distance < BehaviorRobot.Wall_Distance);
        motorLeft.stop(true);
        motorRight.stop();
    }

    public void suppress() {
        suppressed = true;
    }
}
