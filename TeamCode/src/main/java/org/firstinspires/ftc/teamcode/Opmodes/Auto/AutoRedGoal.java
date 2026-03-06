package org.firstinspires.ftc.teamcode.Opmodes.Auto;

import static java.lang.Math.PI;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Actions.CustomActions;
import org.firstinspires.ftc.teamcode.Actions.SharedPose;
import org.firstinspires.ftc.teamcode.GoBildaPinPointOdo.Localizer;
import org.firstinspires.ftc.teamcode.GoBildaPinPointOdo.Poses;
//import org.firstinspires.ftc.teamcode.LimelightHelper;
import org.firstinspires.ftc.teamcode.Subsystems.Drive;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.Subsystems.ShooterHood;
import org.firstinspires.ftc.teamcode.Util.AllianceManager;
import org.firstinspires.ftc.teamcode.Util.Positions;

@Autonomous(name = "AutoRedGoal", group = "Autonomous")
public class AutoRedGoal extends LinearOpMode {
    Double hopperUpTime = 0.5;
    Double hopperDownTime = 1.0;
    Double driveTime = 1.0;

    @Override
    public void runOpMode() {

        Localizer localizer = new Localizer(hardwareMap, new Poses(45, 55, PI * 0.0));
        Drive drive = new Drive(hardwareMap);
        Shooter shooter = new Shooter(hardwareMap);
        Intake intake = new Intake(hardwareMap);
        ShooterHood shooterHood = new ShooterHood(hardwareMap);
        CustomActions customActions = new CustomActions(hardwareMap);
        //LimelightHelper limelightHelper = new LimelightHelper(hardwareMap);
        //limelightHelper.setAlliance(true);
        AllianceManager alliance = new AllianceManager();
        SharedPose sharedPose = new SharedPose();
        // AutoGlobals.messageFromAuto = "Info moved :)";


        //customActions.update();

        waitForStart();

        Actions.runBlocking(
                new ParallelAction(
                        telemetryPacket -> {
                            localizer.update();
                            customActions.update();
                            alliance.redAlliance();
                            alliance.offBlueAlliance();
                            SharedPose.runToExactAlways(SharedPose.targetPose);
                            //SharedPose.robotPosition = Poses(Localizer.pose.x)


                            telemetry.addData("X pos", Localizer.pose.getX());
                            telemetry.addData("Y pos", Localizer.pose.getY());
                            telemetry.addData("Heading pos", -Localizer.pose.getHeading());
                            //for(String string: customActions.getTelemetry()) telemetry.addLine(string);
                            telemetry.update();

                            return true;
                        },

                        new SequentialAction(
                                customActions.shootFrontRed,
                                new SleepAction(driveTime),
                                Positions.ShootingPositionsRed.runToExact(),
                                customActions.stopDrive
                        )
                )
        );
    }
}
