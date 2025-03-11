package org.firstinspires.ftc.teamcode.WireFireFTC.TeleOP;

import com.qualcomm.ftccommon.configuration.EditLynxModuleActivity;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.WireFireFTC.Robot.WireFireRobotClass;

@TeleOp(name = "BasicMoveTest", group = "Linear OpMode")
public class basicMoveTest extends LinearOpMode {
    WireFireRobotClass robot = new WireFireRobotClass(this);

    //Power Multipliers
    final double SAFE_DRIVE_SPEED = 0.8;
    final double SAFE_STRAFE_SPEED = 0.8;
    final double SAFE_TURN_SPEED = 0.5;

    //Local parameter
    ElapsedTime stopTime = new ElapsedTime();
    boolean autoHeading = false;

    @Override
    public void runOpMode() {
        robot.initialize(true);

        while(opModeInInit()) {
            telemetry.addData("This is the basic move test, a test teleop mode", "Touch Play to Drive");
        }

        waitForStart();
        while(opModeIsActive()) {
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
        }
    }
}
