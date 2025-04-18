package org.firstinspires.ftc.teamcode.dairyFarm.subsytems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.camembert.cheeseFactory.Globals;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.cachinghardware.CachingServo;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotations;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.SDKSubsystem;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.util.cell.Cell;
import kotlin.annotation.MustBeDocumented;


public class Arm extends SDKSubsystem {

    public static final Arm INSTANCE = new Arm();

    public final Cell<CachingServo> leftArm = subsystemCell(() -> new CachingServo(getHardwareMap().get(Servo.class, "leftArm")));
    public final Cell<CachingServo> rightArm = subsystemCell(() -> new CachingServo(getHardwareMap().get(Servo.class, "rightArm")));

    private Arm(){
    }

    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        // Init sequence
        getTelemetry().addLine("Arm Initalising");
        leftArm.get().setDirection(Servo.Direction.REVERSE);

        setDefaultCommand(turnArm());
    }

    @NonNull
    public Lambda turnArm(){
        return new Lambda("TurnArm")
        .addRequirements(INSTANCE)
        .addExecute(() -> {
            switch (Globals.INSTANCE.getRobotState()) {
                case IDLE:
                    leftArm.get().setPosition(0);
                    rightArm.get().setPosition(0);
                    break;
                case DEPOSIT:
                    leftArm.get().setPosition(0.237);
                    rightArm.get().setPosition(0.237);
                    break;
                case HOVERAFTERGRAB:
                    leftArm.get().setPosition(0.4942);
                    rightArm.get().setPosition(0.4942);
                    break;
                case HOVERBEFOREGRAB:
                    leftArm.get().setPosition(0.4942);
                    rightArm.get().setPosition(0.4942);
                    break;
                case GRAB:
                    leftArm.get().setPosition(0.5723);
                    rightArm.get().setPosition(0.5723);
                    break;
                case INTAKESPECIMEN:
                    leftArm.get().setPosition(0);
                    rightArm.get().setPosition(0);
                    break;
                case DEPOSITSPECIMEN:
                    leftArm.get().setPosition(0);
                    rightArm.get().setPosition(0);
                    break;
                default:
                    break;
            }
        });
    }

    @NonNull
    public Lambda adjustArmUp() {
        return new Lambda("adjust wrist")
                .addExecute(()-> {
                    leftArm.get().setPosition(leftArm.get().getPosition()+0.01);
                    rightArm.get().setPosition(rightArm.get().getPosition()+0.01);
                    getTelemetry().addLine("Arm Adjusted");
                    getTelemetry().update();
                });
    }

    @NonNull
    public Lambda adjustArmDown() {
        return new Lambda("adjust wrist")
                .addExecute(()-> {
                    leftArm.get().setPosition(leftArm.get().getPosition()-0.01);
                    rightArm.get().setPosition(rightArm.get().getPosition()-0.01);
                    getTelemetry().addLine("Arm Adjusted");
                });
    }

    @NonNull
    public Lambda resetAdjustment() {
        return new Lambda("reset adjustment")
                .addExecute(() -> {
                    // CHANGE ADJUSTMENT
                    getTelemetry().addLine("Arm Adjustment Reset");
                });
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @MustBeDocumented
    @Inherited
    public @interface Attach{}

    private Dependency<?> dependency = Subsystem.DEFAULT_DEPENDENCY.and(new SingleAnnotations<>(Arm.Attach.class));

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
