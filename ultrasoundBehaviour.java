import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Behavior;

public class UltrasoundBehavior implements Behavior {
    private boolean suppressed = false;

    public boolean takeControl() {
        float[] sample = new float[BehaviorRobot.ultrasonicSensor.sampleSize()];
        BehaviorRobot.ultrasonicSensor.fetchSample(sample, 0);
        float distance = sample[0] * 100; // Convert to cm
        return (BehaviorRobot.Ultrasound_State == 0 && distance < 25) ||
               (BehaviorRobot.Ultrasound_State == 1 && distance < 15);
    }

    public void action() {
        suppressed = false;
        LCD.clear();
        LCD.drawString("Behavior 1: Ultrasound", 0, 1);

        float[] sample = new float[BehaviorRobot.ultrasonicSensor.sampleSize()];
        while (!suppressed) {
            BehaviorRobot.ultrasonicSensor.fetchSample(sample, 0);
            float distance = sample[0] * 100; // Convert to cm

            if (distance < BehaviorRobot.Wall_Distance) {
                if (BehaviorRobot.Ultrasound_State == 0) {
                    // Adjust for wall on the right
                    BehaviorRobot.motorLeft.setSpeed(BehaviorRobot.Wander_Speed);
                    BehaviorRobot.motorRight.setSpeed(BehaviorRobot.Wander_Speed / 2);
                } else {
                    // Adjust for wall on the left
                    BehaviorRobot.motorLeft.setSpeed(BehaviorRobot.Wander_Speed / 2);
                    BehaviorRobot.motorRight.setSpeed(BehaviorRobot.Wander_Speed);
                }
                BehaviorRobot.motorLeft.forward();
                BehaviorRobot.motorRight.forward();
            }
        }
        BehaviorRobot.motorLeft.stop(true);
        BehaviorRobot.motorRight.stop();
    }

    public void suppress() {
        suppressed = true;
    }
}
