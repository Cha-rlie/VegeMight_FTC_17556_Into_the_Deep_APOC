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

        Mercurial.gamepad1().rightBumper().onTrue();

        Mercurial.gamepad1().leftBumper().onTrue();

        telemetry.update();
    }

    @Override
    public void loop() {

    }

    public RobotState getCurrentRobotState() {
        return currentRobotState;
    }

}
