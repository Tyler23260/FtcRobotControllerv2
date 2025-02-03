package org.firstinspires.ftc.teamcode.WireFireFTC.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "Rightside")
public class WireFireFTC_RIGHT_SIDE_AUTO extends LinearOpMode {
    //Used for Telemetry
    private ElapsedTime runtime = new ElapsedTime();

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
        initializeServo();
        waitForStart();
        //Start of autonomous
        intakeWristRotation = 0.9;
        setWristRotation(intakeWristRotation);
        intakeHandRotation = 0.0;
        setIntakeHand(intakeHandRotation);
        movement(0.65,0.0,1.0,0.0);
        movement( 0.65, 0.0, 0.0, -0.6);
        movement( 1.0, 0.0, 1.0, 0.0);
        stopMotors();
        for(int i = 0; i < 3; i++) {
        movement(0.3, 0.5, 0.0, 0.0);
        stopMotors();
        movement(0.8, 0.0, -1.0, 0.0);

        }
        movement(0.90,1.0,0.0,0.0);
        stopMotors();
        movement(1.25, 0.0, -2.0, 0.0);
        stopMotors();
        height = 3260;
        setSlides(height, 0.7);
        intakeWristRotation = 0.84;
        setWristRotation(intakeWristRotation);
        sleep(250);
        rotation = 650;
        setSlidesrotation(rotation, 0.5);
        height = 3460;
        setSlides(height, 0.7);
        intakeHandRotation = 0.0;
        setIntakeHand(intakeHandRotation);
    }

    // Method to initialize the motors
    private void initializeMotors() {
        frontleft = hardwareMap.get(DcMotor.class, "frontleft");
        frontright = hardwareMap.get(DcMotor.class, "frontright");
        backleft = hardwareMap.get(DcMotor.class, "backleft");
        backright = hardwareMap.get(DcMotor.class, "backright");

        slidesrotation = hardwareMap.get(DcMotorEx.class, "rotation_motor");
        slide_motor = hardwareMap.get(DcMotor.class, "slide_motor");

        // Set motor directions if needed
        frontleft.setDirection(DcMotor.Direction.REVERSE);
        backleft.setDirection(DcMotor.Direction.REVERSE);
        frontright.setDirection(DcMotor.Direction.FORWARD);
        backright.setDirection(DcMotor.Direction.FORWARD);
    }

    private void initializeServo() {
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