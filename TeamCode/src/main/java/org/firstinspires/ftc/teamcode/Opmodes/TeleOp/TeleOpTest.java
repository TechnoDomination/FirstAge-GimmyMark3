package org.firstinspires.ftc.teamcode.Opmodes.TeleOp;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
//import org.firstinspires.ftc.teamcode.Subsystems.DoublePark;
import org.firstinspires.ftc.teamcode.Subsystems.Drive;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.Subsystems.ShooterHood;
import org.firstinspires.ftc.teamcode.Subsystems.TurretGate;
@Config
@TeleOp(name = "TeleOpTestShooter", group = "TeleOp")
public class TeleOpTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        Drive drive = new Drive(hardwareMap);
        Intake intake = new Intake(hardwareMap);
        Shooter shooter = new Shooter(hardwareMap);
        ShooterHood shooterHood = new ShooterHood(hardwareMap);
        TurretGate turretGate = new TurretGate(hardwareMap);
        FtcDashboard dashboard = FtcDashboard.getInstance();
        Telemetry dashboardTelemetry = dashboard.getTelemetry();
        //DoublePark doublePark = new DoublePark(hardwareMap);
        boolean isStarted = false;

        waitForStart();
        while (opModeIsActive() && !isStopRequested()) {

            if (!isStarted) {
                isStarted = true;
                intake.state = Intake.State.FEED;
                shooter.state = Shooter.State.MIDDLE;
                shooterHood.state = ShooterHood.State.CLOSE;
                turretGate.state = TurretGate.State.CLOSE;
                //doublePark.state = DoublePark.State.IN;
            }

            drive.update(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
            shooter.update();
            intake.update();
            shooterHood.update();
            turretGate.update();
            //doublePark.update();

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
            if (gamepad2.y) {
                //shooter.state = Shooter.State.CLOSE;
                shooterHood.state = ShooterHood.State.CLOSE;
            }
            if (gamepad2.a) {
                //shooter.stopMotor();
                shooterHood.state = ShooterHood.State.REST;
            }
            if (gamepad2.x) {
                //shooter.state = Shooter.State.FAR;
                shooterHood.state = ShooterHood.State.FAR;
            }
            if (gamepad2.b) {
               // shooter.state = Shooter.State.MIDDLE;
                shooterHood.state = ShooterHood.State.MIDDLE;
            }

            if (gamepad2.right_bumper) {
                turretGate.state = TurretGate.State.OPEN;
                intake.state = Intake.State.FEED;
            }
            if (gamepad2.left_bumper) {
                turretGate.state = TurretGate.State.CLOSE;
                intake.state = Intake.State.FORWARD;
            }
            if (gamepad2.dpad_up) {
                shooter.offset += 10;
            }
            if (gamepad2.dpad_down) {
                shooter.offset -= 10;
            }

            telemetry.addData("Shooter Power For Left Motor:", shooter.ShooterMotorLeft.getVelocity());
            telemetry.addData("Shooter Power For Right Motor:", shooter.ShooterMotorRight.getVelocity());
            telemetry.addData("Left PIDFCoeff : ", shooter.ShooterMotorLeft.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER));
            telemetry.addData("Right PIDFCoeff : ", shooter.ShooterMotorRight.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER));
            telemetry.addData("State Shooter:" , shooter.state);
            telemetry.addData("Shooter telemetry: ", shooter.getShooterTelemetry());
            telemetry.addData("TurretGate telemetry: ", turretGate.getTurretGateTelemetry());
            telemetry.addData("Shooter Hood: ", shooterHood.getShooterHoodTelemetry());
            telemetry.addData("Voltage: ",hardwareMap.voltageSensor.iterator().next().getVoltage());
            dashboardTelemetry.addData("Actull Velocity: ", hardwareMap.voltageSensor.iterator().next().getVoltage());
            dashboardTelemetry.update();
            telemetry.update();
        }
    }
}
