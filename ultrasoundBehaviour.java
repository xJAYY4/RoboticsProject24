import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.subsumption.Behavior;

public class UltrasoundBehavior implements Behavior {
    private final EV3UltrasonicSensor ultrasonicSensor;
    private final EV3LargeRegulatedMotor engineR;
    private final EV3LargeRegulatedMotor engineL;
    private boolean isActive = false;
    private float[] distanceSample;

    public UltrasoundBehavior(EV3UltrasonicSensor sensor, EV3LargeRegulatedMotor motorR, EV3LargeRegulatedMotor motorL) {
        this.ultrasonicSensor = sensor;
        this.engineR = motorR;
        this.engineL = motorL;
        this.distanceSample = new float[sensor.sampleSize()];
    }

    @Override
    public boolean takeControl() {
        ultrasonicSensor.fetchSample(distanceSample, 0);
        return distanceSample[0] < 0.25;
    }

    @Override
    public void action() {
        isActive = true;
        if (BehaviorRobot.Ultrasound_State == 0) {
            // Track wall with sensor facing right
        } else {
            // Track wall with sensor facing left
        }
    }

    @Override
    public void suppress() {
        isActive = false;
    }
}
