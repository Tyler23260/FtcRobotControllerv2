package org.firstinspires.ftc.teamcode.WireFireFTC.TeleOP;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.WireFireFTC.Robot.WireFireRobotClass;

@TeleOp(name = "WireFire-TeleOp",group = "Linear OpMode")
public class WireFireTeleOp extends LinearOpMode {
    //Used for Telemetry
    private ElapsedTime runtime = new ElapsedTime();

    //Imports the Robot Class
    WireFireRobotClass robot = new WireFireRobotClass(this);

    //Power Multipliers
    final double SAFE_DRIVE_SPEED = 0.95;
    final double SAFE_STRAFE_SPEED = 0.83;
    final double SAFE_TURN_SPEED = 0.83;

    //Create Variables for rotating slides
    int rotation = 0;
    final int ROTATION_INCREMENT = 7;
    final int MAX_ROTATION = 1400;
    final int MIN_ROTATION = 0;

    //Create Variables for slides
    int height = 0;
    final double HEIGHT_INCREMENT = 25;
    final int MAX_HEIGHT = 4000;
    final int ADJUSTED_MAX_HEIGHT = 5000;
    final int MIN_HEIGHT = 0;

    //Create Variables for Servo
    double intakeHandRotation = 0.0;
    double ServoHandIncrement = 0.025;
    final double MAX_INTAKEHAND_ROTATION = 0.20;
    final double MIN_INTAKEHAND_ROTATION = 0;

    double intakeWristRotation = 0.0;
    double ServoWristIncrement = 0.005;
    final double MAX_INTAKEWRIST_ROTATION = 0.9;
    final double MIN_INTAKEWRIST_ROTATION = 0.2;

    //Create the objects for motors
    private DcMotor frontleft = null;
    private DcMotor frontright = null;
    private DcMotor backleft = null;
    private DcMotor backright = null;

    private DcMotorEx slidesrotation = null;
    private DcMotor slide_motor = null;

    //Create the objects for servos
    private Servo wristRotation = null;
    private Servo intakeHand = null;

    //Create the objects for sensors
    //private ColorSensor colorSensor = null;

