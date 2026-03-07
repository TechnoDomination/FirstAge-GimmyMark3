package org.firstinspires.ftc.teamcode.Actions;
import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Subsystems.Drive;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.Subsystems.ShooterHood;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.util.ElapsedTime;

public class CustomActions {
    Shooter shooter = Shooter.instance;
    Intake intake = Intake.instance;
    ShooterHood shooterHood = ShooterHood.instance;
    public Drive drive = Drive.instance;
    public static CustomActions instance;
    public ElapsedTime runTime = new ElapsedTime();
    boolean timerStarted;
    boolean reset = timerStarted;


    P2P p2p = new P2P(new Vector2d(0,0), 0);

    public CustomActions(HardwareMap hardwareMap) {
        instance = this;
    }

    public void update() {


        shooter.update();
        intake.update();
        shooterHood.update();

    }
    public Action stopDrive = new Action() {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {

            drive.stopDrive();

            return false;

        }
    };
    public Action shootFrontRed = new Action() {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            shooter.state = Shooter.State.AUTOCLOSERED;
            shooterHood.state = ShooterHood.State.AUTOCLOSE;

            return !shooter.isTargetReached;
        }
    };

    public Action intakeForward = new Action() {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            intake.state = Intake.State.FORWARD;
            return !intake.isTargetReached;
        }
    };





}
