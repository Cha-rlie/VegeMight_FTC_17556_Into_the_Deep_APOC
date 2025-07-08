package org.firstinspires.ftc.teamcode.dairyFarm.subsytems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.cachinghardware.CachingCRServo;
import dev.frozenmilk.dairy.cachinghardware.CachingServo;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotations;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.SDKSubsystem;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.util.cell.Cell;
import kotlin.annotation.MustBeDocumented;

public class CRIntake extends SDKSubsystem {
    private final Cell<CachingCRServo> intakeServo = subsystemCell(() -> new CachingCRServo(getHardwareMap().get(CRServo.class, "SM")));
    public static final Intake INSTANCE = new Intake();
    public static boolean isIntakeTrue= false;

    public CRIntake(){}
    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        // Init sequence
        getTelemetry().addLine("Intake Initalising");
    }

    @NonNull
    public Lambda intakeSample() {
        return new Lambda ("Turn Intake")
                .addExecute(()-> {
                    if (isIntakeTrue) {
                        intakeServo.get().setPower(0.5);
                    } else {
                        intakeServo.get().setPower(0);
                    }
                });
    }

    @NonNull
    public Lambda outtakeSample(){
        return new Lambda("Turn Intake")
                .addExecute(()->{
                    intakeServo.get().setPower(-0.5);
                });
    }


    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @MustBeDocumented
    @Inherited

    public @interface Attach{}

    private Dependency<?> dependency = Subsystem.DEFAULT_DEPENDENCY.and(new SingleAnnotations<>(CRIntake.Attach.class));

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

