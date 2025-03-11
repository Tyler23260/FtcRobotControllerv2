package org.firstinspires.ftc.teamcode.WireFireFTC.TeleOP;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.WireFireFTC.Robot.WireFireRobotClass;

@TeleOp(name = "SWINGhang", group = "Linear OpMode")
public class SWINGhang extends LinearOpMode {
    WireFireRobotClass robot = new WireFireRobotClass(this);

    private DcMotor SWINGhangR = null;
    private DcMotor SWINGhangL = null;
    int SWINGRotation = 0;
    final double SWINGRotation_INCREMENT = 15;
    final int SWINGRotation_MIN_HEIGHT = 0;
    final int SWINGRotation_MAX_HEIGHT = 2000;

    @Override
    public void runOpMode() {
        robot.initialize(true);

        SWINGhangR = hardwareMap.get(DcMotor.class, "String_hangR");
        SWINGhangL = hardwareMap.get(DcMotor.class, "String_hangL");

        SWINGhangR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        SWINGhangL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //Reverse if needed
        //SWINGhangR.setDirection(DcMotor.Direction.REVERSE);
        //SWINGhangL.setDirection(DcMotor.Direction.REVERSE);
        while(opModeInInit()) {
            telemetry.addData("This is the basic move test, a test teleop mode", "Touch Play to Drive");
        }
        SWINGhangR.setTargetPosition(0);
        SWINGhangL.setTargetPosition(0);
        while(opModeIsActive()) {
            SWINGhangR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            SWINGhangR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            SWINGhangL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            SWINGhangL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            //Used for Slides_Motor see slide code for details
            if(gamepad2.left_stick_y > 0.03) {
                SWINGRotation -= SWINGRotation_INCREMENT;
                SWINGRotation = Math.max(SWINGRotation_MIN_HEIGHT, Math.min(SWINGRotation_MAX_HEIGHT, SWINGRotation));
                setSWINGhang(SWINGRotation, 1.0);
            }
            else if (gamepad2.left_stick_y < -0.03) {
                SWINGRotation += SWINGRotation_INCREMENT;
                SWINGRotation = Math.max(SWINGRotation_MIN_HEIGHT, Math.min(SWINGRotation_MAX_HEIGHT, SWINGRotation));
                setSWINGhang(SWINGRotation, 1.0);
            }
        }
    }
    private void setSWINGhang(int rot, double pow) {
        SWINGhangR.setTargetPosition(rot);
        SWINGhangR.setPower(pow);
        SWINGhangL.setTargetPosition(rot);
        SWINGhangL.setPower(pow);
    }
}