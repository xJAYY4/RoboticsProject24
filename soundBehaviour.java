import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.sensor.EV3SoundSensor;
import lejos.robotics.subsumption.Behavior;

public class SoundBehavior implements Behavior {
    private final EV3LargeRegulatedMotor engineR;
    private final EV3LargeRegulatedMotor engineL;
    private final EV3SoundSensor soundSensor;
    private boolean isActive = false;
    private float[] soundSample;

    public SoundBehavior(EV3LargeRegulatedMotor motorR, EV3LargeRegulatedMotor motorL, EV3SoundSensor sensor) {
        this.engineR = motorR;
        this.engineL = motorL;
        this.soundSensor = sensor;
        this.soundSample = new float[sensor.sampleSize()];
    }

    @Override
    public boolean takeControl() {
        soundSensor.fetchSample(soundSample, 0);
        return soundSample[0] > BehaviorRobot.SOUND_THRESHOLD;
    }

    @Override
    public void action() {
        if (BehaviorRobot.Ultrasound_State == 0) {
            engineR.rotate(90); // Rotate 90 degrees counterclockwise
        } else {
            engineL.rotate(90);
            // Play victory tune, terminate
            System.exit(0);
        }
    }

    @Override
    public void suppress() {
        isActive = false;
    }
}
