import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.NXTLightSensor;
import lejos.hardware.sensor.NXTTouchSensor;
import lejos.hardware.sensor.NXTUltrasonicSensor;
import lejos.hardware.sound.Sound;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class BehaviorRobot
{   
    // Define the sensor objects needed by the robot here.
    NXTRegulatedMotor engineR = new NXTRegulatedMotor(MotorPort.B); 
    NXTRegulatedMotor engineL = new NXTRegulatedMotor(MotorPort.C); 
    NXTTouchSensor bumperR = new NXTTouchSensor(SensorPort.S2); 
    NXTLightSensor light = new NXTLightSensor(SensorPort.S1);
    NXTUltrasonicSensor uSensor = new NXTUltrasonicSensor(SensorPort.S3); 
    GyroSensor gyro = new GyroSensor(SensorPort.S4);

    // Initialization of required class-level variables
    public final static int Wander_Speed = 350;        // Set Wander_Speed to 350 degrees per second
    public static int Ultrasound_State = 0;             // Set Ultrasound_State to 0
    public static int Wall_Distance = 20;               // Set Wall_Distance to 20 cm
    public static int Sound_Threshold = 60;             // Set Sound_Threshold to 60 decibels

    Arbitrator arby;
    
    // Ensure the array is large enough to accommodate all behaviors
    Behavior[] behaviorList = new Behavior[6]; 
    IntHolder desiredDistance = new IntHolder(30);  // cm, default value  
    public final static int SPEED = 360; 

    BehaviorRobot() throws Exception 
    { 
        // Initializing ultrasonic sensor in distance mode
        uSensor.setCurrentMode("Distance"); 
        
        // Setting default values
        desiredDistance.value = 30;

        // Create behavior objects and assign them to behaviorList
        behaviorList[0] = new MoveForward(engineR, engineL);  // Behavior 0: Forward
        behaviorList[1] = new UltrasoundTrack(uSensor, engineR, engineL);  // Behavior 1: Ultrasound
        behaviorList[2] = new SoundBehavior(engineR, engineL);  // Behavior 2: Sound
        behaviorList[3] = new LightBehavior(light, desiredDistance);  // Behavior 3: Light
        behaviorList[4] = new TouchBehavior(bumperR, engineR, engineL);  // Behavior 4: Touch
        behaviorList[5] = new ButtonBehavior();  // Behavior 5: Button

        // Initialize Arbitrator with behaviors
        arby = new Arbitrator(behaviorList); 
    } 

    public static void main(String[] args) throws Exception  
    {     
        // Create the BehaviorRobot object and start the arbitrator
        BehaviorRobot SampleBehaviors = new BehaviorRobot(); 
        LCD.drawString("Arbitrating", 0, 0); 
        SampleBehaviors.arby.go();  // Start the arbitrator loop
    } 
}
