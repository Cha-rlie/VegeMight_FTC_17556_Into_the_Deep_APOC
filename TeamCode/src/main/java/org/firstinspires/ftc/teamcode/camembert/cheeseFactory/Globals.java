package org.firstinspires.ftc.teamcode.camembert.cheeseFactory;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.dairyFarm.subsytems.SampleManipulator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;

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
    public static boolean updateRobotStateTrue = false;

    // Declare the global variables
    private OpModeLazyCell<RobotState> robotState = new OpModeLazyCell<>(() -> RobotState.IDLE);

    private HashMap<Object, Command> goForwardState;
    private HashMap <Object, Command> goBackwardState;
    
    // Constructor that builds the drivetrain subsystem class and Hashmaps :D
    public Globals() {
        robotState.accept(RobotState.IDLE);

        goForwardState = new HashMap<Object, Command>() {{
            //Global States
            put(RobotState.IDLE, new Lambda("Go forwards from IDLE").addExecute(()-> {
                if (Globals.isSampleModeTrue == true) {
                    robotState.accept(RobotState.DEPOSIT);
                } else {
                    robotState.accept(RobotState.DEPOSITSPECIMEN);
                }
            }));
            put(RobotState.REJECT, new Lambda("Go forwards from REJECT").addExecute(()->{
                new Sequential(
                        new Lambda("Open Claw").addExecute(()->{SampleManipulator.INSTANCE.clawOpen=true;}), //OPEN CLAW
                        new Wait(0.1),
                        new Lambda ("Spec or sample reject").addExecute(()->{
                            if (Globals.isSampleModeTrue == true) {
                                new Lambda("To HOVER").addExecute(()->{robotState.accept(RobotState.HOVERBEFOREGRAB);});
                            } else {
                                new Lambda("To HOVER").addExecute(()->{robotState.accept(RobotState.SPECHOVER);});
                            }
                        })
                );
            }));

            //Sample states
            put(RobotState.DEPOSIT, new Lambda("Go forwards from IDLE").addExecute(()->{
                new Sequential(
                    new Lambda("Open Claw").addExecute(()->{SampleManipulator.INSTANCE.clawOpen=true;}), //OPEN CLAW
                    new Lambda("To IDLE").addExecute(()->{robotState.accept(RobotState.IDLE);}) // Go to IDLE ** NEED TO MAKE THIS NO L3 ASCENT **
                );
            }));
            put(RobotState.HOVERAFTERGRAB, new Lambda("Go forwards from HOVER AFTER").addExecute(()->{
                robotState.accept(RobotState.IDLE);
            }));
            put(RobotState.HOVERBEFOREGRAB, new Lambda("Go forwards from HOVER BEFORE").addExecute(()->{
                robotState.accept(RobotState.GRAB);
            }));
            put(RobotState.GRAB, new Lambda("Go forwards from HOVER GRAB").addExecute(()->{
                robotState.accept(RobotState.HOVERAFTERGRAB);
            }));

            //Specimen States
            put(RobotState.DEPOSITSPECIMEN, new Lambda("Go forwards from SPECDEPOSIT").addExecute(()->{
                // WHATEVER GOES HERE - DUNNO ABT NEW SPEC
                robotState.accept(RobotState.IDLE);
            }));
            put(RobotState.SPECHOVER, new Lambda("Go forwards from SPECHOVER").addExecute(()->{
                // WHATEVER GOES HERE - DUNNO ABT NEW SPEC
                robotState.accept(RobotState.GRAB);
            }));
            put(RobotState.SPECGRAB, new Lambda("Go forwards from SPECGRAB").addExecute(()->{
                // WHATEVER GOES HERE - DUNNO ABT NEW SPEC
                robotState.accept(RobotState.IDLE);
            }));
        }};

        goBackwardState = new HashMap<Object, Command>() {{
            //Global States
            put(RobotState.IDLE, new Lambda("Go forwards from IDLE").addExecute(()-> {
                if (Globals.isSampleModeTrue == true) {
                    robotState.accept(RobotState.HOVERBEFOREGRAB);
                } else {
                    robotState.accept(RobotState.SPECHOVER);
                }
            }));
            //NOTE TO SELF - REJECT HAS NO BACKWARDS

            //Sample states
            put(RobotState.DEPOSIT, new Lambda("Go forwards from IDLE").addExecute(()->{
                new Lambda("To IDLE").addExecute(()->{robotState.accept(RobotState.IDLE);}); // DO NOT OPEN CLAW
            }));
            put(RobotState.HOVERAFTERGRAB, new Lambda("Go forwards from HOVER AFTER").addExecute(()->{
                robotState.accept(RobotState.GRAB);
            }));
            put(RobotState.HOVERBEFOREGRAB, new Lambda("Go forwards from HOVER BEFORE").addExecute(()->{
                robotState.accept(RobotState.IDLE);
            }));
            put(RobotState.GRAB, new Lambda("Go forwards from HOVER GRAB").addExecute(()->{
                robotState.accept(RobotState.HOVERBEFOREGRAB);
            }));

            //Specimen States
            put(RobotState.DEPOSITSPECIMEN, new Lambda("Go forwards from SPECDEPOSIT").addExecute(()->{
                // WHATEVER GOES HERE - DUNNO ABT NEW SPEC
                robotState.accept(RobotState.IDLE);
            }));
            put(RobotState.SPECHOVER, new Lambda("Go forwards from SPECHOVER").addExecute(()->{
                // WHATEVER GOES HERE - DUNNO ABT NEW SPEC
                robotState.accept(RobotState.IDLE);
            }));
            put(RobotState.SPECGRAB, new Lambda("Go forwards from SPECGRAB").addExecute(()->{
                // WHATEVER GOES HERE - DUNNO ABT NEW SPEC
                robotState.accept(RobotState.SPECHOVER);
            }));
        }};

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
        return new Lambda("Backwards State")
                .addRequirements(INSTANCE)
                .addExecute(()-> {
                            updateRobotStateTrue=true;
                            if (Globals.updateRobotStateTrue == true) {
                                new Lambda("Immmmm going back").addExecute(() -> goBackwardState.get(Globals.getRobotState()));
                            }
                            new Wait(0.1);
                            updateRobotStateTrue=false;
                        }
                );
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

    @NonNull
    public Lambda reject() {
        return new Lambda("GO REJECT")
                .addExecute(()->{
                   robotState.accept(RobotState.REJECT);
                });
    }

    public Lambda changeState(){
        return new Lambda("Change Scoring States")
                .addExecute(()-> {
                   isSampleModeTrue = !isSampleModeTrue;
                });
    }
    @NonNull
    public Lambda forwardsRobotState() {
        return new Lambda("Forwards State")
                .addRequirements(INSTANCE)
                .addExecute(()-> {
                            updateRobotStateTrue=true;
                            if (Globals.updateRobotStateTrue == true) {
                                new Lambda("Immmmm going forwards").addExecute(() -> goForwardState.get(Globals.getRobotState()));
                            }
                            new Wait(0.1);
                            updateRobotStateTrue=false;
                        }
                );
    }

    @Override
    public void preUserLoopHook(@NonNull Wrapper opMode) {
        getTelemetry().addData("Robot State", INSTANCE.robotState.get());
        getTelemetry().addData("isSampleTrue", isSampleModeTrue);
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