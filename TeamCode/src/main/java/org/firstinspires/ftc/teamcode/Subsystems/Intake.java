package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

public class Intake {
    public static Intake instance;
    public DcMotorEx IntakeMotor;
    public State state = State.OFF;
    public boolean isTargetReached = false;
    private static ElapsedTime timer = new ElapsedTime();
    public static final double NEW_P = 50.0;
    public static final double NEW_I = 0.0;
    public static final double NEW_D = 0.0;
    public static final double NEW_F = 0.000357;
    PIDFCoefficients pidfNew = new PIDFCoefficients(NEW_P, NEW_I, NEW_D, NEW_F);

    public Intake(HardwareMap hardwareMap) {
        IntakeMotor = hardwareMap.get(DcMotorEx.class, "IntakeMotor");
        IntakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        IntakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        IntakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        IntakeMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfNew);
        instance = this;
    }

    public void setVelocityRPM(double targetRPM) {
        // Prevent setting a velocity above the motor's capability.
        // Convert RPM to ticks per second.
        double targetVelocityTPS = (targetRPM / 60) * 28;
        IntakeMotor.setVelocity(targetVelocityTPS);
        //ShooterMotorRight.setVelocity(targetVelocityTPS);
    }

    public void stopMotor() {
        IntakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        IntakeMotor.setPower(0.0);
    }

    public enum State {
        OFF,
        FORWARD,
        FEED,
        BACKWARD,
        SLOWDOWN,
        REST
    }

    public void update() {

        switch (state) {
            case OFF:
                IntakeMotor.setPower(0);
                break;
            case FORWARD:
                setVelocityRPM(1000);
                break;
            case FEED:
                setVelocityRPM(800);
                break;
            case BACKWARD:
                setVelocityRPM(-1000);
                break;
            case SLOWDOWN:
                setVelocityRPM(100);
            case REST:
                setVelocityRPM(0);
        }

        if (IntakeMotor.getCurrent(CurrentUnit.AMPS) > 7.5)
        {
            state = State.SLOWDOWN;
            timer.reset();
        }

        if (state == State.SLOWDOWN && timer.seconds() > 0.5){
            state = State.FORWARD;
        }




        /*if ((state == State.FORWARD) && ((IntakeMotor.getCurrent(CurrentUnit.AMPS) > 5 || IntakeMotor.getCurrent(CurrentUnit.AMPS) > 5))){
            IntakeMotor.setPower(0);
            IntakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }*/

        if (state == State.OFF && IntakeMotor.getPower() == 0) {
            isTargetReached = true;
        } else if (state == State.FORWARD && IntakeMotor.getVelocity() >= 200) {
            isTargetReached = true;
        } else if (state == State.FEED && IntakeMotor.getVelocity() >= 450) {
            isTargetReached = true;
        } else if (state == State.BACKWARD && IntakeMotor.getVelocity() >= 200) {
            isTargetReached = true;
        } else {
            isTargetReached = false;
        }

    }

    public String getIntakeTelemetry(){
        String telemetry = "";
        telemetry = telemetry + "\n Intake Motor Amps = " + IntakeMotor.getCurrent(CurrentUnit.AMPS);
        telemetry = telemetry + "\n Intake Velocity = " + IntakeMotor.getVelocity();
        telemetry = telemetry + "\n ";
        return telemetry;
    }
}
