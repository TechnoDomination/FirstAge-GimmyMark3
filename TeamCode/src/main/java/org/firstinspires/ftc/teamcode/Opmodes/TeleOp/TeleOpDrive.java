package org.firstinspires.ftc.teamcode.Opmodes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Drive;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.Subsystems.ShooterHood;

@TeleOp(name = "TeleOpDrive", group = "TeleOp")
public class TeleOpDrive extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        Drive drive = new Drive(hardwareMap);
        Intake intake = new Intake(hardwareMap);
        Shooter shooter = new Shooter(hardwareMap);
        ShooterHood shooterHood = new ShooterHood(hardwareMap);
        boolean isStarted = false;


        waitForStart();
        while (opModeIsActive() && !isStopRequested()) {

            if (!isStarted) {
                isStarted = true;
                intake.state = Intake.State.FORWARD;
                shooter.state = Shooter.State.CLOSE;
                shooterHood.state = ShooterHood.State.CLOSE;
            }



            drive.update(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

            //Intake
            if (gamepad1.dpad_up) {
                intake.state = Intake.State.FORWARD;
            }
            if (gamepad1.dpad_down) {
                intake.state = Intake.State.BACKWARD;
            }
            if (gamepad1.dpad_right) {
                intake.state = Intake.State.FEED;
            }
            if (gamepad1.dpad_left) {
                intake.state = Intake.State.REST;
            }

            //Shooter
            if (gamepad1.y) {
                shooter.state = Shooter.State.CLOSE;
                shooterHood.state = ShooterHood.State.CLOSE;
            }
            if (gamepad1.a) {
                shooter.state = Shooter.State.REST;
                shooterHood.state = ShooterHood.State.REST;
            }
            if (gamepad1.x) {
                shooter.state = Shooter.State.FAR;
                shooterHood.state = ShooterHood.State.FAR;
            }
            if (gamepad1.b) {
                shooter.state = Shooter.State.MIDDLE;
                shooterHood.state = ShooterHood.State.MIDDLE;
            }

        }
    }
}
