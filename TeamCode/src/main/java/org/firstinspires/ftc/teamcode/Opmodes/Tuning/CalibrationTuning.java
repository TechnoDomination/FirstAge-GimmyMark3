package org.firstinspires.ftc.teamcode.Opmodes.Tuning;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp (name = "Calibration Tuning", group = "Tuning")
public class CalibrationTuning extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        DcMotorEx ShooterMotorLeft;
        DcMotorEx ShooterMotorRight;
        ShooterMotorLeft = hardwareMap.get(DcMotorEx.class, "ShooterMotorLeft");
        ShooterMotorRight = hardwareMap.get(DcMotorEx.class, "ShooterMotorRight");

        ShooterMotorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        ShooterMotorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        ShooterMotorLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        ShooterMotorRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        ShooterMotorLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        ShooterMotorRight.setDirection(DcMotorSimple.Direction.REVERSE);

        ShooterMotorLeft.setPower(1.0);
        ShooterMotorRight.setPower(1.0);
        ElapsedTime t = new ElapsedTime();

        double maxTicksPerSecLeft = 0;
        int lastPosLeft = ShooterMotorLeft.getCurrentPosition();
        double maxTicksPerSecRight = 0;
        int lastPosRight = ShooterMotorRight.getCurrentPosition();
        double lastTime = t.seconds();

        waitForStart();

        while (t.seconds() < 10.0){
            double now = t.seconds();
            double dt = now - lastTime;
            int posLeft = ShooterMotorLeft.getCurrentPosition();
            int posRight = ShooterMotorRight.getCurrentPosition();
            double velLeft = (posLeft - lastPosLeft) / dt; // ticks per second

            if (now > 0.4 && velLeft > maxTicksPerSecLeft) {
                maxTicksPerSecLeft = velLeft;
            }

            double velRight = (posRight - lastPosRight) / dt; // ticks per second

            if (now > 0.4 && velRight > maxTicksPerSecRight) {
                maxTicksPerSecRight = velRight;
            }

            lastPosLeft = posLeft;
            lastPosRight = posRight;
            lastTime = now;

        }

        ShooterMotorLeft.setPower(0);
        ShooterMotorRight.setPower(0);

        telemetry.addData("MaxTicksPerSecLeft: ", maxTicksPerSecLeft);
        telemetry.addData("MaxTicksPerSecRight: ", maxTicksPerSecRight);
        telemetry.update();
    }
}
