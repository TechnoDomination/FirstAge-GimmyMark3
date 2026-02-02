package org.firstinspires.ftc.teamcode.Actions;
import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.GoBildaPinPointOdo.Angle;
import org.firstinspires.ftc.teamcode.GoBildaPinPointOdo.Localizer;
import org.firstinspires.ftc.teamcode.GoBildaPinPointOdo.Poses;
import org.firstinspires.ftc.teamcode.Subsystems.Drive;

//RR - Actions
public class P2P implements Action {
    Vector2d targetVector;
    double rotation;
    Poses robotPosition;
    public boolean isTargetReached;
    public ElapsedTime timer = new ElapsedTime();
    public boolean checkTimer = false;

    public P2P(Vector2d vector2d, double rotation2d) {
        this.targetVector = vector2d;
        this.rotation = rotation2d;
    }


    @Override
    public boolean run(@NonNull TelemetryPacket telemetryPacket) {

        robotPosition = Localizer.pose;
        Drive motorController = Drive.instance;

        double latError = targetVector.y - robotPosition.getY();
        double axialError = targetVector.x - robotPosition.getX();
        double headingError = Angle.INSTANCE.wrap(rotation + robotPosition.getHeading());

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
        isTargetReached = true;
       /* if ((Math.abs(latError) > 2 || Math.abs(axialError) > 2) && timer.seconds() > 5 && checkTimer) {
            isTargetReached = false;
            checkTimer = false;

            return isTargetReached;
        }*/

        //checking if it has reached the point



        return !(
                Math.abs(latError) < 2 && Math.abs(axialError) < 2 && Math.abs(headingError) < Math.toRadians(5.0)
        );
    }
}