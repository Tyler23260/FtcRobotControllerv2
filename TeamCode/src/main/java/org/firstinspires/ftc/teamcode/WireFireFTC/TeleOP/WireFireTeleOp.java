package org.firstinspires.ftc.teamcode.WireFireFTC.TeleOP;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "WireFire-TeleOp",group = "Linear OpMode")
public class WireFireTeleOp extends LinearOpMode {
    //Used for Telemetry
    private ElapsedTime runtime = new ElapsedTime();

    //Used to keep power
    double PWR_MULTIPLIER = 0.83;

    //Create Variables for rotating slides
    int rotation = 0;
    int INCREMENT = 5;

    //Create Variables for slides
    int height = 0;
    double HEIGHT_INCREMENT = 5;
    final int MAX_HEIGHT = 3500;
    final int MIN_HEIGHT = 0;

    //Create Variables for Servo
    int intakeHandRotation = 0;
    double ServoIncrement = 1;
    final int MAX_INTAKEHAND_ROTATION = 1000;
    final int MIN_INTAKEHAND_ROTATION = 0;


    //Create the objects for motors
    private DcMotor frontleft = null;
    private DcMotor frontright = null;
    private DcMotor backleft = null;
    private DcMotor backright = null;

    private DcMotorEx slidesrotation = null;
    private DcMotor slide_motor = null;

    //Create te objects for servos

    private CRServo wristRotation = null;
    private Servo intakeHand = null;


    @Override
    public void runOpMode() {
        // Initialize the Motors
        frontleft = hardwareMap.get(DcMotor.class, "frontleft");
        frontright = hardwareMap.get(DcMotor.class, "frontright");
        backleft = hardwareMap.get(DcMotor.class, "backleft");
        backright = hardwareMap.get(DcMotor.class, "backright");

        slidesrotation = hardwareMap.get(DcMotorEx.class, "rotation_motor");
        slide_motor = hardwareMap.get(DcMotor.class, "slide_motor");

        //Initialize the Servos
        wristRotation = hardwareMap.get(CRServo.class, "wristServo");
        intakeHand = hardwareMap.get(Servo.class, "intakeServo");


        // Set motor directions if needed
        frontleft.setDirection(DcMotor.Direction.REVERSE);
        backleft.setDirection(DcMotor.Direction.REVERSE);
        frontright.setDirection(DcMotor.Direction.FORWARD);
        backright.setDirection(DcMotor.Direction.FORWARD);

        //Set Servo direction if needed
        //intakeHand.etDirection(Servo.Direction.REVERSE);

        //Change RunMode



        slidesrotation.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        slide_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide_motor.setDirection(DcMotor.Direction.REVERSE);

        // Wait for the game to start (driver presses START)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        //Setting all target positions to zero so it doesn't jitter
        waitForStart();
        slide_motor.setTargetPosition(0);
        slidesrotation.setTargetPosition(0);
        runtime.reset();

        // Main control loop
        while (opModeIsActive()) {
            //Start coding here >>>>>>>>>>>>

            //Setting modes and ZeroPower
            slide_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            slide_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            slidesrotation.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slidesrotation.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            //Used for Slides_Motor see slide code for details
            if(gamepad2.left_stick_y > 0.03) {
                rotation += INCREMENT;
                setSlidesrotation(rotation);
            } else if (gamepad2.left_stick_y < -0.03) {
                rotation -= INCREMENT;
                setSlidesrotation(rotation);
            }

            //Code for Slides using values to determine how long for the motors to be set until it reaches Target Position
            if(gamepad2.y) {
                height += HEIGHT_INCREMENT;
                height = Math.max(MIN_HEIGHT, Math.min(MAX_HEIGHT, height));
                setSlides(height);
            } else if (gamepad2.a) {
                height -= HEIGHT_INCREMENT;
                height = Math.max(MIN_HEIGHT, Math.min(MAX_HEIGHT, height));
                setSlides(height);
            }

            //IntakeHand Servo
            if(gamepad2.left_bumper) {
                intakeHandRotation += ServoIncrement;
                setHand(intakeHandRotation);
            }
            else if(gamepad2.right_bumper) {
                intakeHandRotation -= ServoIncrement;
                setHand(intakeHandRotation);
            }

            //Wrist Servo
            if(gamepad2.right_trigger > 0) {
                wristRotation.setPower(2.5);
            }
            else if(gamepad2.left_trigger > 0) {
                wristRotation.setPower(-2.5);
            }
            else {
                wristRotation.setPower(0);
            }

            // Get gamepad inputs
            double forward = gamepad1.left_stick_y; // Forward/backward movement
            double strafe = -gamepad1.left_stick_x;  // Left/right movement
            double turn = gamepad1.right_stick_x;   // Turn left/right

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
            frontleft.setPower(frontLeftPower*PWR_MULTIPLIER);
            frontright.setPower(frontRightPower*PWR_MULTIPLIER);
            backleft.setPower(backLeftPower*PWR_MULTIPLIER);
            backright.setPower(backRightPower*PWR_MULTIPLIER);

            // Optional telemetry
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front left/Right", "%4.2f, %4.2f", frontLeftPower, frontRightPower);
            telemetry.addData("Back  left/Right", "%4.2f, %4.2f", backLeftPower, backRightPower);
            telemetry.addData("height:", height);
            telemetry.addData("left slide ticks:", slide_motor.getCurrentPosition());
            telemetry.addData("rotation:", rotation);
            telemetry.update();

            // Stop all motors when op mode is stopped
            frontleft.setPower(0);
            frontright.setPower(0);
            backleft.setPower(0);
            backright.setPower(0);
        }
    }
    public void setSlidesrotation(int rot) {
        slidesrotation.setTargetPosition(rot);
        slidesrotation.setPower(1.0);
    }

    public void setSlides(int Height) {
        Height = Math.max(MIN_HEIGHT, Math.min(MAX_HEIGHT, height));
        slide_motor.setTargetPosition(Height);
        slide_motor.setPower(1);
    }
    public void setHand(int Rotation){
        Rotation = Math.max(MIN_INTAKEHAND_ROTATION, Math.min(MAX_INTAKEHAND_ROTATION, intakeHandRotation));
        intakeHand.setPosition(Rotation);
    }
}
