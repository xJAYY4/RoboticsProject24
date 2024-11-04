import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.EV3SoundSensor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class BehaviorRobot {
    // Project-specific configuration variables
    public static final int Wander_Speed = 350;
    public static int Ultrasound_State = 0;
    public static final int Wall_Distance = 20;
    public static final int Sound_Threshold = 60;

    // Motors and sensors
    public static EV3LargeRegulatedMotor motorLeft = new EV3LargeRegulatedMotor(MotorPort.B);
    public static EV3LargeRegulatedMotor motorRight = new EV3LargeRegulatedMotor(MotorPort.C);
    public static EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S3);
    public static EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S1);
    public static EV3SoundSensor soundSensor = new EV3SoundSensor(SensorPort.S2);

    public static void main(String[] args) {
        // Define and prioritize behaviors
        Behavior[] behaviors = {
            new ForwardBehavior(),
            new UltrasoundBehavior(),
            new SoundBehavior(),
            new LightBehavior(),
            new TouchBehavior(),
            new ButtonBehavior()
        };
        Arbitrator arbitrator = new Arbitrator(behaviors);

        // Display initial message
        LCD.drawString("Starting Arbitrator", 0, 0);
        arbitrator.go();
    }
}
