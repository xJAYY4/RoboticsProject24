import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.NXTTouchSensor;
import lejos.hardware.sensor.NXTUltrasonicSensor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class BehaviorRobot {
    // Class-level variables for configuration
    private static final int Wander_Speed = 350;
    private static int Ultrasound_State = 0;
    private static final int Wall_Distance = 20;
    private static final int Sound_Threshold = 60;

    private NXTRegulatedMotor engineR = new NXTRegulatedMotor(MotorPort.B);
    private NXTRegulatedMotor engineL = new NXTRegulatedMotor(MotorPort.C);
    private NXTUltrasonicSensor uSensor = new NXTUltrasonicSensor(SensorPort.S3);
    private NXTTouchSensor bumperR = new NXTTouchSensor(SensorPort.S1);
    private Arbitrator arby;
    private Behavior[] behaviorList = new Behavior[5];

    // Constructor to initialize behaviors
    public BehaviorRobot() {
        behaviorList[0] = new MoveForward();
        behaviorList[1] = new UltrasonicCollision();
        behaviorList[2] = new BumperCollision();
        behaviorList[3] = new SoundBehavior();
        behaviorList[4] = new EmergencyStop();
        arby = new Arbitrator(behaviorList);
    }

    public static void main(String[] args) {
        BehaviorRobot robot = new BehaviorRobot();
        LCD.drawString("Arbitrating", 0, 0);
        robot.arby.go();
    }

    // Behavior 0: Move Forward
    private class MoveForward implements Behavior {
        private boolean isActive = false;

        public void action() {
            LCD.drawString("Behavior 0", 0, 1);
            isActive = true;
            engineL.setSpeed(Wander_Speed);
            engineR.setSpeed(Wander_Speed);
            engineR.forward();
            engineL.forward();
            while (isActive) {}
            engineR.stop(true);
            engineL.stop(true);
        }

        public void suppress() {
            isActive = false;
        }

        public boolean takeControl() {
            return true;
        }
    }

    // Behavior 1: Ultrasound Collision Avoidance
    private class UltrasonicCollision implements Behavior {
        private float[] distanceSample = new float[uSensor.sampleSize()];
        private boolean isActive = false;

        public void action() {
            LCD.drawString("Behavior 1", 0, 1);
            isActive = true;
            engineR.setSpeed(Wander_Speed * 0.5F);
            engineL.setSpeed(Wander_Speed * 0.5F);
            if (Ultrasound_State == 0) {
                engineR.forward();
                engineL.backward();
            } else {
                engineL.forward();
                engineR.backward();
            }
            while (isActive && (distanceSample[0] < 1.0)) {
                uSensor.fetchSample(distanceSample, 0);
            }
            engineL.stop(true);
            engineR.stop(true);
        }

        public void suppress() {
            isActive = false;
        }

        public boolean takeControl() {
            uSensor.fetchSample(distanceSample, 0);
            return (distanceSample[0] < 0.30);
        }
    }

    // Behavior 2: Bumper Collision
    private class BumperCollision implements Behavior {
        private float[] bumperSample = new float[bumperR.sampleSize()];
        private boolean isActive = false;

        public void action() {
            LCD.drawString("Behavior 2", 0, 1);
            isActive = true;
            engineR.backward();
            engineL.backward();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            engineR.stop(true);
            engineL.stop(true);
        }

        public void suppress() {
            isActive = false;
        }

        public boolean takeControl() {
            bumperR.fetchSample(bumperSample, 0);
            return bumperSample[0] > 0;
        }
    }

    // Behavior 3: Sound-Based Behavior
    private class SoundBehavior implements Behavior {
        private boolean isActive = false;

        public void action() {
            LCD.drawString("Behavior 3", 0, 1);
            isActive = true;
            if (Ultrasound_State == 0) {
                engineR.rotate(90);
            } else {
                LCD.drawString("Victory Dance!", 0, 2);
                engineL.rotate(360);
                engineR.rotate(-360);
                System.exit(0);
            }
            engineL.stop(true);
            engineR.stop(true);
        }

        public void suppress() {
            isActive = false;
        }

        public boolean takeControl() {
            return (Math.random() * 100) > Sound_Threshold;
        }
    }

    // Behavior 4: Emergency Stop
    private class EmergencyStop implements Behavior {
        public void action() {
            LCD.drawString("Emergency Stop", 0, 1);
            engineR.stop(true);
            engineL.stop(true);
            int buttonID;
            LCD.clear();
            LCD.drawString("Press RIGHT to Continue", 0, 2);
            LCD.drawString("Press ESCAPE to Exit", 0, 3);
            do {
                buttonID = Button.waitForAnyPress();
            } while (buttonID != Button.ID_RIGHT && buttonID != Button.ID_ESCAPE);
            if (buttonID == Button.ID_ESCAPE) {
                System.exit(1);
            } else {
                LCD.clear();
                LCD.drawString("Resuming...", 0, 1);
            }
        }

        public void suppress() {}

        public boolean takeControl() {
            return Button.ENTER.isDown();
        }
    }
}
