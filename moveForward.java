import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Behavior;

public class ForwardBehavior implements Behavior {
    private boolean suppressed = false;

    public boolean takeControl() {
        return true; // Always active
    }

    public void action() {
        suppressed = false;
        LCD.clear();
        LCD.drawString("Behavior 0: Forward", 0, 1);
        BehaviorRobot.motorLeft.setSpeed(BehaviorRobot.Wander_Speed);
        BehaviorRobot.motorRight.setSpeed(BehaviorRobot.Wander_Speed);
        BehaviorRobot.motorLeft.forward();
        BehaviorRobot.motorRight.forward();

        while (!suppressed) {
            Thread.yield();
        }
        BehaviorRobot.motorLeft.stop(true);
        BehaviorRobot.motorRight.stop();
    }

    public void suppress() {
        suppressed = true;
    }
}
