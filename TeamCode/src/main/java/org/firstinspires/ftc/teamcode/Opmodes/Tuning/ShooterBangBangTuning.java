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
@TeleOp(name = "Shooter Bang Bang Tuning")
public class ShooterBangBangTuning extends LinearOpMode {

    public static double TARGET_RPM = 2570;

    public static double HOLD_POWER = 0.65;

    public static double RPM_DEADBAND = 25;

    @Override
    public void runOpMode() {

        telemetry = new MultipleTelemetry(
                telemetry,
                FtcDashboard.getInstance().getTelemetry());

        Shooter shooter = new Shooter(hardwareMap);
        Intake intake = new Intake(hardwareMap);
        TurretGate turretGate = new TurretGate(hardwareMap);
        boolean isStarted = false;

        waitForStart();

        while(opModeIsActive()) {

            if (!isStarted) {
                isStarted = true;
                intake.state = Intake.State.FEED;
                //shooter.state = Shooter.State.MIDDLE;
               // shooterHood.state = ShooterHood.State.UP;
                turretGate.state = TurretGate.State.CLOSE;
                //doublePark.state = DoublePark.State.IN;
            }

            double actualRPM = shooter.getLeftRPM();

            double motorPower;

            if(actualRPM < TARGET_RPM - RPM_DEADBAND) {
                motorPower = 1.0;
            }
            else {
                motorPower = HOLD_POWER;
            }

            shooter.setPower(motorPower);

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

            telemetry.addData("TargetRPM", TARGET_RPM);
            telemetry.addData("ActualRPM", actualRPM);
            telemetry.addData("RPMError",
                    TARGET_RPM - actualRPM);
            telemetry.addData("MotorPower", motorPower);
            telemetry.addData("Deadband", RPM_DEADBAND);
            telemetry.addData("HoldPower", HOLD_POWER);

            telemetry.update();
        }
    }
}
