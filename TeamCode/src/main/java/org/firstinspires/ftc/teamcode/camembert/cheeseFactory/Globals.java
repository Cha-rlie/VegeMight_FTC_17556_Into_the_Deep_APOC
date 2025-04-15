package org.firstinspires.ftc.teamcode.camembert.cheeseFactory;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.robot.Robot;

import org.firstinspires.ftc.teamcode.camembert.driveStuff.MecanumDriveCalculator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.core.FeatureRegistrar;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotations;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.mercurial.subsystems.SubsystemObjectCell;
import kotlin.annotation.MustBeDocumented;

public class Globals implements Subsystem {

    public static final Globals INSTANCE = new Globals();

    // Declare the global variables
    RobotState currentRobotState;


    // Constructor that builds the drivetrain subsystem class
    public Globals() {
        currentRobotState = RobotState.IDLE;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @MustBeDocumented
    @Inherited
    public @interface Attach{}

    public static Globals getCurrentInstance() {
        return INSTANCE;
    }

    public static RobotState getCurrentRobotState() {
        return INSTANCE.currentRobotState;
    }

    private Dependency<?> dependency = Subsystem.DEFAULT_DEPENDENCY.and(new SingleAnnotations<>(Attach.class));

    // SubsystemObjectCells get eagerly reevaluated at the start of every OpMode, if this subsystem is attached
    // this means that we can always rely on motor to be correct and up-to-date for the current OpMode
    // this can also work with Calcified
    //zprivate final SubsystemObjectCell<RobotState> currentRobotState = subsystemCell(() -> FeatureRegistrar.getActiveOpMode().hardwareMap.get(DcMotorEx.class, ""));

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
