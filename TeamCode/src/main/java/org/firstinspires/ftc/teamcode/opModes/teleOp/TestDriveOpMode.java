package org.firstinspires.ftc.teamcode.opModes.teleOp;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.camembert.cheeseFactory.Globals;
import org.firstinspires.ftc.teamcode.camembert.cheeseFactory.RobotState;
import org.firstinspires.ftc.teamcode.dairyFarm.subsytems.Arm;
import org.firstinspires.ftc.teamcode.dairyFarm.subsytems.Drivetrain;
import org.firstinspires.ftc.teamcode.dairyFarm.subsytems.Intake;
import org.firstinspires.ftc.teamcode.dairyFarm.subsytems.Lift;
import org.firstinspires.ftc.teamcode.dairyFarm.subsytems.Pitching;

import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.groups.Parallel;

@TeleOp(name="TestDriveOpMode", group="Testing OpModes")
@Config

@Mercurial.Attach

@Globals.Attach
@Drivetrain.Attach

public class TestDriveOpMode extends OpMode{
    public RobotState currentRobotState = RobotState.IDLE;

    @Override
    public void init() {
        Globals.INSTANCE.setRobotState(RobotState.IDLE);

        telemetry.update();
    }

    @Override
    public void loop() {

    }
}
