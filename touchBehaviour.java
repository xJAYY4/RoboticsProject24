import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.subsumption.Behavior;

public class TouchBehavior implements Behavior {
    private final EV3TouchSensor touchSensor;
    private final EV3LargeRegulatedMotor engineR;
    private final EV3LargeRegulatedMotor engineL;
    private boolean isActive = false;
    private float[] touchSample;

    public TouchBehavior(EV3TouchSensor sensor, EV3LargeRegulatedMotor motorR, EV3LargeRegulatedMotor motorL) {
        this.touchSensor = sensor;
        this.engineR = motorR;
        this.engineL = motorL;
        this.touchSample = new float[sensor.sampleSize()];
    }

    @Override
    public boolean takeControl() {
        touchSensor.fetchSample(touchSample, 0);
        return touchSample[0] == 1.0;
    }

    @Override
    public void action() {
        engineR.backward();
        engineL.backward();
    }

    @Override
    public void suppress() {
        isActive = false;
        engineR.stop(true);
        engineL.stop(true);
    }
}
