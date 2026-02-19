package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class TurretGate {
    private final Servo TurretGate;
    public boolean isTargetReached = false;
    public State state = State.REST;
    public static TurretGate instance;
    private final double rest = 0.0;
    private final double close = 0.0;
    private final double open = 1;

    public enum State {
        OPEN,
        CLOSE,
        REST
    }

    public TurretGate(HardwareMap hardwareMap) {
        TurretGate = hardwareMap.get(Servo.class, "ShooterHood");

        instance = this;
    }

    public void update() {
        switch (state) {
            case OPEN:
                TurretGate.setPosition(open);
                break;
            case CLOSE:
                TurretGate.setPosition(close);
                break;
            case REST:
                TurretGate.setPosition(rest);
                break;
        }

        if (state == State.OPEN && TurretGate.getPosition() == open) {
            isTargetReached = true;
        } else if (state == State.CLOSE && TurretGate.getPosition() == close) {
            isTargetReached = true;
        } else if (state == State.REST && TurretGate.getPosition() == rest) {
            isTargetReached = true;
        } else {
            isTargetReached = false;
        }
    }

    public String getShooterHoodTelemetry() {
        String telemetry = "";
        telemetry = telemetry + "\n Stopper for turret pos = " + TurretGate.getPosition();
        telemetry = telemetry + "\n ";
        return telemetry;
    }
}
