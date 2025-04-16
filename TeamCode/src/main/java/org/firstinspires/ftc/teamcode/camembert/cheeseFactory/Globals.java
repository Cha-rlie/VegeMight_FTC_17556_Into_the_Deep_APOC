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


    public static double WRIST_IDLE = 0.12; //0.36;
    public static double WRIST_PICKUP = 0.21;
    public static double WRIST_DEPOSIT = 0.51;
    public static double WRIST_PARK = 0.455;
    public static double WRIST_SPECIMEN_DEPOSIT = 0.9;
    public static double WRIST_SPECIMEN_DEPOSIT_TELEOP = 0.79;
    public static double WRIST_SPECIMEN_PICKUP = 0.55;

    public static double CLAW_OPEN = 0.8;
    public static double CLAW_CLOSED = 0.36; //1;

    public static double ARM_IDLE = 0; //0.028;
    public static double ARM_HOVER = 0.4942;
    public static double ARM_PICKUP = 0.5723;
    public static double ARM_DEPOSIT = 0.237;
    //public WHAT HERE CHARLIE_
    public static double ARM_PARK = 0.28;
    public static double ARM_SPECIMEN_PICKUP = 0.4929;
    public static double ARM_SPECIMEN_DEPOSIT= 0;

    public static int LIFT_LOW = 0;
    public static int LIFT_HIGH = 1700;
    public static int LIFT_SPECIMEN_DEPOSIT = 100;
    public static int LIFT_SPECIMEN_HOVER = 1250;
    public static int LIFT_SPECIMEN_PICKUP = 600;
    public static int LIFT_SPECIMEN_HOVER_TELEOP = 940;

    public static int FLAG_UP =1;
    public static double FLAG_DOWN= 0.33;

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
