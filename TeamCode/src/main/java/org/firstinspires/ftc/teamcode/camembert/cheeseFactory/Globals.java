package org.firstinspires.ftc.teamcode.camembert.cheeseFactory;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.dairyFarm.subsytems.SampleManipulator;
import org.firstinspires.ftc.teamcode.dairyFarm.subsytems.Wrist;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotations;
import dev.frozenmilk.dairy.core.util.OpModeLazyCell;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Command;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import dev.frozenmilk.mercurial.commands.util.Wait;
import dev.frozenmilk.mercurial.subsystems.SDKSubsystem;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import kotlin.annotation.MustBeDocumented;

public class Globals extends SDKSubsystem {

    public static final Globals INSTANCE = new Globals();
    public static boolean isSampleModeTrue = true;

    // Declare the global variables
    public OpModeLazyCell<RobotState> robotState = new OpModeLazyCell<>(() -> RobotState.IDLE);

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
        robotState.accept(RobotState.IDLE);
    }

    public static RobotState getRobotState() {
        return INSTANCE.robotState.get();
    }

    @NonNull
    public Lambda setRobotState(RobotState newRobotState) {
        return new Lambda("Setting Robot State")
                .addExecute(() -> INSTANCE.robotState.accept(newRobotState));
    }

    @NonNull
    public Lambda backwardsRobotState() {
        return new Lambda("One Stage Backwards")
                .addExecute(() -> {
                    if (robotState.get() == RobotState.IDLE) {
                        if (isSampleModeTrue) {
                            robotState.accept(RobotState.HOVERBEFOREGRAB);
                        } else if (!isSampleModeTrue) {
                            robotState.accept(RobotState.INTAKESPECIMEN);
                        }
                    } else if (robotState.get() == RobotState.DEPOSIT) {
                        robotState.accept(RobotState.IDLE);
                    } else if (robotState.get() == RobotState.HOVERAFTERGRAB) {
                        robotState.accept(RobotState.GRAB);
                    } else if (robotState.get() == RobotState.GRAB) {
                        robotState.accept(RobotState.HOVERBEFOREGRAB);
                    } else if (robotState.get() == RobotState.HOVERBEFOREGRAB) {
                        robotState.accept(RobotState.IDLE);
                    } else if (robotState.get() == RobotState.INTAKESPECIMEN) {
                        robotState.accept(RobotState.IDLE);
                    } else if (robotState.get() == RobotState.IDLE); {
                        robotState.accept(RobotState.IDLE);
                    }
                });
    }
    @NonNull
    public Lambda goToIdle() {
        return new Lambda("Go IDLE")
                .addExecute(()-> {
                   if (robotState.get() != RobotState.IDLE) {
                       robotState.accept(RobotState.IDLE);
                   }
                });
    }

    public Lambda changeState(){
        return new Lambda("Change States")
                .addExecute(()-> {
                   isSampleModeTrue = !isSampleModeTrue;
                });
    }
    @NonNull
    public Lambda forwardsRobotState() {
        return new Lambda("One Stage Forwards")
                .addExecute(() -> {
                    if (robotState.get() == RobotState.IDLE) {
                        if (isSampleModeTrue){
                            robotState.accept(RobotState.DEPOSIT);
                        } else if (!isSampleModeTrue){
                            robotState.accept(RobotState.DEPOSITSPECIMEN);
                        }
                    } else if (robotState.get() == RobotState.DEPOSIT) {
                        robotState.accept(RobotState.IDLE);
                    } else if (robotState.get() == RobotState.HOVERAFTERGRAB) {
                        robotState.accept(RobotState.IDLE);
                    } else if (robotState.get() == RobotState.GRAB) {
                        robotState.accept(RobotState.HOVERAFTERGRAB);
                    } else if (robotState.get() == RobotState.HOVERBEFOREGRAB) {
                        robotState.accept(RobotState.GRAB);
                    } else if (robotState.get() == RobotState.INTAKESPECIMEN) {
                        new Sequential(
                            //Sequence of Commands - close claw, move wrist, then go to idle
                                SampleManipulator.INSTANCE.intakeSpecimenSequence().then(
                                        new Wait(0.1).then(
                                                Wrist.INSTANCE.intakeSpecimenSequence().then(
                                                    new Wait(0.1)
                                                )
                                        )
                                )

                        );
                        new Wait(0.01);
                        robotState.accept(RobotState.IDLE);
                    } else if (robotState.get() == RobotState.DEPOSITSPECIMEN) {
                        //Another Sequence of commands
                        new Sequential(

                        );
                        //Lower lift to 600
                        //Wait 350
                        //open claw
                        //Wait 300
                        //Wrist to 0.9
                    }
                });
    }

    @Override
    public void preUserLoopHook(@NonNull Wrapper opMode) {
        getTelemetry().addData("Robot State", INSTANCE.robotState.get());
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @MustBeDocumented
    @Inherited
    public @interface Attach{}

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
