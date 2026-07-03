package org.firstinspires.ftc.teamcode.Opmodes.Tuning;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.Subsystems.ShooterHood;
import org.firstinspires.ftc.teamcode.Subsystems.TurretGate;

@Config
@TeleOp(name = "Shooter PIDF Tuning")
public class ShooterPIDFTuning extends LinearOpMode {

    public static double TARGET_RPM = 2650;
//hello4
    public static double kP = 1;
    public static double kI = 0;
    public static double kD = 0;
    public static double kF = 17.75;

    @Override
    public void runOpMode() {

        Shooter shooter = new Shooter(hardwareMap);
        Intake intake = new Intake(hardwareMap);
        ShooterHood shooterHood = new ShooterHood(hardwareMap);
        TurretGate turretGate = new TurretGate(hardwareMap);
        boolean isStarted = false;

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        waitForStart();

        while(opModeIsActive()) {

            if (!isStarted) {
                isStarted = true;
                intake.state = Intake.State.FEED;
                //shooter.state = Shooter.State.MIDDLE;
                shooterHood.state = ShooterHood.State.MIDDLE;
                turretGate.state = TurretGate.State.CLOSE;
                //doublePark.state = DoublePark.State.IN;
            }

            shooter.setPIDF(
                    kP,
                    kI,
                    kD,
                    kF);

            shooter.setVelocityRPM(TARGET_RPM);

            if (gamepad1.dpad_up) {
                TARGET_RPM += 25;
            }
            if (gamepad1.dpad_down) {
                TARGET_RPM -= 25;
            }

            if (gamepad1.right_bumper) {
                turretGate.state = TurretGate.State.OPEN;
                intake.state = Intake.State.FEED;
            }
            if (gamepad1.left_bumper) {
                turretGate.state = TurretGate.State.CLOSE;
                intake.state = Intake.State.FORWARD;
            }

            intake.update();
            turretGate.update();
            shooterHood.update();

            double targetTPS =
                    TARGET_RPM / 60.0 * 28.0;

            double leftRPM =
                    shooter.getLeftRPM();

            double rightRPM =
                    shooter.getRightRPM();

            telemetry.addData(
                    "TargetRPM",
                    TARGET_RPM);

            telemetry.addData(
                    "ActualRPMLeft",
                    leftRPM);

            telemetry.addData(
                    "ActualRPMRight",
                    rightRPM);

            telemetry.addData(
                    "TargetTPS",
                    targetTPS);

            telemetry.addData(
                    "LeftVelocity",
                    shooter.getLeftVelocity());

            telemetry.addData(
                    "RightVelocity",
                    shooter.getRightVelocity());

            telemetry.addData(
                    "RPMError",
                    TARGET_RPM - leftRPM);

            telemetry.addData(
                    "LeftTPS",
                    shooter.ShooterMotorLeft.getVelocity());

            telemetry.addData(
                    "RightTPS",
                    shooter.ShooterMotorRight.getVelocity());

            telemetry.addData(
                    "Battery",
                    hardwareMap.voltageSensor
                            .iterator()
                            .next()
                            .getVoltage());

            telemetry.update();
        }
    }
}
