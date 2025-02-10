package org.firstinspires.ftc.teamcode.WireFireFTC.Robot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class WireFireRobotClass {
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

    private LinearOpMode myOpMode;
    private ElapsedTime holdTimer = new ElapsedTime();

    private boolean showTelemetry = false;
    public WireFireRobotClass(LinearOpMode opMode) {
        myOpMode = opMode;
    }

    public void initialize(boolean showTelemetry) {
        //Initialize the hardware variables
        //motor/device must match the names assigned during the robot configuration
        frontleft = setupDriveMotor("frontleft", DcMotor.Direction.REVERSE);
        frontright = setupDriveMotor("frontright", DcMotor.Direction.FORWARD);
        backleft = setupDriveMotor("backleft", DcMotorSimple.Direction.REVERSE);
        backright = setupDriveMotor("backright", DcMotorSimple.Direction.FORWARD);
    }

    private DcMotor setupDriveMotor(String deviceName, DcMotor.Direction direction) {
        DcMotor aMotor = myOpMode.hardwareMap.get(DcMotor.class, deviceName);
        aMotor.setDirection(direction);
        aMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        aMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        aMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        return aMotor;
    }

    public void moveRobot(double drive, double strafe, double turn) {
        // Calculate motor powers
        double fL = drive + strafe + turn;
        double fR = drive - strafe - turn;
        double bL = drive - strafe + turn;
        double bR = drive + strafe - turn;

        // Normalize motor powers to stay within the range of -1 to 1
        double maxPower = Math.max(Math.abs(fL), Math.max(Math.abs(fR), Math.max(Math.abs(bL), Math.abs(bR))));
        if (maxPower > 1) {
            fL /= maxPower;
            fR /= maxPower;
            bL /= maxPower;
            bR /= maxPower;
        }

        // Set motor powers
        frontleft.setPower(fL);
        frontright.setPower(fR);
        backleft.setPower(bL);
        backright.setPower(bR);

        if(showTelemetry) {
            myOpMode.telemetry.addData("Front left/Right", "%4.2f, %4.2f", fL, fR);
            myOpMode.telemetry.addData("Back  left/Right", "%4.2f, %4.2f", bL, bR);
        }
    }

    public void stopRobot() {
        moveRobot(0.0,0.0,0.0);
    }

    public void showTelemetry(boolean show) {
        showTelemetry = show;
    }
}
