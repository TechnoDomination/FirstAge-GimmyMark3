package org.firstinspires.ftc.teamcode.Opmodes.Auto;

import static java.lang.Math.PI;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Actions.CustomActions;
import org.firstinspires.ftc.teamcode.GoBildaPinPointOdo.Localizer;
import org.firstinspires.ftc.teamcode.GoBildaPinPointOdo.Poses;
import org.firstinspires.ftc.teamcode.Subsystems.Drive;
import org.firstinspires.ftc.teamcode.Util.Positions;

@Autonomous(name = "AutoTest", group = "Autonomous")
public class AutoTest extends LinearOpMode {



    @Override
    public void runOpMode() throws InterruptedException {

        Drive drive = new Drive(hardwareMap);
        Localizer localizer = new Localizer(hardwareMap, new Poses(0, 0, PI*0.0));
        CustomActions customActions = new CustomActions(hardwareMap);

        waitForStart();

            Actions.runBlocking(
                    new ParallelAction(
                            telemetryPacket -> {
                                localizer.update();



                                telemetry.addData("X pos", Localizer.pose.getX());
                                telemetry.addData("Y pos", Localizer.pose.getY());
                                telemetry.addData("Heading pos",- Localizer.pose.getHeading());
                                //for(String string: customActions.getTelemetry()) telemetry.addLine(string);
                                telemetry.update();

                                return true;
                            },

                            new SequentialAction(
                                    Positions.TestDrive.runToExact,
                                    customActions.stopDrive

                            )
                    )
            );

    }
}
