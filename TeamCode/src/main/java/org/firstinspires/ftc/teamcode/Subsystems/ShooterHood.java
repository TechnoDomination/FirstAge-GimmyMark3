package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ShooterHood {
    private final Servo ShooterHood;
    public State state = State.DOWN;
    public boolean isTargetReached = false;
    public static ShooterHood instance;
    private final double closePos = 0.3;
    private final double upPos = closePos+0.4;
    private final double closePosAuto = closePos;
    private final double middlePos = (closePos+upPos)/2;
    private final double farPos = closePos+0.4; //0.7
    private final double downPos = closePos;
    private final double farBlue = closePos+0.4;

    public enum State {
        UP,
        DOWN,
        MIDDLE,
        REST,
        CLOSE,
        FAR,
        AUTOCLOSE,
        AUTOFARBLUE
    }

    public ShooterHood(HardwareMap hardwareMap) {
        ShooterHood = hardwareMap.get(Servo.class, "ShooterHood");

        instance = this;
    }
    public void update() {
        switch (state) {
            case DOWN:
                ShooterHood.setPosition(downPos);
                break;
            case UP:
                ShooterHood.setPosition(upPos);
                break;
            case MIDDLE:
                ShooterHood.setPosition(middlePos);
                break;
            case CLOSE:
                ShooterHood.setPosition(closePos);
                break;
            case FAR:
                ShooterHood.setPosition(farPos);
                break;
            case REST:
                ShooterHood.setPosition(downPos);
                break;
            case AUTOCLOSE:
                ShooterHood.setPosition(closePosAuto);
                break;
            case AUTOFARBLUE:
                ShooterHood.setPosition(farBlue);
                break;
        }

        if (state == State.UP && ShooterHood.getPosition() == upPos) {
            isTargetReached = true;
        } else if (state == State.DOWN && ShooterHood.getPosition() == middlePos) {
            isTargetReached = true;
        } else if (state == State.REST && ShooterHood.getPosition() == downPos) {
            isTargetReached = true;
        } else {
            isTargetReached = false;
        }
    }

    public String getShooterHoodTelemetry() {
        String telemetry = "";
        telemetry = telemetry + "\n Shhoter Hood Pos = " + ShooterHood.getPosition();
        telemetry = telemetry + "\n ";
        return telemetry;
    }

}
