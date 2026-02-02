package org.firstinspires.ftc.teamcode.Util;

public class AllianceManager {
    public static boolean isRedAlliance  = false;
    public static boolean isBlueAlliance  = false;

    public boolean redAlliance() {
        isRedAlliance = true;
        return isRedAlliance;
    }

    public boolean blueAlliance() {
        isBlueAlliance = true;
        return isBlueAlliance;
    }
    public boolean offBlueAlliance() {
        isBlueAlliance = false;
        return isBlueAlliance;
    }
    public boolean offRedAlliance() {
        isRedAlliance = false;
        return isRedAlliance;
    }

    public String getAllianceManagerTelemetry(){
        String telemetry = "";
        if (isBlueAlliance){
            telemetry = telemetry + "\n Blue Alliance";
            telemetry = telemetry + "\n ";
        }
        else if (isRedAlliance){
            telemetry = telemetry + "\n Red Alliance";
            telemetry = telemetry + "\n ";
        }
        else {
            telemetry = telemetry + "\n No Alliance";
            telemetry = telemetry + "\n ";
        }

        return telemetry;
    }

}
