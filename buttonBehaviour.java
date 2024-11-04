import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.Sound;
import lejos.robotics.subsumption.Behavior;

public class ButtonBehavior implements Behavior {
    public boolean takeControl() {
        return Button.ENTER.isDown();
    }

    public void action() {
        LCD.clear();
        LCD.drawString("Behavior 5: Button", 0, 1);
        Sound.beepSequence();
        System.exit(0);
    }

    public void suppress() {}
}
