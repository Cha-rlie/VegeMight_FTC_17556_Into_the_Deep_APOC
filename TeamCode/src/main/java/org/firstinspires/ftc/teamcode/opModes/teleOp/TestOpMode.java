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
    @Override
    public void init() {
        Mercurial.gamepad1().triangle().onTrue(
                Intake.INSTANCE.adjustClawForwards()
        );

        Mercurial.gamepad1().cross().onTrue(
                Intake.INSTANCE.adjustClawBackwards()
        );

        Mercurial.gamepad1().options().onTrue(
                Intake.INSTANCE.toggleIntake()
        );

        Mercurial.gamepad1().ps().onTrue(
                Testing.INSTANCE.saveState()
        );

        Mercurial.gamepad1().rightBumper().onTrue(
                Testing.INSTANCE.togglePos()
        );

        telemetry.update();
    }

    @Override
    public void loop() {
        telemetry.update();
    }
}