    @Override
    public void runOpMode() {
        robot.initialize(true);
        // Initialize the Motors
        frontleft = hardwareMap.get(DcMotor.class, "frontleft");
        frontright = hardwareMap.get(DcMotor.class, "frontright");
        backleft = hardwareMap.get(DcMotor.class, "backleft");
        backright = hardwareMap.get(DcMotor.class, "backright");

        slidesrotation = hardwareMap.get(DcMotorEx.class, "rotation_motor");
        slide_motor = hardwareMap.get(DcMotor.class, "slide_motor");

        //Initialize the Servos
        wristRotation = hardwareMap.get(Servo.class, "wristServo");
        intakeHand = hardwareMap.get(Servo.class, "intakeServo");

        //Initialize the Sensors
        //colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");

        // Set motor directions if needed
        frontleft.setDirection(DcMotor.Direction.REVERSE);
        backleft.setDirection(DcMotor.Direction.REVERSE);
        frontright.setDirection(DcMotor.Direction.FORWARD);
        backright.setDirection(DcMotor.Direction.FORWARD);

        //Change RunMode
        slidesrotation.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        slide_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide_motor.setDirection(DcMotor.Direction.REVERSE);

        wristRotation.setDirection(Servo.Direction.REVERSE);
        intakeHand.setDirection(Servo.Direction.REVERSE);

        while(opModeInInit()) {
            telemetry.addData(">", "Touch Play to Drive");
        }

        // Wait for the game to start (driver presses START)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        //Setting all target positions to zero so it doesn't jitter
        waitForStart();
        slide_motor.setTargetPosition(0);
        slidesrotation.setTargetPosition(0);
        robot.initialize(true);
        runtime.reset();

        // Main control loop
        while (opModeIsActive()) {
            //Start coding here >>>>>>>>>>>>
            /*
            //ColorSensor
            int red = colorSensor.red();
            int green = colorSensor.green();
            int blue = colorSensor.blue();

            // Send color values to telemetry for debugging
            telemetry.addData("Red", red);
            telemetry.addData("Green", green);
            telemetry.addData("Blue", blue);
            */
            //Setting modes and ZeroPower
            slide_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            slide_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            slidesrotation.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slidesrotation.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            //Used for Slides_Motor see slide code for details
            if(gamepad2.left_stick_y > 0.03) {
                rotation -= ROTATION_INCREMENT;
                rotation = Math.max(MIN_ROTATION, Math.min(MAX_ROTATION, rotation));
                setSlidesrotation(rotation, 1.0);
            }
            else if (gamepad2.left_stick_y < -0.03) {
                rotation += ROTATION_INCREMENT;
                rotation = Math.max(MIN_ROTATION, Math.min(MAX_ROTATION, rotation));
                setSlidesrotation(rotation, 1.0);
            }

            //Code for Slides using values to determine how long for the motors to be set until it reaches Target Position
            if(rotation < 1150) {
                if (gamepad2.y) {
                    height += HEIGHT_INCREMENT;
                    height = Math.max(MIN_HEIGHT, Math.min(MAX_HEIGHT, height));
                    setSlides(height, 1.0);
                }
                else if (gamepad2.a) {
                    height -= HEIGHT_INCREMENT;
                    height = Math.max(MIN_HEIGHT, Math.min(MAX_HEIGHT, height));
                    setSlides(height, 1.0);
                }
            }
            else if(rotation > 1150){
                if (gamepad2.y) {
                    height += HEIGHT_INCREMENT;
                    height = Math.max(MIN_HEIGHT, Math.min(ADJUSTED_MAX_HEIGHT, height));
                    setSlides(height, 1.0);
                }
                else if (gamepad2.a) {
                    height -= HEIGHT_INCREMENT;
                    height = Math.max(MIN_HEIGHT, Math.min(ADJUSTED_MAX_HEIGHT, height));
                    setSlides(height, 1.0);
                }
            }


            //IntakeHand Servo
            if(gamepad2.left_trigger > 0.0) {
                intakeHandRotation -= ServoHandIncrement;
                intakeHandRotation = Math.max(MIN_INTAKEHAND_ROTATION, Math.min(MAX_INTAKEHAND_ROTATION, intakeHandRotation));
                setIntakeHand(intakeHandRotation);
            }
            else if(gamepad2.right_trigger > 0.0) {
                intakeHandRotation += ServoHandIncrement;
                intakeHandRotation = Math.max(MIN_INTAKEHAND_ROTATION, Math.min(MAX_INTAKEHAND_ROTATION, intakeHandRotation));
                setIntakeHand(intakeHandRotation);
            }

            //Wrist Servo
            if(gamepad2.left_bumper) {
                intakeWristRotation -= ServoWristIncrement;
                intakeWristRotation = Math.max(MIN_INTAKEWRIST_ROTATION, Math.min(MAX_INTAKEWRIST_ROTATION, intakeWristRotation));
                setWristRotation(intakeWristRotation);
            }
            else if(gamepad2.right_bumper) {
                intakeWristRotation += ServoWristIncrement;
                intakeWristRotation = Math.max(MIN_INTAKEWRIST_ROTATION, Math.min(MAX_INTAKEWRIST_ROTATION, intakeWristRotation));
                setWristRotation(intakeWristRotation);
            }

            //Preset
            if(gamepad2.dpad_up){ //for the high basket
                rotation = 1300;
                setSlidesrotation(rotation, 0.3);
                sleep(1000);
                height = 5000;
                setSlides(height, 0.7);
                intakeWristRotation = 0.38;
                setWristRotation(intakeWristRotation);
            }
            else if(gamepad2.dpad_right){ //for the human player
                height = 1640;
                setSlides(height, 0.7);
                intakeWristRotation = 0.52;
                setWristRotation(intakeWristRotation);
                rotation = 393;
                setSlidesrotation(rotation, 0.3);

            }
            else if(gamepad2.dpad_left) { //for the specimens on the bar
                height = 3260;
                setSlides(height, 0.7);
                intakeWristRotation = 0.84;
                setWristRotation(intakeWristRotation);
                sleep(250);
                rotation = 800;
                setSlidesrotation(rotation, 0.3);
            }
            else if (gamepad2.dpad_down) { //retract
                height = 0;
                setSlides(height, 1.0);
                sleep(1000);
                rotation = 0;
                setSlidesrotation(rotation, 0.4);
                intakeWristRotation = 0.2;
                setWristRotation(intakeWristRotation);
            }
            else if (gamepad2.x){ //To reset auto Left
                height = 950;
                setSlides(height, 1.0);
                intakeWristRotation = 0.9;
                setWristRotation(intakeWristRotation);
                rotation = 1300;
                setSlidesrotation(rotation, 0.78);
                sleep(250);
                height = 1900;
                setSlides(height, 1.0);
            }
            else if(gamepad2.b){ //To reset auto Right

            }

            // Get gamepad inputs
            double drive = -gamepad1.left_stick_y * SAFE_DRIVE_SPEED; // Forward/backward movement
            double strafe = gamepad1.left_stick_x * SAFE_STRAFE_SPEED;  // Left/right movement
            double turn = gamepad1.right_stick_x * SAFE_TURN_SPEED; // Turn left/right

            if(gamepad1.dpad_up){
                drive = SAFE_DRIVE_SPEED / 2.0;
            } else if(gamepad1.dpad_down) {
                drive = -SAFE_DRIVE_SPEED / 2.0;
            } else if(gamepad1.dpad_left) {
                strafe = -SAFE_STRAFE_SPEED / 2.0;
            } else if(gamepad1.dpad_right) {
                strafe = SAFE_STRAFE_SPEED / 2.0;
            }

            robot.moveRobot(drive, strafe, turn);

            // Optional telemetry
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Height:", height);
            telemetry.addData("Slide Ticks:", slide_motor.getCurrentPosition());
            telemetry.addData("Rotation:", rotation);
            telemetry.addData("HandServo", intakeHand.getPosition());
            telemetry.addData("HandServoRotation", intakeHandRotation);
            telemetry.addData("WristRotation", wristRotation.getPosition());
            telemetry.addData("WristServoRotation", intakeWristRotation);
            telemetry.update();
        }
    }

