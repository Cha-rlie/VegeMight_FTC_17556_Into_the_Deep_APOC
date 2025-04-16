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
import dev.frozenmilk.mercurial.commands.util.Wait;
import dev.frozenmilk.mercurial.subsystems.SDKSubsystem;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.util.cell.Cell;
import kotlin.annotation.MustBeDocumented;

public class Flag extends SDKSubsystem {
    public static final Flag INSTANCE = new Flag();

    public final Cell<CachingServo> flagServo = subsystemCell(()-> new CachingServo(getHardwareMap().get(Servo.class, "flag")));

    private Flag(){
    }

    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        // Init sequence
        getTelemetry().addLine("Flag Initalising");
        getTelemetry().update();

        setDefaultCommand(revealFlag());
    }

    @NonNull
    public Lambda revealFlag(){
        return new Lambda ("Flag turning")
                .addRequirements(INSTANCE)
                .addExecute(() -> {
                    switch (Globals.getCurrentInstance().getCurrentRobotState()) {
                        case PARKNOASCENT:
                            flagServo.get().setPosition(1);
                            break;
                        case PARKASCENT:
                            //Maybe add Wait incase
                            flagServo.get().setPosition(1);
                            break;
                        default:
                            flagServo.get().setPosition(0.33);
                            break;
                    }
                });
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
