package org.firstinspires.ftc.teamcode.opModes.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.dairyFarm.subsytems.Intake;

import dev.frozenmilk.mercurial.Mercurial;

@TeleOp(name= "TESTING")
@Mercurial.Attach
@Intake.Attach

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
        telemetry.update();
    }

    @Override
    public void loop() {
        telemetry.update();
    }
}
