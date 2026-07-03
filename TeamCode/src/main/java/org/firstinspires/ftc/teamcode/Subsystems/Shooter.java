package org.firstinspires.ftc.teamcode.Subsystems;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;


public class Shooter {

    public static Shooter instance;
    public State state = State.REST;
    public double shooterMotorRPM;
    public boolean isTargetReached = false;
    public double targetRPM = 0.0;
    public double targetVelocityTPS = 0.0;
    public DcMotorEx ShooterMotorLeft;
    public DcMotorEx ShooterMotorRight;
    DcMotorEx motorExLeft;
    public double setRPMdistance = 0.0;
    public boolean isVelReached = true;
    public double offset = 0.0;//-280.0;
    public double currVelToCheck = 4000.0;
    public static final double NEW_I = 0.0000003;
    public static final double NEW_D = 0.0;


    public static final double INITIAL_voltage = 13.41;
    public static final double INITIAL_rightF = 18;
    public static final double INITIAL_leftF = 18;
    public static final double CalcVol_leftF = INITIAL_leftF * 12 / INITIAL_voltage;
    public static final double CalcVol_rightF = INITIAL_rightF * 12 / INITIAL_voltage;
    public static

    PIDFCoefficients pidfNewLeft = new PIDFCoefficients(CalcVol_leftF/10+0.5, NEW_I, NEW_D, CalcVol_leftF);
    PIDFCoefficients pidfNewRight = new PIDFCoefficients(CalcVol_rightF/10+0.5, NEW_I, NEW_D, CalcVol_rightF);