    private void movement(double seconds, double Y_LEFT_INPUT, double X_LEFT_INPUT, double X_RIGHT_INPUT){
        // Get gamepad inputs
        double forward = -Y_LEFT_INPUT; // Forward/backward movement
        double strafe = X_LEFT_INPUT;  // Left/right movement
        double turn = X_RIGHT_INPUT;   // Turn left/right

        // Calculate motor powers
        double frontLeftPower = forward + strafe + turn;
        double frontRightPower = forward - strafe - turn;
        double backLeftPower = forward - strafe + turn;
        double backRightPower = forward + strafe - turn;

        // Normalize motor powers to stay within the range of -1 to 1
        double maxPower = Math.max(Math.abs(frontLeftPower), Math.max(Math.abs(frontRightPower),
                Math.max(Math.abs(backLeftPower), Math.abs(backRightPower))));
        if (maxPower > 1) {
            frontLeftPower /= maxPower;
            frontRightPower /= maxPower;
            backLeftPower /= maxPower;
            backRightPower /= maxPower;
        }

        // Set motor powers
        frontleft.setPower(frontLeftPower);
        frontright.setPower(frontRightPower);
        backleft.setPower(backLeftPower);
        backright.setPower(backRightPower);

        // Optional telemetry
        telemetry.addData("Front Left Power", frontLeftPower);
        telemetry.addData("Front Right Power", frontRightPower);
        telemetry.addData("Back Left Power", backLeftPower);
        telemetry.addData("Back Right Power", backRightPower);
        telemetry.update();

        sleep((long) (seconds * 1000));
    }

    private void setSlidesrotation(int rot, double pow) {
        slidesrotation.setTargetPosition(rot);
        slidesrotation.setPower(pow);
    }

    private void setSlides(int Height, double pow) {
        slide_motor.setTargetPosition(Height);
        slide_motor.setPower(pow);
    }

    private void setIntakeHand(double hand){
        intakeHand.setPosition(hand);
    }

    private void setWristRotation(double wrist){
        wristRotation.setPosition(wrist);
    }
}
