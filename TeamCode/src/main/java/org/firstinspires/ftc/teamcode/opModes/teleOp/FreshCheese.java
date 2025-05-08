package org.firstinspires.ftc.teamcode.opModes.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.camembert.cheeseFactory.Globals;
import org.firstinspires.ftc.teamcode.camembert.cheeseFactory.RobotState;
import org.firstinspires.ftc.teamcode.dairyFarm.subsytems.Arm;
import org.firstinspires.ftc.teamcode.dairyFarm.subsytems.Drivetrain;
import org.firstinspires.ftc.teamcode.dairyFarm.subsytems.Flag;
import org.firstinspires.ftc.teamcode.dairyFarm.subsytems.Lift;
import org.firstinspires.ftc.teamcode.dairyFarm.subsytems.SampleManipulator;
import org.firstinspires.ftc.teamcode.dairyFarm.subsytems.SampleManipulator2;
import org.firstinspires.ftc.teamcode.dairyFarm.subsytems.Wrist;

import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.groups.Parallel;

@TeleOp(name="FreshCheese")

@Globals.Attach
@Drivetrain.Attach
@SampleManipulator2.Attach

public class FreshCheese extends OpMode{
    public RobotState currentRobotState = RobotState.IDLE;

    @Override
    public void init() {
        Globals.INSTANCE.setRobotState(RobotState.IDLE);

        Mercurial.gamepad1().circle().onTrue(
        SampleManipulator2.INSTANCE.toggleClaw()
        );

        telemetry.update();
    }

    @Override
    public void loop() {

    }
}
