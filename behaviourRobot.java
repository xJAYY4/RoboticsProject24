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
    public static final int Wander_Speed = 350;
    public static int Ultrasound_State = 0;
    public static final int Wall_Distance = 20;
    public static final int Sound_Threshold = 60;

    EV3LargeRegulatedMotor motorLeft = new EV3LargeRegulatedMotor(MotorPort.B);
    EV3LargeRegulatedMotor motorRight = new EV3LargeRegulatedMotor(MotorPort.C);
    EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S3);
    EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S1);
    EV3SoundSensor soundSensor = new EV3SoundSensor(SensorPort.S4);

    public static void main(String[] args) {
        Behavior[] behaviors = {
            new ForwardBehavior(),
            new UltrasoundBehavior(),
            new SoundBehavior(),
            new LightBehavior(),
            new TouchBehavior(),
            new ButtonBehavior()
        };
        Arbitrator arbitrator = new Arbitrator(behaviors);
        LCD.drawString("Starting Arbitrator", 0, 0);
        arbitrator.go();
    }
}
