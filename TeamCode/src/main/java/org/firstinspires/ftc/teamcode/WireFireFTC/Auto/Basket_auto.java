package org.firstinspires.ftc.teamcode.WireFireFTC.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "Basket-Autonomous")
public class Basket_auto extends LinearOpMode {
    //Used for Telemetry
    private ElapsedTime runtime = new ElapsedTime();

    //Used to keep power
    double PWR_MULTIPLIER = 0.77;

    //Create Variables for rotating slides
    int rotation = 0;

    //Create Variables for slides
    int height = 0;

    //Create Variables for Servo
    double intakeHandRotation = 0.0;

    double intakeWristRotation = 0.0;

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

    @Override
    public void runOpMode() {
        initializeMotors();
        intializeServo();
        waitForStart();
        setIntakeHand(0.0);
        movement(0.3, -0.5, 0.0, 0.0);
        stopMotors();
        movement(0.4,0.0,-0.5,0.0);
        stopMotors();
        movement(0.1,0.0,0.0,0.9);
        stopMotors();
        setSlidesrotation(1250,0.3);
        sleep(600);
        setSlides(5000,1.0);
        sleep(750);
        setIntakeHand(0.3);
        /*movement(0.35, 0.0, -0.5, 0.0);
        stopMotors();
        movement(0.2, -0.1, 0.0, 0.0);
        intakeHandRotation = 0.5;
        setIntakeHand(intakeHandRotation);
        rotation = 50;
        setSlidesrotation(rotation, 0.4);
        height = 4000;
        setSlides(height, 1.0);
        sleep(5000);
        rotation = 0;
        setSlidesrotation(rotation,0.7);
        intakeHandRotation = 0.2;
        setIntakeHand(intakeHandRotation);
        intakeWristRotation = 0.9;
        setWristRotation(intakeWristRotation);
        height = 0;
        setSlides(height, 1.0);*/
        sleep(30000);
    }

    // Method to initialize the motors
    private void initializeMotors() {
        frontleft = hardwareMap.get(DcMotorEx.class, "frontleft");
        backleft = hardwareMap.get(DcMotorEx.class, "backleft");
        backright = hardwareMap.get(DcMotorEx.class, "backright");
        frontright = hardwareMap.get(DcMotorEx.class, "frontright");

        slidesrotation = hardwareMap.get(DcMotorEx.class, "rotation_motor");
        slide_motor = hardwareMap.get(DcMotor.class, "slide_motor");

        // Set motor directions if needed
        frontleft.setDirection(DcMotor.Direction.REVERSE);
        backleft.setDirection(DcMotor.Direction.REVERSE);
        frontright.setDirection(DcMotor.Direction.FORWARD);
        backright.setDirection(DcMotor.Direction.FORWARD);

        //Change RunMode
        slidesrotation.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        slide_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide_motor.setDirection(DcMotor.Direction.REVERSE);

        slide_motor.setTargetPosition(0);
        slidesrotation.setTargetPosition(0);

        //Setting modes and ZeroPower
        slide_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        slide_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slidesrotation.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slidesrotation.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    private void intializeServo() {
        //Initialize the Servos
        wristRotation = hardwareMap.get(Servo.class, "wristServo");
        intakeHand = hardwareMap.get(Servo.class, "intakeServo");
    }

    // Method to stop the motors
    private void stopMotors() {
        frontleft.setPower(0);
        frontright.setPower(0);
        backleft.setPower(0);
        backright.setPower(0);
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
