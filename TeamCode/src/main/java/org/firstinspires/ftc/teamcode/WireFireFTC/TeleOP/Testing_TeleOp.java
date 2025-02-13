package org.firstinspires.ftc.teamcode.WireFireFTC.TeleOP;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.WireFireFTC.Robot.WireFireRobotClass;

@TeleOp(name = "Testing-TeleOp", group = "Linear OpMode")
public class Testing_TeleOp extends LinearOpMode {
    WireFireRobotClass robot = new WireFireRobotClass(this);
    //Used for Telemetry
    private ElapsedTime runtime = new ElapsedTime();

    //Power Multipliers
    final double SAFE_DRIVE_SPEED = 0.9;
    final double SAFE_STRAFE_SPEED = 0.9;
    final double SAFE_TURN_SPEED = 0.85;

    //Create Variables for rotating slides
    int rotation = 0;

    //Create Variables for slides
    int height = 0;

    //Create Variables for Servo
    double intakeHandRotation = 0.0;

    double intakeWristRotation = 0.0;

    //Create the objects for sensors
    //private ColorSensor colorSensor = null;

    @Override
    public void runOpMode() {

        while (opModeInInit()) {
            telemetry.addData(">", "Touch Play to Drive");
        }

        // Wait for the game to start (driver presses START)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        //Setting all target positions to zero so it doesn't jitter
        waitForStart();
        runtime.reset();

        // Main control loop
        while (opModeIsActive()) {
            //Used for Slides_Motor see slide code for details
            if (gamepad2.left_stick_y > 0.0) {
                robot.SlidesRotation(false);
            } else if (gamepad2.left_stick_y < 0.0) {
                robot.SlidesRotation(true);
            }

            //Code for Slides using values to determine how long for the motors to be set until it reaches Target Position
            if (rotation < 1150) {
                if (gamepad2.right_stick_y > 0.0) {
                    robot.Slides(false);
                } else if (gamepad2.right_stick_y < 0.0) {
                    robot.Slides(true);
                }
            } else if (rotation > 1150) {
                if (gamepad2.right_stick_y > 0.0) {
                    robot.SlidesAdjusted(false);
                } else if (gamepad2.right_stick_y < 0.0) {
                    robot.SlidesAdjusted(true);
                }
            }


            //IntakeHand Servo
            if (gamepad2.right_trigger > 0.0) {
                robot.Claw(true);
            } else {
                robot.Claw(false);
            }

            //Wrist Servo
            if (gamepad2.left_bumper) {
                robot.Wrist(false);
            } else if (gamepad2.right_bumper) {
                robot.Wrist(true);
            }

            //Preset
            if (gamepad2.dpad_up) { //for the high basket
                height = 4500;
                robot.setSlides(height, 0.7);
                intakeWristRotation = 0.38;
                robot.setWristRotation(intakeWristRotation);
                sleep(250);
                rotation = 1300;
                robot.setSlidesrotation(rotation, 0.3);

            } else if (gamepad2.dpad_right) { //for the human player
                height = 1640;
                robot.setSlides(height, 0.7);
                intakeWristRotation = 0.52;
                robot.setWristRotation(intakeWristRotation);
                rotation = 393;
                robot.setSlidesrotation(rotation, 0.3);

            } else if (gamepad2.dpad_left) { //for the specimens on the bar
                height = 3260;
                robot.setSlides(height, 0.7);
                intakeWristRotation = 0.84;
                robot.setWristRotation(intakeWristRotation);
                sleep(250);
                rotation = 650;
                robot.setSlidesrotation(rotation, 0.3);
            } else if (gamepad2.dpad_down) { //retract
                height = 100;
                robot.setSlides(height, 1.0);
                sleep(150);
                rotation = 0;
                robot.setSlidesrotation(rotation, 0.4);
                intakeWristRotation = 0.2;
                robot.setWristRotation(intakeWristRotation);
            }


            // Get gamepad inputs
            double drive = -gamepad1.left_stick_y * SAFE_DRIVE_SPEED; // Forward/backward movement
            double strafe = gamepad1.left_stick_x * SAFE_STRAFE_SPEED;  // Left/right movement
            double turn = gamepad1.right_stick_x * SAFE_TURN_SPEED; // Turn left/right

            if (gamepad1.dpad_up) {
                drive = SAFE_DRIVE_SPEED / 2.0;
            } else if (gamepad1.dpad_down) {
                drive = -SAFE_DRIVE_SPEED / 2.0;
            } else if (gamepad1.dpad_left) {
                strafe = -SAFE_STRAFE_SPEED / 2.0;
            } else if (gamepad1.dpad_right) {
                strafe = SAFE_STRAFE_SPEED / 2.0;
            }

            robot.moveRobot(drive, strafe, turn);

            // Optional telemetry
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Height:", height);
            telemetry.addData("Rotation:", rotation);
            telemetry.addData("HandServoRotation", intakeHandRotation);
            telemetry.addData("WristServoRotation", intakeWristRotation);
            telemetry.update();
        }
    }
}