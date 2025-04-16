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

public class Wrist extends SDKSubsystem {
    public static final Wrist INSTANCE = new Wrist();

    public final Cell<CachingServo> wristServo= subsystemCell(() -> new CachingServo(getHardwareMap().get(Servo.class, "wristRot")));

    private Wrist(){
    }

    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        // Init sequence
        getTelemetry().addLine("Wrist Initalising");
        getTelemetry().update();

        setDefaultCommand(turnWrist());
    }

    @NonNull
    public Lambda turnWrist() {
        return new Lambda("turnwrist")
                .addRequirements(INSTANCE)
                .addExecute(()-> {
                    switch (Globals.getCurrentInstance().getCurrentRobotState()) {
                        case IDLE:
                            wristServo.get().setPosition(0.12);
                            break;
                        case DEPOSIT:
                            wristServo.get().setPosition(0.51);
                            break;
                        case HOVERAFTERGRAB:
                            wristServo.get().setPosition(0.21);
                            break;
                        case HOVERBEFOREGRAB:
                            wristServo.get().setPosition(0.21);
                            break;
                        case GRAB:
                            wristServo.get().setPosition(0.21);
                            break;
                        default:
                            wristServo.get().setPosition(0.12);
                            break;
                    }
                }
                );
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @MustBeDocumented
    @Inherited

    public @interface Attach{}

    private Dependency<?> dependency = Subsystem.DEFAULT_DEPENDENCY.and(new SingleAnnotations<>(Drivetrain.Attach.class));

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
