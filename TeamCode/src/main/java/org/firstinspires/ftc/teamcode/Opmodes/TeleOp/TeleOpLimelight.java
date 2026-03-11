package org.firstinspires.ftc.teamcode.Opmodes.TeleOp;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Actions.CustomActions;
import org.firstinspires.ftc.teamcode.LimelightHelper;
import org.firstinspires.ftc.teamcode.Subsystems.DoublePark;
import org.firstinspires.ftc.teamcode.Subsystems.Drive;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.Subsystems.ShooterHood;
import org.firstinspires.ftc.teamcode.Subsystems.Turret;
import org.firstinspires.ftc.teamcode.Subsystems.TurretGate;
import org.firstinspires.ftc.teamcode.Util.AllianceManager;

import java.util.ArrayList;
import java.util.List;

@TeleOp(name = "TeleOpLimelight", group = "TeleOp")
public class TeleOpLimelight extends LinearOpMode {

    boolean isStarted = false;
    public double shooterPowerDistance;
    private List<Action> runningActions = new ArrayList<>();

    @Override
    public void runOpMode() throws InterruptedException {
        Drive drive = new Drive(hardwareMap);
        Intake intake = new Intake(hardwareMap);
        Shooter shooter = new Shooter(hardwareMap);
        ShooterHood shooterHood = new ShooterHood(hardwareMap);
        LimelightHelper limelightHelper = new LimelightHelper(hardwareMap);
        AllianceManager allianceManager = new AllianceManager();
        TurretGate turretGate = new TurretGate(hardwareMap);
        DoublePark doublePark = new DoublePark(hardwareMap);
        CustomActions customActions = new CustomActions(hardwareMap);

        Turret turret = new Turret(hardwareMap);

        waitForStart();
        while (opModeIsActive() && !isStopRequested()) {

            if (!isStarted) {
                isStarted = true;
                intake.state = Intake.State.FORWARD;
                shooter.state = Shooter.State.CLOSE;
                shooterHood.state = ShooterHood.State.CLOSE;
                turretGate.state = TurretGate.State.CLOSE;
                doublePark.state = DoublePark.State.IN;

               // turret.resetTimer();
            }

            if (!allianceManager.isRedAlliance && !allianceManager.isBlueAlliance) {
                allianceManager.isRedAlliance = true;
            }

            limelightHelper.isReadyToShoot();
            shooterPowerDistance = shooter.ShooterPowerDistance(limelightHelper.getDistance());

            drive.update(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
            //shooter.update();
            intake.update();
            //shooterHood.update();
            turretGate.update();
            //doublePark.update();


            //used for far
            if (limelightHelper.getDistance() < 0 ) {
                shooter.setVelocityRPM(3650);
            }
            else{
                //shooter.setVelocityRPM(3200);
                shooter.setVelocityRPM(shooterPowerDistance);
            }

            /*if (limelightHelper.getDistance() > 0 && limelightHelper.getDistance() < 60){
                shooterHood.state = ShooterHood.State.DOWN;
                //shooter.state = Shooter.State.CLOSE;
            } else if (limelightHelper.getDistance() >= 60 && limelightHelper.getDistance() < 80) {
                shooterHood.state = ShooterHood.State.CLOSE;
            }else if (limelightHelper.getDistance() >= 80 && limelightHelper.getDistance() < 120) {
                shooterHood.state = ShooterHood.State.MIDDLE;
                //shooter.state = Shooter.State.MIDDLE;
            } else {
                shooterHood.state = ShooterHood.State.UP;
                //shooter.state = Shooter.State.FAR;
            }*/

           // turret.update(limelightHelper);

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
                //shooterHood.state = ShooterHood.State.CLOSE;
            }
            if (gamepad1.a) {
                shooter.stopMotor();
                //shooterHood.state = ShooterHood.State.REST;
            }
            if (gamepad1.x) {
                shooter.state = Shooter.State.FAR;
                //shooterHood.state = ShooterHood.State.FAR;
            }
            if (gamepad1.b) {
                shooter.state = Shooter.State.MIDDLE;
                //shooterHood.state = ShooterHood.State.MIDDLE;
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
                shooter.offset += 25;
            }
            if (gamepad2.dpad_down) {
                shooter.offset -= 25;
            }

            TelemetryPacket packet = new TelemetryPacket();

            // update running actions
            List<Action> newActions = new ArrayList<>();
            for (Action action : runningActions) {
                action.preview(packet.fieldOverlay());
                if (action.run(packet)) {
                    newActions.add(action);
                }
            }
            runningActions = newActions;

            if (gamepad2.right_bumper) {
                runningActions.add(new SequentialAction(

                        customActions.turretGateOpen,
                        new SleepAction(1.0),
                        customActions.turretGateClose

                ));
            }

            telemetry.addData("Shooter Power For Left Motor:", shooter.ShooterMotorLeft.getVelocity());
            telemetry.addData("Shooter Power For Right Motor:", shooter.ShooterMotorRight.getVelocity());
            telemetry.addData("Left PIDFCoeff : ", shooter.ShooterMotorLeft.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER));
            telemetry.addData("Right PIDFCoeff : ", shooter.ShooterMotorRight.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER));
            telemetry.addData("State Shooter:" , shooter.state);
            telemetry.addData("Shooter telemetry: ", shooter.getShooterTelemetry());
            telemetry.addData("Limelight telemetry: ", limelightHelper.getLimelightTelemetry());
            telemetry.update();
        }
    }
}
