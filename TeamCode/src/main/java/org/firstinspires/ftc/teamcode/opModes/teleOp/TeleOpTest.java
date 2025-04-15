package org.firstinspires.ftc.teamcode.opModes.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.camembert.cheeseFactory.RobotState;
import org.firstinspires.ftc.teamcode.dairyFarm.subsytems.Drivetrain;

import dev.frozenmilk.dairy.core.util.OpModeLazyCell;
import dev.frozenmilk.mercurial.Mercurial;

// Sets this class as a TeleOp
@TeleOp(name="PLEASE WORK")
// Enables the scheduler
@Mercurial.Attach
// Add additional Subsystem attaches
@Drivetrain.Attach
public class TeleOpTest extends OpMode {

    public OpModeLazyCell<RobotState> robotState = new OpModeLazyCell<>(() -> RobotState.IDLE);
    public RobotState currentRobotState = RobotState.IDLE;

    @Override
    public void init() {
        robotState.accept(RobotState.IDLE);
        // Modify gamepads

        // Apply bindings
        telemetry.update();
    }

    @Override
    public void loop() {

    }

    public RobotState getCurrentRobotState() {
        return currentRobotState;
    }

}
