package org.firstinspires.ftc.teamcode.dairyFarm.subsytems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.camembert.cheeseFactory.Globals;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.cachinghardware.CachingDcMotorEx;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotations;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Command;
import dev.frozenmilk.mercurial.commands.Lambda;
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

    private Lift() {
    }

    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        // Init sequence
        RTP = 0;
        getTelemetry().addLine("Slides Initalising");
        motorLiftL.get().resetDeviceConfigurationForOpMode();
        motorLiftR.get().resetDeviceConfigurationForOpMode();
        motorLiftR.get().setDirection(DcMotorSimple.Direction.REVERSE);
        motorLiftR.get().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorLiftL.get().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorLiftL.get().setTargetPositionTolerance(50);
        motorLiftL.get().setTargetPositionTolerance(50);
        motorLiftL.get().setTargetPosition(432);
        motorLiftR.get().setTargetPosition(432);
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
        getTelemetry().addData("Lift Target Position", motorLiftL.get().getTargetPosition());
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
    public Lambda goToPosition() {
        return new Lambda("ChangePosition")
                .addRequirements(INSTANCE)
                .addExecute(() -> {
                    if (Globals.updateRobotStateTrue) {updatePosFromState();}
                    motorLiftL.get().setTargetPosition(RTP);
                    motorLiftR.get().setTargetPosition(RTP);
                    motorLiftL.get().setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    motorLiftR.get().setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    getTelemetry().addLine("AM I RUNNING TO TELEMETRY PLS");
                    motorLiftL.get().setPower(0.9);
                    motorLiftR.get().setPower(0.9);
                });
    }

    @NonNull
    public Lambda updatePosFromState() {
        return new Lambda("ChangeLiftPos")
            .addRequirements(INSTANCE)
            .addExecute(() -> {
                if (Globals.isSampleModeTrue) {
                    switch (Globals.INSTANCE.getRobotState()) {
                        case IDLE:
                            RTP = 0;
                            break;
                        case DEPOSIT:
                            RTP = 1300;
                            break;
                        default:
                            RTP = 0;
                            break;
                    }
                }
            });
    }

    @NonNull
    public Lambda adjustUp(){
        return new Lambda("Lift Up")
                .addRequirements(INSTANCE)
                .addExecute(()-> {
                    if (RTP+100<1500 /*CHANGE THIS NUMBER*/) {
                        RTP = RTP + 100;
                    }
                });
    }

    @NonNull
    public Lambda adjustDown(){
        return new Lambda("Lift Down")
                .addRequirements(INSTANCE)
                .addExecute(()-> {
                    if (RTP-100>0){
                        RTP=RTP-100;
                    }
                });
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

