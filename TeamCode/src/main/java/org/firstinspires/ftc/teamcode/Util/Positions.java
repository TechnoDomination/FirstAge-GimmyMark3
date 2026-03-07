
package org.firstinspires.ftc.teamcode.Util;

import static java.lang.Math.PI;

import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.Actions.P2P;
import org.firstinspires.ftc.teamcode.Actions.SetDriveTimer;
import org.firstinspires.ftc.teamcode.GoBildaPinPointOdo.Poses;


public enum Positions {

    TestDrive(new Vector2d(0,10),0.0),
    ShootingPositionsRed(new Vector2d(20, 17), PI*.22),
    RedIntakeTape1Start(new Vector2d(90,15), PI*0.5, 3.0, 0.2);

    public double x,y, heading;
    public double driveSpeed;
    public double maxTime;
    //public final SetDriveTimer runToExact;


    Positions(Vector2d vector, Double rotation){
        this.x = vector.x;
        this.y = vector.y;
        this.heading = rotation;
        //runToExact = new SetDriveTimer(vector, rotation);
    }

    Positions(Vector2d vector, Double rotation, Double driveSpeed){
        this.x = vector.x;
        this.y = vector.y;
        this.heading = rotation;
        this.driveSpeed = driveSpeed;
        //runToExact = new SetDriveTimer(vector, rotation);
    }


    Positions(Vector2d vector, Double rotation, Double maxTime, Double driveSpeed){
        this.x = vector.x;
        this.y = vector.y;
        this.heading = rotation;
        this.driveSpeed = driveSpeed;
        this.maxTime = maxTime;
        //runToExact = new SetDriveTimer(vector, rotation); //maxtime

    }


    //public final SetDriveTimer runToExact;

    public SetDriveTimer runToExact() {
        return new SetDriveTimer(new Poses(this.x, this.y, this.heading), this.driveSpeed, this.maxTime);
    }

}

