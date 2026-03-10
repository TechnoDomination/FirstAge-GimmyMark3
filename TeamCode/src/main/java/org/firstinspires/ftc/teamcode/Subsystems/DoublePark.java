package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class DoublePark {
    private final Servo DoubleParkLeft;
    //private final Servo DoubleParkRight;
    public boolean isTargetReached = false;
    public State state = State.REST;
    public static DoublePark instance;
    private final double rest = 0.0;
    private final double park = 1;
    private final double in = 0;

    public enum State {
        PARK,
        IN,
        REST
    }

    public DoublePark(HardwareMap hardwareMap) {
        DoubleParkLeft = hardwareMap.get(Servo.class, "doubleParkLeft");
        //DoubleParkRight = hardwareMap.get(Servo.class, "doubleParkRight");

        instance = this;
    }

    public void update() {
        switch (state) {
            case PARK:
                DoubleParkLeft.setPosition(park);
               // DoubleParkRight.setPosition(park);
                break;
            case IN:
                DoubleParkLeft.setPosition(in);
               // DoubleParkRight.setPosition(in);
                break;
            case REST:
                DoubleParkLeft.setPosition(rest);
              //  DoubleParkRight.setPosition(rest);
                break;
        }

        if (state == State.PARK && DoubleParkLeft.getPosition() == park) {
            isTargetReached = true;
        } else if (state == State.IN && DoubleParkLeft.getPosition() == in) {
            isTargetReached = true;
        } else if (state == State.REST && DoubleParkLeft.getPosition() == rest) {
            isTargetReached = true;
        } else {
            isTargetReached = false;
        }
    }

    public String getShooterHoodTelemetry() {
        String telemetry = "";
        telemetry = telemetry + "\n Stopper for turret pos = " + DoubleParkLeft.getPosition();
        telemetry = telemetry + "\n ";
        return telemetry;
    }
}
