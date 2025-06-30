package org.firstinspires.ftc.teamcode.dairyFarm.subsytems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.camembert.cheeseFactory.Globals;
import org.firstinspires.ftc.teamcode.camembert.cheeseFactory.RobotState;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;

import dev.frozenmilk.dairy.cachinghardware.CachingServo;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotations;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Command;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.commands.groups.Parallel;
import dev.frozenmilk.mercurial.subsystems.SDKSubsystem;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.util.cell.Cell;
import kotlin.annotation.MustBeDocumented;


public class Arm extends SDKSubsystem {

    public static final Arm INSTANCE = new Arm();

    public final Cell<CachingServo> leftArm = subsystemCell(() -> new CachingServo(getHardwareMap().get(Servo.class, "LA")));
    public final Cell<CachingServo> rightArm = subsystemCell(() -> new CachingServo(getHardwareMap().get(Servo.class, "RA")));
    public static double armPosition = 0;
    public double adjustment = 0;

    private HashMap<Object, Command> stateToCommandMap;

    private Arm(){
        //HASHMAP OF POSITIONS
        stateToCommandMap = new HashMap<Object, Command>() {{
            put(RobotState.IDLE, new Lambda("IDLE ARM").addExecute(() -> {
                armPosition=0.35;
                adjustment=0;
            }));
            put(RobotState.DEPOSIT, new Lambda("DEPOSIT ARM").addExecute(() -> {
                armPosition=0.25;
                adjustment=0;
            }));
            put(RobotState.HOVERBEFOREGRAB, new Lambda("HBG ARM").addExecute(() -> {
                armPosition=0.35;
                adjustment=0;
            }));
            put(RobotState.GRAB, new Lambda("GRAB ARM").addExecute(() -> {
                armPosition=0.5;
                adjustment=0;
            }));
            put(RobotState.HOVERAFTERGRAB, new Lambda("HAG ARM").addExecute(() -> {
                armPosition=0.35;
                adjustment=0;
            }));
            put(RobotState.SPECHOVER, new Lambda("INTAKE SPECIMEN ARM").addExecute(() -> {
                armPosition=0;
                adjustment=0;
            }));
            put(RobotState.SPECGRAB, new Lambda("INTAKE SPECIMEN ARM").addExecute(() -> {
                armPosition=0;
                adjustment=0;
            }));
            put(RobotState.DEPOSITSPECIMEN,new Lambda("DEPOSIT SPECIMEN ARM").addExecute(() -> {
                armPosition=0;
                adjustment=0;
            }));
            put(RobotState.PARKASCENT, new Lambda("PARK ASCENT ARM").addExecute(() -> {
                //This doesn't exist rn
            }));
            put(RobotState.PARKNOASCENT, new Lambda("PARK NO ASCENT ARM").addExecute(() -> {
                // Yeah nah not doing this
            }));
        }};
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
            new Parallel(
                new Lambda("TurnArm").addExecute(()-> {
                    if (Globals.updateRobotStateTrue == true) {
                        new Lambda("Run Change State for Arm").addExecute(() -> stateToCommandMap.get(Globals.getRobotState()));
                    }
                    rightArm.get().setPosition(armPosition+adjustment);
                    leftArm.get().setPosition(armPosition+adjustment);
                }));
        });
    }

    @NonNull
    public Lambda adjustArmUp() {
        return new Lambda("adjust wrist")
                .addExecute(()-> {
                    new Lambda("changeAdjustment").addExecute(()->adjustment=adjustment+0.01);
                    getTelemetry().addLine("Arm Adjusted");
                    getTelemetry().update();
                });
    }

    @NonNull
    public Lambda adjustArmDown() {
        return new Lambda("adjust wrist")
                .addExecute(()-> {
                    new Lambda("changeAdjustment").addExecute(()->adjustment=adjustment-0.01);
                    getTelemetry().addLine("Arm Adjusted");
                });
    }

    @NonNull
    public Lambda resetAdjustment() {
        return new Lambda("reset adjustment")
                .addExecute(() -> {
                    new Lambda("resetAdjustment").addExecute(()->adjustment=0);
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