    public Shooter(HardwareMap hardwareMap) {
        ShooterMotorLeft = hardwareMap.get(DcMotorEx.class, "ShooterMotorLeft");
        ShooterMotorRight = hardwareMap.get(DcMotorEx.class, "ShooterMotorRight");
        ShooterMotorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        ShooterMotorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        ShooterMotorLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        ShooterMotorRight.setDirection(DcMotorSimple.Direction.FORWARD);
        ShooterMotorLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        ShooterMotorRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        ShooterMotorLeft.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfNewLeft);
        ShooterMotorRight.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfNewRight);
        instance = this;
    }

    public void setPIDF (double p, double i, double d, double f){
        ShooterMotorLeft.setVelocityPIDFCoefficients(p,i,d,f);
        ShooterMotorRight.setVelocityPIDFCoefficients(p,i,d,f);
    }

    public double getLeftVelocity() {
        return ShooterMotorLeft.getVelocity();
    }

    public double getRightVelocity() {
        return ShooterMotorRight.getVelocity();
    }

    public double getLeftRPM() {
        return ShooterMotorLeft.getVelocity() * 60.0 / 28.0;
    }

    public double getRightRPM() {
        return ShooterMotorRight.getVelocity() * 60.0 / 28.0;
    }

    public double ShooterPowerDistance(double distanceFromGoal) {

        //setRPMdistance = (0.00725174 * Math.pow(distanceFromGoal, 3)) - (1.78957 * Math.pow(distanceFromGoal, 2)) + (152.94642 * distanceFromGoal) - 892.15026;
        //y=-0.00144221x^{3}+0.577479x^{2}-58.38114x+4656.98818
        //setRPMdistance = (-0.00144221 * Math.pow(distanceFromGoal, 3)) + (0.577479 * Math.pow(distanceFromGoal, 2)) - (58.38114 * distanceFromGoal) + 4656.98818;

        setRPMdistance = (0.15477 * Math.pow(distanceFromGoal, 3)) - (12.9769 * Math.pow(distanceFromGoal, 2)) + (369.25183 * distanceFromGoal) - 373.07039;

        if (setRPMdistance > 0) {
            return setRPMdistance;
        } else {
            return 3000;
        }
    }

    public double ShooterPowerDistance() {

        //setRPMdistance = (0.00725174 * Math.pow(distanceFromGoal, 3)) - (1.78957 * Math.pow(distanceFromGoal, 2)) + (152.94642 * distanceFromGoal) - 892.15026;
        //y=-0.00144221x^{3}+0.577479x^{2}-58.38114x+4656.98818
        //setRPMdistance = (-0.00144221 * Math.pow(distanceFromGoal, 3)) + (0.577479 * Math.pow(distanceFromGoal, 2)) - (58.38114 * distanceFromGoal) + 4656.98818;

            return 3250;
    }


    public boolean setCurrVelCheck(){

        currVelToCheck = getShooterRPM();
        return true;
    }

    public void setPIDFCoeff(double currVoltage){
        double currLeftF = INITIAL_leftF * 12/currVoltage;
        double currRightF = INITIAL_rightF * 12/currVoltage;
        PIDFCoefficients pidfCurrLeft = new PIDFCoefficients(currLeftF/10,
                NEW_I, NEW_D, currLeftF);
        PIDFCoefficients pidfCurrRight = new PIDFCoefficients(currRightF/10,
                NEW_I, NEW_D, currRightF);
        ShooterMotorLeft.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,
                pidfCurrLeft);
        ShooterMotorRight.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,
                pidfCurrRight);
    }


    public double getShooterRPM(){
        shooterMotorRPM = ((ShooterMotorLeft.getVelocity()/28) * 60);
        return shooterMotorRPM;
    }

    public boolean isRPMreached () {
        if (getShooterRPM() >= (currVelToCheck - 50)) {
            return true;
        } else {
            return false;
        }
    }

    public void setVelocityRPM(double targetRPM) {
        // Prevent setting a velocity above the motor's capability.
        // Convert RPM to ticks per second.
        this.targetRPM = targetRPM;
        targetVelocityTPS = (targetRPM / 60) * 28;
        ShooterMotorLeft.setVelocity(targetVelocityTPS+offset);
        ShooterMotorRight.setVelocity(targetVelocityTPS+offset);
    }

    public void stopMotor() {
        ShooterMotorLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        ShooterMotorRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        ShooterMotorLeft.setPower(0.0);
        ShooterMotorRight.setPower(0.0);
    }

    public void setPower() {
        ShooterMotorLeft.setPower(0.25);
        ShooterMotorRight.setPower(0.25);
    }

    public enum State {
        AUTOCLOSERED,
        AUTOCLOSEBLUE,
        CLOSE,
        TOOCLOSE,
        AUTOMIDDLERED,
        AUTOMIDDLEBLUE,
        MIDDLE,
        FAR,
        AUTOFAR,
        AUTOFARRED,
        AUTOFARBLUE,
        REST,
        SHOOTMID,
        SHOOTMIDBLUE,
        SHOOTBACK,
    }

    public void update() {
        switch (state) {
            case AUTOCLOSERED:
                setVelocityRPM(3470);
                break;
            case AUTOCLOSEBLUE:
                setVelocityRPM(3550);//3150
                break;
            case CLOSE:
                setVelocityRPM(3500);
                break;
            case TOOCLOSE:
                setVelocityRPM(2800);
                break;
            case AUTOMIDDLERED:
                setVelocityRPM(3200);
                break;
            case AUTOMIDDLEBLUE:
                setVelocityRPM(3200);
                break;
            case MIDDLE:
                setVelocityRPM(3650);
                break;
            case FAR:
                setVelocityRPM(4650);
                break;
            case AUTOFARRED:
                setVelocityRPM(4650);//3650
                break;
            case AUTOFARBLUE:
                setVelocityRPM(4650);
                break;
            case AUTOFAR:
                setVelocityRPM(4650);
                break;
            case REST:
                ShooterMotorLeft.setPower(0);
                ShooterMotorRight.setPower(0);
                break;
            case SHOOTMID:
                setVelocityRPM(3400);
                break;
            case SHOOTMIDBLUE:
                setVelocityRPM(3200);
                break;
            case SHOOTBACK:
                setVelocityRPM(4600);
                break;
        }

       /* if ((state == State.CLOSE) && ((ShooterMotorLeft.getCurrent(CurrentUnit.AMPS) > 5 || ShooterMotorLeft.getCurrent(CurrentUnit.AMPS) > 5))) {
            ShooterMotorLeft.setPower(0);
            ShooterMotorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        } else if ((state == State.MIDDLE) && ((ShooterMotorLeft.getCurrent(CurrentUnit.AMPS) > 5 || ShooterMotorLeft.getCurrent(CurrentUnit.AMPS) > 5))) {
            ShooterMotorLeft.setPower(0);
            ShooterMotorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        } else if ((state == State.FAR) && ((ShooterMotorLeft.getCurrent(CurrentUnit.AMPS) > 5 || ShooterMotorLeft.getCurrent(CurrentUnit.AMPS) > 5))) {
            ShooterMotorLeft.setPower(0);
            ShooterMotorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }*/
        if (state == State.AUTOCLOSERED && ShooterMotorLeft.getVelocity() >= 700) {
            isTargetReached = true;
        } else if (state == State.MIDDLE && ShooterMotorLeft.getVelocity() >= 100) {
            isTargetReached = true;
        } else if (state == State.AUTOFAR && ShooterMotorLeft.getPower() >=1300) {
            isTargetReached = true;
        } else if (state == State.REST && ShooterMotorLeft.getPower() == 0) {
            isTargetReached = true;
        } else {
            isTargetReached = false;
        }


    }


        public String getShooterTelemetry(){
            String telemetry = "";
            telemetry = telemetry + "\n Shooter Target Velocity = " + targetVelocityTPS;
            telemetry = telemetry + "\n Shooter Target Motor RPM = " + targetRPM;
            telemetry = telemetry + "\n LL calculated RPMdistance = " + setRPMdistance;
            telemetry = telemetry + "\n Shooter Left Actual Velocity = " + ShooterMotorLeft.getVelocity();
            telemetry = telemetry + "\n Shooter Right Actual Velocity = " + ShooterMotorRight.getVelocity();
            telemetry = telemetry + "\n Shooter Actual Motor Left RPM = " + ((ShooterMotorLeft.getVelocity()/28) * 60);
            telemetry = telemetry + "\n Shooter Actual Motor Right RPM = " + ((ShooterMotorRight.getVelocity()/28) * 60);
            telemetry = telemetry + "\n Shooter State = " + state;
            telemetry = telemetry + "\n ";
            return telemetry;
        }
    }

