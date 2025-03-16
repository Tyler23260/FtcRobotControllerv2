package org.firstinspires.ftc.teamcode.WireFireFTC.Robot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class WireFireRobotClass{
    //Create Variables for rotating slides
    int rotation = 0;
    final int INCREMENT = 3;
    final int MAX_ROTATION = 1400;
    final int MIN_ROTATION = 0;

    //Create Variables for slides
    int height = 0;
    final double HEIGHT_INCREMENT = 10;
    final int MAX_HEIGHT = 4000;
    final int ADJUSTED_MAX_HEIGHT = 5000;
    final int MIN_HEIGHT = 0;

    //StringHang
    /*private DcMotor STRhang = null;
    int STRlength = 0;
    final double STRlegnth_INCREMENT = 15;
    final int STRlength_MIN_HEIGHT = 0;
    final int STRlength_MAX_HEIGHT = 4500;*/

    //Create Variables for Servo
    double intakeServoHand = 0.0;
    double ServoHandIncrement = 0.025;
    final double MAX_INTAKEHAND_ROTATION = 0.20;
    final double MIN_INTAKEHAND_ROTATION = 0;

    double intakeWristRotation = 0.0;
    double ServoWristIncrement = 0.001;
    final double MAX_INTAKEWRIST_ROTATION = 1.0;
    final double MIN_INTAKEWRIST_ROTATION = 0.0;

    //Create the objects for motors
    private DcMotor frontleft = null;
    private DcMotor frontright = null;
    private DcMotor backleft = null;
    private DcMotor backright = null;

    private DcMotor slidesrotation = null;
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
        backleft = setupDriveMotor("backleft", DcMotor.Direction.REVERSE);
        backright = setupDriveMotor("backright", DcMotor.Direction.FORWARD);

        slidesrotation = setupMotor("rotation_motor", DcMotor.Direction.FORWARD);
        slide_motor = setupMotor("slide_motor", DcMotor.Direction.REVERSE);
        //STRhang = setupMotor("string_hang", DcMotor.Direction.REVERSE);
        //Servo/device must match the names assigned during the robot configuration
        wristRotation = setupServo("wristServo", Servo.Direction.FORWARD);
        intakeHand = setupServo("intakeServo", Servo.Direction.REVERSE);
    }

    //Setups the Drive Motor As Well As Setting Direction
    private DcMotor setupDriveMotor(String deviceName, DcMotor.Direction direction) {
        DcMotor aMotor = myOpMode.hardwareMap.get(DcMotor.class, deviceName);
        aMotor.setDirection(direction);
        aMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        aMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        aMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        return aMotor;
    }

    //Setups the Slide Motor As Well As Setting Direction
    private DcMotor setupMotor(String deviceName, DcMotor.Direction direction) {
        DcMotor aMotor = myOpMode.hardwareMap.get(DcMotor.class, deviceName);
        aMotor.setDirection(direction);
        aMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        aMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        aMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        aMotor.setTargetPosition(0);
        aMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        return aMotor;
    }

    //Setups the Servos As Well As Setting Direction
    private Servo setupServo(String deviceName, Servo.Direction direction) {
        Servo aServo = myOpMode.hardwareMap.get(Servo.class, deviceName);
        aServo.setDirection(direction);
        return aServo;
    }

    //Used for Driving robot
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

    //Stops the Robot Movement
    public void stopRobot() {
        moveRobot(0.0,0.0,0.0);
    }

    //Go to the op Function
    public void showTelemetry(boolean show) {
        showTelemetry = show;
    }

    //For Slide Rotation
    public void SlidesRotation(boolean t) {
        if(t){
            rotation += INCREMENT;
            rotation = Math.max(MIN_ROTATION, Math.min(MAX_ROTATION, rotation));
            setSlidesrotation(rotation, 1.0);
        } else{
            rotation -= INCREMENT;
            rotation = Math.max(MIN_ROTATION, Math.min(MAX_ROTATION, rotation));
            setSlidesrotation(rotation, 1.0);
        }
    }

    //For Slides
    public void Slides(boolean t){
        if(t){
            height += HEIGHT_INCREMENT;
            height = Math.max(MIN_HEIGHT, Math.min(MAX_HEIGHT, height));
            setSlides(height, 1.0);
        } else{
            height -= HEIGHT_INCREMENT;
            height = Math.max(MIN_HEIGHT, Math.min(MAX_HEIGHT, height));
            setSlides(height, 1.0);
        }
    }

    //For Slides Adjusted Height
    public void SlidesAdjusted(boolean t){
        if(t){
            height += HEIGHT_INCREMENT;
            height = Math.max(MIN_HEIGHT, Math.min(ADJUSTED_MAX_HEIGHT, height));
            setSlides(height, 1.0);
        } else{
            height -= HEIGHT_INCREMENT;
            height = Math.max(MIN_HEIGHT, Math.min(ADJUSTED_MAX_HEIGHT, height));
            setSlides(height, 1.0);
        }
    }

    //For Claw
    public void Claw(boolean t){
        if (t) {
            intakeServoHand += ServoHandIncrement;
            intakeServoHand = Math.max(MIN_INTAKEHAND_ROTATION, Math.min(MAX_INTAKEHAND_ROTATION, intakeServoHand));
            setIntakeHand(intakeServoHand);
        } else {
            intakeServoHand -= ServoHandIncrement;
            intakeServoHand = Math.max(MIN_INTAKEHAND_ROTATION, Math.min(MAX_INTAKEHAND_ROTATION, intakeServoHand));
            setIntakeHand(intakeServoHand);
        }
    }

    //For Wrist
    public void Wrist(boolean t) {
        if (t) {
            intakeWristRotation += ServoWristIncrement;
            intakeWristRotation = Math.max(MIN_INTAKEWRIST_ROTATION, Math.min(MAX_INTAKEWRIST_ROTATION, intakeWristRotation));
            setWristRotation(intakeWristRotation);
        } else{
            intakeWristRotation -= ServoWristIncrement;
            intakeWristRotation = Math.max(MIN_INTAKEWRIST_ROTATION, Math.min(MAX_INTAKEWRIST_ROTATION, intakeWristRotation));
            setWristRotation(intakeWristRotation);
        }

    }

    /*public void STRHang(boolean t) {
        if (t) {
            STRlength += STRlegnth_INCREMENT;
            STRlength = Math.max(STRlength_MIN_HEIGHT, Math.min(STRlength_MAX_HEIGHT, STRlength));
            setSTRhang(STRlength, 1.0);
        } else{
            STRlength -= STRlegnth_INCREMENT;
            STRlength = Math.max(STRlength_MIN_HEIGHT, Math.min(STRlength_MAX_HEIGHT, STRlength));
            setSTRhang(STRlength, 1.0);
        }

    }*/

    //For Slide Rotation Movement
    public void setSlidesrotation(int rot, double pow) {
        rotation = rot;
        slidesrotation.setTargetPosition(rot);
        slidesrotation.setPower(pow);
    }

    //For Slide Movement
    public void setSlides(int Height, double pow) {
        height = Height;
        slide_motor.setTargetPosition(Height);
        slide_motor.setPower(pow);
    }

    //For Claw Movement
    public void setIntakeHand(double hand){
        intakeServoHand = hand;
        intakeHand.setPosition(hand);
    }

    //For Wrist Movement
    public void setWristRotation(double wrist){
        intakeWristRotation = wrist;
        wristRotation.setPosition(wrist);
    }

    /*private void setSTRhang(int rot, double pow) {
        STRhang.setTargetPosition(rot);
        STRhang.setPower(pow);
    }*/
}
