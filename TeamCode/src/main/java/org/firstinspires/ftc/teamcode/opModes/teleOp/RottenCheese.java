package org.firstinspires.ftc.teamcode.opModes.teleOp;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.groups.Parallel;

import org.firstinspires.ftc.teamcode.camembert.cheeseFactory.Globals;
import org.firstinspires.ftc.teamcode.camembert.cheeseFactory.RobotState;
import org.firstinspires.ftc.teamcode.dairyFarm.subsytems.Arm;
import org.firstinspires.ftc.teamcode.dairyFarm.subsytems.OLDINTAKE;

// Sets this class as a TeleOp
@TeleOp(name="RottenCheese")
@Config
// Enables the scheduler
@Mercurial.Attach
// Add additional Subsystem attaches
/*@Globals.Attach
@Drivetrain.Attach
@Flag.Attach
@Lift.Attach
@Wrist.Attach
@Arm.Attach
@OLDINTAKE.Attach */
@Arm.Attach
//Attach Pitching Later

public class RottenCheese extends OpMode {
    public RobotState currentRobotState = RobotState.IDLE;

    @Override
    public void init() {
        Globals.INSTANCE.setRobotState(RobotState.IDLE);
        // Modify gamepads

        // Apply bindings

        //State Changing Gamebinds
        Mercurial.gamepad1().rightBumper().onTrue(
                //Update Status
                Globals.INSTANCE.forwardsRobotState());

        Mercurial.gamepad1().leftBumper().onTrue(
                //Update status
                Globals.INSTANCE.backwardsRobotState());

        Mercurial.gamepad1().touchpad().onTrue(
                Globals.INSTANCE.goToIdle()
        );

        Mercurial.gamepad1().guide().onTrue(
                Globals.INSTANCE.reject()
        );

        // MODE CHANGING GAMEBIND
        Mercurial.gamepad1().dpadLeft().onTrue(
                new Parallel(
                        Globals.INSTANCE.goToIdle(),
                        Globals.INSTANCE.changeState()
                ));

        // Claw Changing Gamebinds

        Mercurial.gamepad1().circle().onTrue(
          OLDINTAKE.INSTANCE.rotateClawForwards()
        );

        Mercurial.gamepad1().square().onTrue(
          OLDINTAKE.INSTANCE.rotateClawBackwards()
        );

        Mercurial.gamepad1().options().onTrue(
                OLDINTAKE.INSTANCE.toggleClaw()
        );

        // Wrist Changing Gamebinds



        // Arm Changing Gamebinds

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
