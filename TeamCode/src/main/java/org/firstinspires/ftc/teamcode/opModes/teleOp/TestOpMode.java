package org.firstinspires.ftc.teamcode.opModes.teleOp;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.dairyFarm.subsytems.Intake;
import org.firstinspires.ftc.teamcode.dairyFarm.subsytems.Testing;

import dev.frozenmilk.dairy.core.FeatureRegistrar;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.Command;

@TeleOp(name= "TESTING", group="Testing OpModes")
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


        Mercurial.gamepad1().options().onTrue(
                Intake.INSTANCE.toggleIntake()
        );

        Mercurial.gamepad1().dpadUp().onTrue(
                Testing.INSTANCE.incrementMotor()
        );

        Mercurial.gamepad1().dpadDown().onTrue(
                Testing.INSTANCE.incrementMotorDown()
        );

        Mercurial.gamepad1().dpadRight().onTrue(
                Testing.INSTANCE.incrementServo()
        );

        Mercurial.gamepad1().dpadLeft().onTrue(
                Testing.INSTANCE.incrementServoBack()
        );
    }

    @Override
    public void loop() {
        telemetry.update();
    }
}
