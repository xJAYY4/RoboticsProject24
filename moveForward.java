import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.subsumption.Behavior;

public class MoveForward implements Behavior {
    private final EV3LargeRegulatedMotor engineR;
    private final EV3LargeRegulatedMotor engineL;
    private boolean isActive = false;

    public MoveForward(EV3LargeRegulatedMotor motorR, EV3LargeRegulatedMotor motorL) {
        engineR = motorR;
        engineL = motorL;
    }

    @Override
    public boolean takeControl() {
        return true; // Always active
    }

    @Override
    public void action() {
        isActive = true;
        engineR.setSpeed(BehaviorRobot.WANDER_SPEED);
        engineL.setSpeed(BehaviorRobot.WANDER_SPEED);
        engineR.forward();
        engineL.forward();
    }

    @Override
    public void suppress() {
        isActive = false;
        engineR.stop(true);
        engineL.stop(true);
    }
}
