package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Turret {

    public DcMotorEx Turret;
    public static Turret instance;

    public Turret(HardwareMap hardwareMap) {

        Turret = hardwareMap.get(DcMotorEx.class, "Turret");
        Turret.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        Turret.setDirection(DcMotorSimple.Direction.REVERSE);

        Turret.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        Turret.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        Turret.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        instance = this;
    }

    }
