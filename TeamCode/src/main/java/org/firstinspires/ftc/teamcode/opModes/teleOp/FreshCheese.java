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

@TeleOp(name="FreshCheese")
@Config

@Mercurial.Attach

@Globals.Attach
@Drivetrain.Attach
@Intake.Attach
@Pitching.Attach
@Arm.Attach
@Lift.Attach

public class FreshCheese extends OpMode{
    public RobotState currentRobotState = RobotState.IDLE;

    @Override
    public void init() {
        Globals.INSTANCE.setRobotState(RobotState.IDLE);

        // State binds
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

        // Mode binds
        Mercurial.gamepad1().dpadLeft().onTrue(
                new Parallel(
                        Globals.INSTANCE.goToIdle(),
                        Globals.INSTANCE.changeState()
                ));

        // Intake binds
        Mercurial.gamepad1().options().onTrue(
            Intake.INSTANCE.toggleIntake()
        );

        // Arm binds
        Mercurial.gamepad2().cross().onTrue(
            Arm.INSTANCE.adjustArmDown()
        );

        Mercurial.gamepad2().triangle().onTrue(
            Arm.INSTANCE.adjustArmUp()
        );

        Mercurial.gamepad2().options().onTrue(
            Arm.INSTANCE.resetAdjustment()
        );

        // Pitching binds
        Mercurial.gamepad2().dpadDown().onTrue(
            Pitching.INSTANCE.adjustPitchingDown()
        );

        Mercurial.gamepad2().dpadUp().onTrue(
            Pitching.INSTANCE.adjustPitchingUp()
        );

        // Lift binds
        Mercurial.gamepad2().square().onTrue(
            Lift.INSTANCE.forceDown()
        );

        // Lift adjustment
        Mercurial.gamepad1().dpadUp().onTrue(
            Lift.INSTANCE.adjustUp()
        );

        Mercurial.gamepad1().dpadDown().onTrue(
            Lift.INSTANCE.adjustDown()
        );

        // Park binds
        Mercurial.gamepad2().touchpad().and(Mercurial.gamepad2().guide().and(Mercurial.gamepad2().ps())).onTrue(
            Globals.INSTANCE.parkAscent()
        );

        Mercurial.gamepad2().dpadLeft().onTrue(
            Globals.INSTANCE.parkNoAscent()
        );

        /*
        Quick rundown of Controls:

        Gamepad 1:
        Left Joystick x: Movement ✅
        Left Joystick y: Movement ✅
        Right Joystick x: Movement ✅
        Right Joystick y: Movement ✅
        L1: Forwards State ✅
        R1: Backwards State ✅
        L2: Toggle speed - slower
        R2: Toggle speed - faster
        L3:
        R3:
        Triangle:
        Cross:
        Square:
        Circle:
        Options: Toggle intake ✅
        Guide: Reject ✅
        Dpad Up:
        Dpad Down:
        Dpad Left: Swap Mode
        Dpad Right:
        Touchpad: To IDLE ✅
        PS5:

        Gamepad 2:
        Left Joystick x: TBC - Lift adjustment
        Left Joystick y:
        Right Joystick x:
        Right Joystick y:
        L1: TBC - FORCED RESTART Back to IDLE w L3 and R3
        R1:
        L2:
        R2:
        L3: TBC - FORCED RESTART Back to IDLE w L1 and R3
        R3: TBC - FORCED RESTART Back to IDLE w L1 and L3
        Triangle: Adjust Arm Up ✅
        Cross: Adjust Arm Down ✅
        Square:
        Circle:
        Options: Reset Arm
        Guide: Park Ascent (With Touchpad and With PS)
        Dpad Up: Adjust Pitching up ✅
        Dpad Down: Adjust Pitching down ✅
        Dpad Left: Park No Ascent
        Dpad Right:
        Touchpad: Park Ascent (With Guide and With PS)
        PS5: Park Ascent (With Touchpad and With PS)
         */


        telemetry.update();
    }

    @Override
    public void loop() {
        telemetry.update();
    }
}
