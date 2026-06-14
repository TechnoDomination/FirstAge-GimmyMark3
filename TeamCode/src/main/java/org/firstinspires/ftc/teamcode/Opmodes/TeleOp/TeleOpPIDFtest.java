package org.firstinspires.ftc.teamcode.Opmodes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.LimelightHelper;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.ShooterHood;
import org.firstinspires.ftc.teamcode.Subsystems.TurretGate;
import org.firstinspires.ftc.teamcode.Util.AllianceManager;

@TeleOp
public class TeleOpPIDFtest extends LinearOpMode {

    public DcMotorEx ShooterMotorLeft;
    public DcMotorEx ShooterMotorRight;
    double currentLeftVel;
    double currentRightVel;
    double maxRightVel =0.0;
    double maxLeftVel =0.0;
    ElapsedTime timer = new ElapsedTime();
    @Override
    public void runOpMode() {

        ShooterMotorLeft = hardwareMap.get(DcMotorEx.class, "ShooterMotorLeft");
        ShooterMotorRight = hardwareMap.get(DcMotorEx.class, "ShooterMotorRight");
        ShooterMotorLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        ShooterMotorRight.setDirection(DcMotorSimple.Direction.FORWARD);
        ShooterMotorLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        ShooterMotorRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        timer.reset();
        waitForStart();
        while (opModeIsActive() && !isStopRequested()) {


            if (timer.time() < 5) {
                ShooterMotorLeft.setPower(1);
                ShooterMotorRight.setPower(1);

                currentLeftVel = ShooterMotorLeft.getVelocity();
                currentRightVel = ShooterMotorRight.getVelocity();

                if (currentLeftVel > maxLeftVel) {
                    maxLeftVel = currentLeftVel;
                }
                if (currentRightVel > maxRightVel) {
                    maxRightVel = currentRightVel;
                }
            }
            else {



                telemetry.addData("current left Vel: ", currentLeftVel);
                telemetry.addData("current right Vel: ", currentRightVel);
                telemetry.addData("Max left Vel: ", maxLeftVel);
                telemetry.addData("Max right Vel: ", maxRightVel);
                telemetry.addData("P value for left: ", 0.1 * (32767 / maxLeftVel));
                telemetry.addData("F value for left: ", 32767 / maxLeftVel);
                telemetry.addData("P value for right: ", 0.1 * (32767 / maxRightVel));
                telemetry.addData("F value for right: ", 32767 / maxRightVel);
                telemetry.addData("Voltage Sensor: ", hardwareMap.voltageSensor.iterator().next().getVoltage());
                telemetry.update();
                ShooterMotorLeft.setPower(0);
                ShooterMotorRight.setPower(0);

            }
        }
    }
}
