import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.robotics.subsumption.Behavior;

/*********************************************************
* This class implements the robot's emergency stop function.
* When ENTER is pressed, the robot stops its movement
* immediately and waits for further button-input.
* RIGHT -> The robot continues its movement; 
* ESCAPE -> The robot's control program exits.
**********************************************************/

public class EmergencyStop implements Behavior 
{
    // Declaring any variables here (i.e. within the class, but outside the methods (functions)
    // means that the variables are accessible by all the methods of the class. 
    // The 'private' qualifier  implies that they are 'global' to the class methods
    // - but not accessible by other class objects.

    protected final NXTRegulatedMotor engineR;
    protected final NXTRegulatedMotor engineL;
    
    // This is a Constructor method - same name as the class defines it as such.
    // This is where the initialisations occur
    EmergencyStop(NXTRegulatedMotor motorR, NXTRegulatedMotor motorL)
    {
        engineR = motorR;
        engineL = motorL;
    }
    
    // This is the code that is executed if the behaviour is granted control of the robot.
    public void action() 
    {
        engineR.stop(true);
        engineL.stop(true);
        
        int buttonID;
        
        // Display message on the screen to indicate that the arbitration has been halted
        LCD.clear();
        LCD.drawString("Behavior 5", 0, 0);  // Display behavior number
        LCD.drawString("Arbitration halted", 0, 1);
        LCD.drawString("RIGHT to Continue", 0, 2);
        LCD.drawString("ESCAPE to Exit", 0, 3);
        
        Button.LEDPattern(0); // Turns the LED to indicate "halted"
        
        // Wait until the user presses a button
        do 
        {
            buttonID = Button.waitForAnyPress();
        } while (buttonID != Button.ID_RIGHT && buttonID != Button.ID_ESCAPE);
        
        // Exit the program if the escape button is pressed.
        if (buttonID == Button.ID_ESCAPE) 
        {   
            // Display exit message
            LCD.clear();
            LCD.drawString("Exiting Program", 0, 0);
            LCD.drawString("Goodbye!", 0, 1);
            // Play a "sorrowful" exit tune
            Sound.playTone(440, 500);  // Example of a sorrowful tone
            Sound.playTone(220, 500);
            System.exit(1);
        }
        else 
        {
            LCD.clear();
            LCD.drawString("Resuming...", 0, 0);
            // Resume the robot's actions after "RIGHT" is pressed
        }
    }
    
    // Housekeeping when the behaviour no longer requires control of the robot
    public void suppress() {}
    
    // The event being monitored to determine if this behaviour is to take control 
    public boolean takeControl() 
    {
        // The behavior will take control when the ENTER button is pressed
        return Button.ENTER.isDown();
    }
}
