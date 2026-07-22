package org.firstinspires.ftc.teamcode.Opmodes.Tuning;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.LimelightHelper;
import org.firstinspires.ftc.teamcode.Subsystems.Drive;
import org.firstinspires.ftc.teamcode.Subsystems.Turret;
import org.firstinspires.ftc.teamcode.Util.AllianceManager;

@TeleOp(name = "Turret Tuning", group = "tuning")
public class TurretTuning extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        LimelightHelper limelightHelper = new LimelightHelper(hardwareMap);
        Turret turret = new Turret(hardwareMap);
        AllianceManager allianceManager = new AllianceManager();
        Drive drive = new Drive(hardwareMap);

        double[] stepSizes = {0.1, 0.01, 0.001, 0.0001, 0.00001};
        int stepIndex = 2;

        boolean lastB = false;
         boolean lastDpadLeft = false;
         boolean lastDpadRight = false;
         boolean lastDpadUp = false;
         boolean lastDpadDown = false;

        waitForStart();
        while (opModeIsActive()) {

            allianceManager.isRedAlliance = true;

            turret.update(limelightHelper);
            drive.update(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

            // 1. Fetch current target tracking data from your camera setup [0:1.243]
            // Example: targetTag = webcam.getTagByID(20);

            // 2. Process runtime tuning buttons (Gamepad 1 or 2) [0:1.263]
            double currentStep = stepSizes[stepIndex];

            // Press B to cycle through precision adjustments [0:1.263]
            if (gamepad1.b && !lastB) {
                stepIndex = (stepIndex + 1) % stepSizes.length;
            }
            lastB = gamepad1.b;

            // D-Pad Left/Right controls Proportional gain (kP) [0:1.264]
            if (gamepad1.dpad_right && !lastDpadRight) {
                turret.setKP(turret.getKP() + currentStep);
            }
            if (gamepad1.dpad_left && !lastDpadLeft) {
                turret.setKP(turret.getKP() - currentStep);
            }
            lastDpadRight = gamepad1.dpad_right;
            lastDpadLeft = gamepad1.dpad_left;

            // D-Pad Up/Down controls Derivative gain (kD) [0:1.266]
            if (gamepad1.dpad_up && !lastDpadUp) {
                turret.setKD(turret.getKD() + currentStep);
            }
            if (gamepad1.dpad_down && !lastDpadDown) {
                turret.setKD(turret.getKD() - currentStep);
            }
            lastDpadUp = gamepad1.dpad_up;
            lastDpadDown = gamepad1.dpad_down;

            // 3. Keep the hardware tracking the target tag [0:1.250]

            // 4. Output live metrics to the driver station telemetry [0:1.269]
            telemetry.addLine("=== PID LIVE TUNING ===");
            telemetry.addData("Current Step Size", stepSizes[stepIndex]);
            telemetry.addData("kP (Proportional)", "%.5f", turret.getKP());
            telemetry.addData("kD (Derivative)", "%.5f", turret.getKD());
            telemetry.addLine("-----------------------");
            telemetry.update();
        }
    }

        }