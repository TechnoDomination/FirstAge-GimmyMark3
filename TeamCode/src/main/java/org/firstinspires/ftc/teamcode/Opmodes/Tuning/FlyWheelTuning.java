package org.firstinspires.ftc.teamcode.Opmodes.Tuning;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@TeleOp(name = "FlyWheel Tuning", group = "Tuning")
public class FlyWheelTuning extends OpMode {

    public DcMotorEx ShooterMotorLeft;
    public DcMotorEx ShooterMotorRight;

    public double highVelocity = 1200;
    public double lowVelocity = 900;

    double curTargetVelocity = highVelocity;

    double F = 16.9;
    double P = 33.01;

    double[] stepSizes = {10.0,1.0,0.1,0.01,0.001,0.0001};
    int stepIndex = 0;

    @Override
    public void init() {
        ShooterMotorLeft = hardwareMap.get(DcMotorEx.class, "ShooterMotorLeft");
        ShooterMotorRight = hardwareMap.get(DcMotorEx.class, "ShooterMotorRight");
        ShooterMotorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        ShooterMotorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        ShooterMotorLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        ShooterMotorRight.setDirection(DcMotorSimple.Direction.REVERSE);
        ShooterMotorLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        ShooterMotorRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        PIDFCoefficients pidfNew = new PIDFCoefficients(P, 0, 0, F);
        ShooterMotorLeft.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfNew);
        ShooterMotorRight.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfNew);
    }

    @Override
    public void loop() {

        if (gamepad1.yWasPressed()) {
            if (curTargetVelocity == highVelocity){
                curTargetVelocity = lowVelocity;
            } else {curTargetVelocity = highVelocity;}
        }

        if (gamepad1.bWasPressed()){
            stepIndex = (stepIndex + 1)%stepSizes.length;
        }

        if (gamepad1.dpadLeftWasPressed()){
            F -= stepSizes[stepIndex];
        }

        if (gamepad1.dpadRightWasPressed()){
            F += stepSizes[stepIndex];
        }

        if (gamepad1.dpadDownWasPressed()){
            P -= stepSizes[stepIndex];
        }

        if (gamepad1.dpadUpWasPressed()){
            P += stepSizes[stepIndex];
        }

        PIDFCoefficients pidfNew = new PIDFCoefficients(P, 0, 0, F);
        ShooterMotorLeft.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfNew);
        ShooterMotorRight.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfNew);

        ShooterMotorLeft.setVelocity(curTargetVelocity);
        ShooterMotorRight.setVelocity(curTargetVelocity);

        double curVelocityLeft = ShooterMotorLeft.getVelocity();
        double curVelocityRight = ShooterMotorRight.getVelocity();

        double errorLeft = curTargetVelocity - curVelocityLeft;
        double errorRight = curTargetVelocity - curVelocityRight;

        telemetry.addData("Target Velocity: ", curTargetVelocity);
        telemetry.addData("Current Velocity Left: ", "%.2f",curVelocityLeft);
        telemetry.addData("Current Velocity Right: ", "%.2f",curVelocityRight);
        telemetry.addData("Error Left: ", "%.2f",errorLeft);
        telemetry.addData("Error Right: ", "%.2f",errorRight);
        telemetry.addLine("--------------------------------------");
        telemetry.addData("Tuning P: ", "%.4f (D-Pad U/D)", P);
        telemetry.addData("Tuning F: ", "%.4f (D-Pad L/R)", F);
        telemetry.addData("Step Size: ","%.4f (B Button)", stepSizes[stepIndex]);
    }
}
