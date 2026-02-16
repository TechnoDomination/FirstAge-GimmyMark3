package org.firstinspires.ftc.teamcode.Actions;
import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Subsystems.Drive;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.util.ElapsedTime;

public class CustomActions {
    public Drive drive = Drive.instance;
    public static CustomActions instance;


    P2P p2p = new P2P(new Vector2d(0,0), 0);

    public CustomActions(HardwareMap hardwareMap) {
        instance = this;
    }

    public Action stopDrive = new Action() {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {

            drive.stopDrive();

            return false;

        }
    };





}
