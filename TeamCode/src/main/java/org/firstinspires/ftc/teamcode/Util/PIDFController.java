package org.firstinspires.ftc.teamcode.Util;

import com.qualcomm.robotcore.util.ElapsedTime;

public class PIDFController {

    private PIDFParams params;
    private ElapsedTime timer = new ElapsedTime();

    private double prevError = 0.0;
    private double integral = 0.0;
    private double pastTime = 0.0;

    public PIDFController(PIDFParams params) {
        this.params = params;
    }

    public void setPIDF(PIDFParams pidfParams) {
        params = pidfParams;
    }

    public double calculate(double error, double armAngle) {
        double dt = timer.seconds() - pastTime;
        integral += (error - prevError) / dt;

        double derivative = (error - prevError) /dt;
        prevError = error;

        double ff;
        if (armAngle < Math.PI) {
            ff = Math.min(0.0, Math.sin(armAngle)) * params.kf;
        } else {
            ff = Math.min(0.0, -Math.sin(Math.PI - (armAngle - Math.PI))) * params.kf;
        }

        double controlEffect = Math.max(-1.0, Math.min(1.0, (derivative * params.kd + integral * params.kf + error * params.kp)));
        pastTime = timer.seconds();

        return controlEffect;

    }

    public double calculate(double error) {
        double dt = timer.seconds() - pastTime;
        integral += (error * dt);
        double derivative = (error - prevError) / dt;
        prevError = error;

        double controlEffect = Math.max(-1.0, Math.min(1.0, (derivative * params.kd + integral * params.kf + error * params.kp)));

        pastTime = timer.seconds();

        return controlEffect;
    }

}
