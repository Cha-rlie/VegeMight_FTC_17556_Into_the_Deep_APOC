package org.firstinspires.ftc.teamcode.dairyFarm.subsytems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.cachinghardware.CachingCRServo;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotations;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.commands.groups.Parallel;
import dev.frozenmilk.mercurial.subsystems.SDKSubsystem;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.util.cell.Cell;
import kotlin.annotation.MustBeDocumented;

public class SampleManipulator2 extends SDKSubsystem {
    public static final SampleManipulator2 INSTANCE = new SampleManipulator2();

    private final Cell<CachingCRServo> intakeServo = subsystemCell(() -> new CachingCRServo(getHardwareMap().get(CRServo.class, "sampleManipulator")));

    public static boolean intakeIsTrue = true;
    public static double speedOfRotation=1;

    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        // Init sequence
        getTelemetry().addLine("Intake Initalising");

        setDefaultCommand(
            turnClaw()
        );

    }

    @NonNull
    public Lambda turnClaw() {
        return new Lambda ("Turn Intake")
                .addExecute(()-> {
                    intakeServo.get().setPower(speedOfRotation);
                    if (intakeIsTrue==true) {
                        intakeServo.get().setDirection(DcMotorSimple.Direction.FORWARD);
                    } else {
                        intakeServo.get().setDirection(DcMotorSimple.Direction.REVERSE);
                    }
                    getTelemetry().addLine("Intake Turning");
                    getTelemetry().update();
                });
    }

    @NonNull
    public Lambda toggleClaw() {
        return new Lambda ("Toggle Intake")
                .addExecute(()-> {
                    if (intakeIsTrue == true) {
                        intakeIsTrue = false;
                    } else if (intakeIsTrue == false) {
                        intakeIsTrue = true;
                    }
                    getTelemetry().addLine("Intake Toggled");
                    getTelemetry().update();
                });
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @MustBeDocumented
    @Inherited

    public @interface Attach{}

    private Dependency<?> dependency = Subsystem.DEFAULT_DEPENDENCY.and(new SingleAnnotations<>(SampleManipulator2.Attach.class));

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
