package org.firstinspires.ftc.teamcode.dairyFarm.subsytems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
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
import dev.frozenmilk.mercurial.commands.groups.Parallel;
import dev.frozenmilk.mercurial.subsystems.SDKSubsystem;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.util.cell.Cell;
import kotlin.annotation.MustBeDocumented;

public class Intake extends SDKSubsystem {
    public static final Intake INSTANCE = new Intake();

    private final Cell<CachingServo> intakeServo = subsystemCell(() -> new CachingServo(getHardwareMap().get(Servo.class, "sampleManipulator")));

    public static boolean intakeIsTrue = true;
    public static double speedOfRotation=1;

    public Intake() {}

    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        // Init sequence
        getTelemetry().addLine("Intake Initalising");

        setDefaultCommand(
            new Parallel(
                turnClaw(),
                new Lambda("print name").addExecute(() -> getTelemetry().addData("Servo pos", intakeServo.get().getPosition()))
            ));

    }
    public Lambda adjustClawForwards() {
        return new Lambda ("TESTING")
                .addExecute(()-> {
                    intakeServo.get().setPosition(intakeServo.get().getPosition()+0.01);
                    getTelemetry().addData("ServoPosition",intakeServo.get().getPosition());
                });
    }

    public Lambda adjustClawBackwards() {
        return new Lambda ("TESTING")
                .addExecute(()-> {
                    intakeServo.get().setPosition(intakeServo.get().getPosition()-0.01);
                    getTelemetry().addData("ServoPosition",intakeServo.get().getPosition());
                });
    }

    @NonNull
    public Lambda turnClaw() {
        return new Lambda ("Turn Intake")
                .addExecute(()-> {
                    if (intakeIsTrue==true) {
                        intakeServo.get().setPosition(0.77); // 0.77
                    } else {
                        intakeServo.get().setPosition(0.52); // 0.52
                    }
                    getTelemetry().addLine("Intake Turning");
                    getTelemetry().update();
                });
    }

    @NonNull
    public Lambda toggleIntake() {
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

    private Dependency<?> dependency = Subsystem.DEFAULT_DEPENDENCY.and(new SingleAnnotations<>(Intake.Attach.class));

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
