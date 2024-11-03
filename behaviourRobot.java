import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.EV3SoundSensor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.hardware.Button;

public class BehaviorRobot {
    // Constants for EV3-based requirements
    public static final int WANDER_SPEED = 350;      // Speed for wandering
    public static int Ultrasound_State = 0;          // 0 for right, 1 for left
    public static final int WALL_DISTANCE = 20;      // Wall tracking distance in cm
    public static final int SOUND_THRESHOLD = 60;    // Sound threshold for SoundBehavior

    // Motor and sensor definitions
    EV3LargeRegulatedMotor engineR = new EV3LargeRegulatedMotor(MotorPort.B);
    EV3LargeRegulatedMotor engineL = new EV3LargeRegulatedMotor(MotorPort.C);
    EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S3);
    EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S1);
    EV3SoundSensor soundSensor = new EV3SoundSensor(SensorPort.S2);

    // Array of behaviors
    Behavior[] behaviorList;
    Arbitrator arby;

    public BehaviorRobot() {
        // Initialize behaviors in priority order
        behaviorList = new Behavior[]{
            new MoveForward(engineR, engineL),
            new UltrasoundBehavior(ultrasonicSensor, engineR, engineL),
            new SoundBehavior(engineR, engineL, soundSensor),
            new LightBehavior(ultrasonicSensor),
            new TouchBehavior(touchSensor, engineR, engineL),
            new ButtonBehavior()
        };

        // Create arbitrator
        arby = new Arbitrator(behaviorList);
    }

    public static void main(String[] args) {
        BehaviorRobot robot = new BehaviorRobot();
        LCD.drawString("Starting Arbitrator", 0, 0);
        robot.arby.go();
    }
}
