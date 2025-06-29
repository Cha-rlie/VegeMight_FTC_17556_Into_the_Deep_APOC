package org.firstinspires.ftc.teamcode.opModes.teleOp;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.dairyFarm.subsytems.Intake;
import org.firstinspires.ftc.teamcode.dairyFarm.subsytems.Testing;

import dev.frozenmilk.mercurial.Mercurial;

@TeleOp(name= "TESTING")
@Config
@Mercurial.Attach
@Intake.Attach
@Testing.Attach


public class TestOpMode extends OpMode {

    public static double servoPos = 0.00;
    public static int motorRTP = 100;

    @Override
    public void init() {
        Mercurial.gamepad1().triangle().onTrue(
                Testing.INSTANCE.adjustMotor(motorRTP)
        );

        Mercurial.gamepad1().cross().onTrue(
                Testing.INSTANCE.adjustServo(servoPos)
        );

        Mercurial.gamepad1().square().onTrue(
                Testing.INSTANCE.toggleMotor()
        );

        Mercurial.gamepad1().dpadUp().onTrue(
                
        );

        Mercurial.gamepad1().options().onTrue(
                Intake.INSTANCE.toggleIntake()
        );
    }

    @Override
    public void loop() {
        telemetry.update();
    }
}
