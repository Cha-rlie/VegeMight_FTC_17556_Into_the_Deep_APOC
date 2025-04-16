package org.firstinspires.ftc.teamcode.opModes.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.robot.Robot;

import org.firstinspires.ftc.teamcode.camembert.cheeseFactory.RobotState;
import org.firstinspires.ftc.teamcode.dairyFarm.subsytems.Arm;
import org.firstinspires.ftc.teamcode.dairyFarm.subsytems.Drivetrain;
import org.firstinspires.ftc.teamcode.dairyFarm.subsytems.Flag;
import org.firstinspires.ftc.teamcode.dairyFarm.subsytems.Lift;
import org.firstinspires.ftc.teamcode.dairyFarm.subsytems.SampleManipulator;
import org.firstinspires.ftc.teamcode.dairyFarm.subsytems.Wrist;

import dev.frozenmilk.dairy.core.util.OpModeLazyCell;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.Command;
import dev.frozenmilk.mercurial.commands.groups.Parallel;
import dev.frozenmilk.mercurial.commands.groups.Sequential;

// Sets this class as a TeleOp
@TeleOp(name="NormalTeleOp")
// Enables the scheduler
@Mercurial.Attach
// Add additional Subsystem attaches
@Drivetrain.Attach
@Flag.Attach
@Lift.Attach
@Wrist.Attach
@Arm.Attach
@SampleManipulator.Attach

public class TeleOpTest extends OpMode {

    public OpModeLazyCell<RobotState> robotState = new OpModeLazyCell<>(() -> RobotState.IDLE);
    public RobotState currentRobotState = RobotState.IDLE;

    @Override
    public void init() {
        robotState.accept(RobotState.IDLE);
        // Modify gamepads

        // Apply bindings

        Mercurial.gamepad1().rightBumper().onTrue(
                new Sequential(
                //Update Status, then
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
                //Update status
                //Update subsystems
                new Parallel(
                        SampleManipulator.INSTANCE.openCloseClaw(),
                        Arm.INSTANCE.turnArm(),
                        Lift.INSTANCE.goToPosition(),
                        Wrist.INSTANCE.turnWrist()
                ))
        );

        Mercurial.gamepad1().options().onTrue(
                SampleManipulator.INSTANCE.toggleClaw()
        );

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

    public RobotState getCurrentRobotState() {
        return currentRobotState;
    }

}
