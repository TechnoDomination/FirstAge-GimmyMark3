package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Util.AllianceManager;

import java.util.List;

public class LimelightHelper {
    private Limelight3A limelight;
    public double MIN_TX = 10.0;
    public double MAX_TX = 13.0;
    public double MIN_TY = 10.0;
    public double MAX_TY = 14.0;
    public boolean side;


    public LimelightHelper(HardwareMap hardwareMap) {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100);
        limelight.pipelineSwitch(0);
        limelight.start();
    }

    public boolean setAlliance(boolean alliance) {
       side = alliance;
       return side;
    }

    public LLResult getLatestResult() {
        return limelight.getLatestResult();
    }


    public boolean isReadyToShoot() {
        LLResult result = getLatestResult();
                if (result != null && result.isValid()) {
                    return ((Math.abs(result.getTx()) > MIN_TX && Math.abs(result.getTx()) < MAX_TX) && (Math.abs(result.getTy()) > MIN_TY && Math.abs(result.getTy()) < MAX_TY));
                }

        return false;
    }


    public void stopLimelight() {
        limelight.stop();
    }

    public double getDistance() {
        LLResult result = getLatestResult();
        List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
        for (LLResultTypes.FiducialResult fiducial : fiducials) {
            int id = fiducial.getFiducialId();
            if (AllianceManager.isBlueAlliance) {
            if (result != null && result.isValid() && id == 20) {
                double ty = result.getTy();
                double angleToGoalDegrees = 27 + ty;
                double angleToGoalRadians = angleToGoalDegrees * (3.14159 / 180);
                //26 is the goal height - limelight lens height
                return 26 / Math.tan(angleToGoalRadians);
            }
            } else if (AllianceManager.isRedAlliance) {
                if (result != null && result.isValid() && id == 24) {
                    double ty = result.getTy();
                    double angleToGoalDegrees = 27 + ty;
                    double angleToGoalRadians = angleToGoalDegrees * (3.14159 / 180);
                    //26 is the goal height - limelight lens height
                    return 26 / Math.tan(angleToGoalRadians);
                }
            }
        }
        return -1;
    }

    public double getGoalHeading() {
        LLResult result = getLatestResult();
        List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
        for (LLResultTypes.FiducialResult fiducial : fiducials) {
            int id = fiducial.getFiducialId();
            if (AllianceManager.isBlueAlliance) {
                if (result != null && result.isValid() && id == 20) {
                    return fiducial.getTargetPoseRobotSpace().getOrientation().getYaw();
                }
            } else if (AllianceManager.isRedAlliance) {
                if (result != null && result.isValid() && id == 24) {
                    return fiducial.getTargetPoseRobotSpace().getOrientation().getYaw();
                }
            }
        }
        return -1;
    }

    public double getGoalHeading_NotUsed(){
        LLResult result = getLatestResult();
        return result.getBotpose_MT2().getOrientation().getYaw();
    }


    public String getLimelightTelemetry() {
        LLResult result = getLatestResult();
        String telemetry = "";
        List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
        for (LLResultTypes.FiducialResult fiducial : fiducials) {
            int id = fiducial.getFiducialId();
            double y = fiducial.getTargetYDegrees();
            double x = fiducial.getTargetXDegrees();
            telemetry = telemetry + id + " at x: " + x + ", " + y + " degrees";
            telemetry = telemetry + "Goal 2 " + fiducial.getTargetPoseRobotSpace().getOrientation().getYaw();
        }
        //telemetry = telemetry + "\n Tx: " + result.getTx();
         //telemetry = telemetry + "\n Ty: " + result.getTy();
        telemetry = telemetry + "\n distance From Goal: " + getDistance();
        telemetry = telemetry + "\n Goal Heading: " + getGoalHeading();

        return telemetry;
    }
}
