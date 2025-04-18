package org.firstinspires.ftc.teamcode.opModes.teleOp;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.robot.Robot;

import dev.frozenmilk.dairy.core.util.OpModeLazyCell;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.Command;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.commands.groups.Parallel;
import dev.frozenmilk.mercurial.commands.groups.Sequential;

import org.firstinspires.ftc.teamcode.camembert.cheeseFactory.Globals;
import org.firstinspires.ftc.teamcode.camembert.cheeseFactory.RobotState;
import org.firstinspires.ftc.teamcode.dairyFarm.subsytems.Arm;
import org.firstinspires.ftc.teamcode.dairyFarm.subsytems.Drivetrain;
import org.firstinspires.ftc.teamcode.dairyFarm.subsytems.Flag;
import org.firstinspires.ftc.teamcode.dairyFarm.subsytems.Lift;
import org.firstinspires.ftc.teamcode.dairyFarm.subsytems.SampleManipulator;
import org.firstinspires.ftc.teamcode.dairyFarm.subsytems.Wrist;

// Sets this class as a TeleOp
@TeleOp(name="NormalTeleOp")
// Enables the scheduler
@Mercurial.Attach
// Add additional Subsystem attaches
@Globals.Attach
@Drivetrain.Attach
@Flag.Attach
@Lift.Attach
@Wrist.Attach
@Arm.Attach
@SampleManipulator.Attach
//Attach Pitching Later

public class TeleOpTest extends OpMode {
    public RobotState currentRobotState = RobotState.IDLE;

    @Override
    public void init() {
        Globals.INSTANCE.setRobotState(RobotState.IDLE);
        // Modify gamepads

        // Apply bindings

        Mercurial.gamepad1().rightBumper().onTrue(
                new Sequential(
                //Update Status, then
                Globals.INSTANCE.forwardsRobotState(),
                //Update Subsystems
                new Parallel(
                    SampleManipulator.INSTANCE.openCloseClaw(),
                    Arm.INSTANCE.turnArm(),
                    Lift.INSTANCE.goToPosition(),
                    Wrist.INSTANCE.turnWrist()
                ))
        );
        Mercurial.gamepad1().leftBumper().onTrue(
                new Sequential(
                //Update subsystems
                Globals.INSTANCE.backwardsRobotState(),
                new Parallel(
                        SampleManipulator.INSTANCE.openCloseClaw(),
                        SampleManipulator.INSTANCE.resetClawRotAutomatically(),
                        Arm.INSTANCE.turnArm(),
                        Lift.INSTANCE.goToPosition(),
                        Wrist.INSTANCE.turnWrist()
                ))
        );

        Mercurial.gamepad1().circle().onTrue(
          SampleManipulator.INSTANCE.rotateClawForwards()
        );

        Mercurial.gamepad1().square().onTrue(
          SampleManipulator.INSTANCE.rotateClawBackwards()
        );

        Mercurial.gamepad1().options().onTrue(
                SampleManipulator.INSTANCE.toggleClaw()
        );

        Mercurial.gamepad1().dpadLeft().onTrue(
                new Parallel(
                        Globals.INSTANCE.goToIdle(),
                        Globals.INSTANCE.changeState()
                ));



        Mercurial.gamepad2().dpadDown().onTrue(
                Wrist.INSTANCE.adjustWristDown()
        );
        Mercurial.gamepad2().dpadUp().onTrue(
                Wrist.INSTANCE.adjustWristUp()
        );

        Mercurial.gamepad2().triangle().onTrue(
                Arm.INSTANCE.adjustArmUp()
        );

        Mercurial.gamepad2().cross().onTrue(
                Arm.INSTANCE.adjustArmDown()
        );

        telemetry.update();
    }

    @Override
    public void loop() {

    }

}
