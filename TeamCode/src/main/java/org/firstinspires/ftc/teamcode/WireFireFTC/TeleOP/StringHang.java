package org.firstinspires.ftc.teamcode.WireFireFTC.TeleOP;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.WireFireFTC.Robot.WireFireRobotClass;

@TeleOp(name = "STRhang", group = "Linear OpMode")
public class StringHang extends LinearOpMode {
    WireFireRobotClass robot = new WireFireRobotClass(this);

    private DcMotor STRhang = null;
    int STRlength = 0;
    final double STRlegnth_INCREMENT = 15;
    final int STRlength_MIN_HEIGHT = 0;
    final int STRlength_MAX_HEIGHT = 4500;

    @Override
    public void runOpMode() {
        robot.initialize(true);

        STRhang = hardwareMap.get(DcMotor.class, "String_hang");

        STRhang.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //Reverse if needed
        //STRhang.setDirection(DcMotor.Direction.REVERSE);
        while(opModeInInit()) {
            telemetry.addData("This is the basic move test, a test teleop mode", "Touch Play to Drive");
        }
        STRhang.setTargetPosition(0);
        while(opModeIsActive()) {
            STRhang.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            STRhang.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            //Used for Slides_Motor see slide code for details
            if(gamepad2.left_stick_y > 0.03) {
                STRlength -= STRlegnth_INCREMENT;
                STRlength = Math.max(STRlength_MIN_HEIGHT, Math.min(STRlength_MAX_HEIGHT, STRlength));
                setSTRhang(STRlength, 1.0);
            }
            else if (gamepad2.left_stick_y < -0.03) {
                STRlength += STRlegnth_INCREMENT;
                STRlength = Math.max(STRlength_MIN_HEIGHT, Math.min(STRlength_MAX_HEIGHT, STRlength));
                setSTRhang(STRlength, 1.0);
            }
        }
    }
    private void setSTRhang(int rot, double pow) {
        STRhang.setTargetPosition(rot);
        STRhang.setPower(pow);
    }
}
