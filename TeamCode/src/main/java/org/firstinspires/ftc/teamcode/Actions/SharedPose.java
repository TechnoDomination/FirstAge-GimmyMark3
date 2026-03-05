package org.firstinspires.ftc.teamcode.Actions;

import static org.firstinspires.ftc.teamcode.GoBildaPinPointOdo.Localizer.pose;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.GoBildaPinPointOdo.Angle;
import org.firstinspires.ftc.teamcode.GoBildaPinPointOdo.Localizer;
import org.firstinspires.ftc.teamcode.GoBildaPinPointOdo.Poses;
import org.firstinspires.ftc.teamcode.Subsystems.Drive;

public class SharedPose {
    public static Poses targetPose = new Poses(0,0,0);
    public static Poses robotPosition = null;
    public static void setFinalPose(){
        //robotPosition = new Poses(pose.getX(), pose.getY(), pose.getHeading());
        robotPosition = pose;
    }


    public static boolean runToExactAlways (Poses poses) {;
        robotPosition = Localizer.pose;
        Drive motorController = Drive.instance;
        ElapsedTime timer = new ElapsedTime();

        double latError = poses.getY() - robotPosition.getY();
        double axialError = poses.getX() - robotPosition.getX();
        double headingError = Angle.INSTANCE.wrap(poses.getHeading() + robotPosition.getHeading());

        double lateral = motorController.yPid.calculate(latError);
        double axial = motorController.xPid.calculate(axialError);
        double turn = motorController.rPid.calculate(headingError);


        //field oriented drive
        double h = -robotPosition.getHeading();
        double rotX = axial * Math.cos(h) - lateral * Math.sin(h);
        double rotY = axial * Math.sin(h) + lateral * Math.cos(h);

        motorController.FrontLeftDCMotor.setPower(rotY + rotX + turn);
        motorController.BackLeftDCMotor.setPower(rotY - rotX + turn);
        motorController.FrontRightDCMotor.setPower(rotY - rotX - turn);
        motorController.BackRightDCMotor.setPower(rotY + rotX - turn);
       /* if ((Math.abs(latError) > 2 || Math.abs(axialError) > 2) && timer.seconds() > 5 && checkTimer) {
            isTargetReached = false;
            checkTimer = false;*/

        double xError = Localizer.pose.getX() - pose.getX();
        double yError = Localizer.pose.getY() - pose.getY();
        double angleError = Angle.INSTANCE.wrap(
                -Localizer.pose.getHeading() + pose.getHeading()
        );

        return true;
    }
}
