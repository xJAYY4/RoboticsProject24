// File: ForwardBehavior.java
import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Behavior;

public class ForwardBehavior implements Behavior {
    private boolean suppressed = false;

    public boolean takeControl() {
        return true; // Always active, lowest priority
    }

    public void action() {
        LCD.drawString("Behavior 0: Forward", 0, 1);
        motorLeft.setSpeed(BehaviorRobot.Wander_Speed);
        motorRight.setSpeed(BehaviorRobot.Wander_Speed);
        motorLeft.forward();
        motorRight.forward();
        while (!suppressed) {
            Thread.yield();
        }
        motorLeft.stop();
        motorRight.stop();
    }

    public void suppress() {
        suppressed = true;
    }
}
