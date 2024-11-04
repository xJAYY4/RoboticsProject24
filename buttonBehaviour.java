import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Behavior;

public class ButtonBehavior implements Behavior {
    public boolean takeControl() {
        return Button.ENTER.isDown();
    }

    public void action() {
        LCD.drawString("Behavior 5: Button", 0, 1);
        System.out.println("Exiting...");
        System.exit(0);
    }

    public void suppress() {}
}
