package org.firstinspires.ftc.teamcode.dairyFarm.subsytems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.camembert.cheeseFactory.Globals;
import org.firstinspires.ftc.teamcode.camembert.cheeseFactory.RobotState;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.cachinghardware.CachingDcMotorEx;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotations;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.Command;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import dev.frozenmilk.mercurial.commands.util.IfElse;
import dev.frozenmilk.mercurial.subsystems.SDKSubsystem;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.dairy.cachinghardware.CachingDcMotor;
import dev.frozenmilk.util.cell.Cell;
import kotlin.annotation.MustBeDocumented;


public class Lift extends SDKSubsystem {
    public static final Lift INSTANCE = new Lift();

    // Declare the motors and mecanum drive
    // @charlie please check motor names
    //Low basket - 400
    // Max extension for intake - 800
    public final Cell<CachingDcMotorEx> motorLiftL = subsystemCell(() -> new CachingDcMotorEx(getHardwareMap().get(DcMotorEx.class, "LS")));
    public final Cell<CachingDcMotorEx> motorLiftR = subsystemCell(() -> new CachingDcMotorEx(getHardwareMap().get(DcMotorEx.class, "RS")));

    public static int RTP;
    public static int adjustment;

    private Lift() {
    }

    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        // Init sequence
        RTP = 0;
        adjustment = 0;
        getTelemetry().addLine("Slides Initalising");
        motorLiftL.get().setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        motorLiftR.get().setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        motorLiftL.get().resetDeviceConfigurationForOpMode();
        motorLiftR.get().resetDeviceConfigurationForOpMode();
        motorLiftR.get().setDirection(DcMotorSimple.Direction.REVERSE);
        motorLiftR.get().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorLiftL.get().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorLiftL.get().setTargetPositionTolerance(50);
        motorLiftL.get().setTargetPositionTolerance(50);
        motorLiftL.get().setTargetPosition(0);
        motorLiftR.get().setTargetPosition(0);
        motorLiftL.get().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorLiftR.get().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorLiftL.get().setPower(1);
        motorLiftR.get().setPower(1);
        setDefaultCommand(goToPosition());
        //initialiseLiftMotors();
    }

    @Override
    public void preUserLoopHook(@NonNull Wrapper opMode) {
        getTelemetry().addData("Lift RTP", RTP);
        getTelemetry().addData("Lift Adjustment", adjustment);
        getTelemetry().addData("Lift Target Position", motorLiftL.get().getTargetPosition());
        getTelemetry().addData("LS Pos", motorLiftL.get().getCurrentPosition());
    }

    // TODO: FORCE IT DOWN TO START FROM CONSISTENT POSITION

    @NonNull
    public Lambda forceDown() {
        return new Lambda("Force Down")
            .addExecute(()->{
                // Figure this out
            });
    }
    @NonNull
    public Lambda initialiseLiftMotors() {
        return new Lambda("Stop All Motors")
                .addRequirements(INSTANCE)
                .addExecute(() -> {
                    motorLiftL.get().setTargetPosition(400);
                    motorLiftR.get().setTargetPosition(400);
                    motorLiftL.get().setPower(1);
                    motorLiftR.get().setPower(1);
                });
    }

    @NonNull
    public Sequential goToPosition() {
        return new Sequential(
                new IfElse(
                        () -> Globals.updateRobotStateTrue && !Globals.liftAcceptState,
                        updatePosFromState(),
                        new Lambda("EMPTY")
                ),
                new Lambda("Change Position for Lift")
                .addRequirements(INSTANCE)
                .setExecute(() -> {
                    motorLiftL.get().setTargetPosition(RTP+adjustment);
                    motorLiftR.get().setTargetPosition(RTP+adjustment);
                    motorLiftL.get().setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    motorLiftR.get().setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    motorLiftL.get().setPower(1);
                    motorLiftR.get().setPower(1);
                })
                );
    }

    @NonNull
    public Lambda updatePosFromState() {
        return new Lambda("ChangeLiftPos")
            .addRequirements(INSTANCE)
            .setExecute(() -> {
                Globals.liftAcceptState = true;
                if (Globals.getRobotState() == RobotState.IDLE) {
                    if (Globals.lastRobotState == RobotState.GRAB || Globals.lastRobotState == RobotState.HOVERBEFOREGRAB || Globals.lastRobotState == RobotState.HOVERAFTERGRAB) {
                        adjustment = adjustment;
                    } else {
                        adjustment = 0;
                    }
                } else {
                    adjustment = 0;
                }
                switch (Globals.getRobotState()) {
                    case IDLE:
                        RTP = 0;
                        break;
                    case DEPOSIT:
                        RTP = 1300;
                        break;
                    case HOVERBEFOREGRAB:
                    case GRAB:
                    case HOVERAFTERGRAB:
                        RTP = 250;
                        break;
                    default:
                        RTP = 0;
                        break;
                }
            });
    }

    @NonNull
    public Lambda adjustUp(){
        return new Lambda("Lift Up")
                //.addRequirements(INSTANCE)
                .addExecute(()-> {
                    if (Globals.getRobotState() == RobotState.DEPOSIT || Globals.getRobotState() == RobotState.IDLE) {
                        if ((RTP+adjustment) + 100 < 1500 /*CHANGE THIS NUMBER*/) {
                            adjustment += 100;
                        } else {adjustment = 1500 - RTP;}
                    } else {
                        if (RTP + adjustment + 100 < 800) {
                            adjustment += 100;
                        } else {adjustment = 800 - RTP;}
                    }
                });
    }

    @NonNull
    public Lambda adjustDown(){
        return new Lambda("Lift Down")
                //.addRequirements(INSTANCE)
                .addExecute(()-> {
                    adjustment = Math.max(RTP + adjustment - 100, 0);
                });
    }

    public boolean notAdjusted() {
        return (motorLiftL.get().getCurrentPosition() < Lift.RTP + 50 && motorLiftL.get().getCurrentPosition() > Lift.RTP - 50);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @MustBeDocumented
    @Inherited
    public @interface Attach{}

    private Dependency<?> dependency = Subsystem.DEFAULT_DEPENDENCY.and(new SingleAnnotations<>(Lift.Attach.class));

    @NonNull
    @Override
    public Dependency<?> getDependency() {
        return dependency;
    }

    @Override
    public void setDependency(@NonNull Dependency<?> dependency) {
        this.dependency = dependency;
    }
}

