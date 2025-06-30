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

import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotations;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
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
    private final Cell<CachingDcMotor> motorLiftL = subsystemCell(() -> new CachingDcMotor(getHardwareMap().get(DcMotorEx.class, "LS")));
    private final Cell<CachingDcMotor> motorLiftR = subsystemCell(() -> new CachingDcMotor(getHardwareMap().get(DcMotorEx.class, "RS")));

    public static int RTP = 0;

    private Lift() {
    }

    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        // Init sequence
        getTelemetry().addLine("Slides Initalising");

        motorLiftL.get().setDirection(DcMotorSimple.Direction.REVERSE);
        motorLiftR.get().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorLiftL.get().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        initialiseLiftMotors();
        setDefaultCommand(goToPosition());
    }

    @Override
    public void preUserLoopHook(@NonNull Wrapper opMode) {
        getTelemetry().addData("Left Motor Target Position", motorLiftL.get().getTargetPosition());
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
                    motorLiftL.get().setTargetPosition(0);
                    motorLiftR.get().setTargetPosition(0);
                    motorLiftL.get().setPower(1);
                    motorLiftR.get().setPower(1);
                });
    }

    @NonNull
    public Lambda goToPosition() {
        return new Lambda("ChangePosition")
                .addRequirements(INSTANCE)
                .addExecute(() -> {
                    if (Globals.isSampleModeTrue==true) {
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
                    motorLiftL.get().setTargetPosition(RTP);
                    motorLiftR.get().setTargetPosition(RTP);
                    motorLiftL.get().setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    motorLiftR.get().setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    getTelemetry().addLine("AM I RUNNING TO TELEMETRY PLS");
                    if (motorLiftL.get().getCurrentPosition() > motorLiftL.get().getTargetPosition() + 50 && motorLiftL.get().getCurrentPosition() < motorLiftL.get().getTargetPosition() - 100) {
                        motorLiftL.get().setPower(0);
                        motorLiftR.get().setPower(0);
                    } else {
                        motorLiftL.get().setPower(0.7);
                        motorLiftR.get().setPower(0.7);
                    }
                });
    }

    public Lambda adjustUp(){
        return new Lambda("Lift Up")
                .addExecute(()-> {
                    if (RTP+50<1700 /*CHANGE THIS NUMBER*/) {
                        RTP = RTP + 50;
                    }
                });
    }

    public Lambda adjustDown(){
        return new Lambda("Lift Up")
                .addExecute(()-> {
                    if (RTP-50>0){
                        RTP=RTP-50;
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

