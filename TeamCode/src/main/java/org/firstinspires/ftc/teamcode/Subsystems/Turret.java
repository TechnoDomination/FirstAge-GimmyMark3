package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.LimelightHelper;

import java.util.List;

public class Turret {

    public DcMotorEx Turret;
    public static Turret instance;
    private double kP = 0.0001;
    private double kD = 0.0;
    private double goalX = 0.0;
    private double latestError = 0.0;
    private double toleranceForAngle = 0.5;
    private final double MAX_POWER = 0.7;
    private double power = 0.0;

    private final ElapsedTime timer = new ElapsedTime();



    public Turret(HardwareMap hardwareMap) {

        Turret = hardwareMap.get(DcMotorEx.class, "Turret");
        Turret.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        instance = this;
    }

    public void resetTimer() {
        timer.reset();
    }

    public void update(LimelightHelper limelight) {
        LLResult result = limelight.getLatestResult();
        List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
        for (LLResultTypes.FiducialResult fiducial : fiducials) {
            int id = fiducial.getFiducialId();


            double time = timer.seconds();
            timer.reset();

            if (result == null) {
                Turret.setPower(0);
                latestError = 0;
                return;
            }

            //start PD controller
            double error = goalX - fiducial.getTargetXDegrees();
            double pTerm = error * kP;
            double dTerm = 0;
            if (time > 0) {
                dTerm = ((error - latestError) / time) * kD;
            }

        }
    }


    }
