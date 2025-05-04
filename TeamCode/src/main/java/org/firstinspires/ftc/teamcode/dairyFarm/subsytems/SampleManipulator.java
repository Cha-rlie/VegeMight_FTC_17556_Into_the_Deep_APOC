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

public class SampleManipulator extends SDKSubsystem {

    public static final SampleManipulator INSTANCE = new SampleManipulator();

    private final Cell<CachingServo> clawServo = subsystemCell(() -> new CachingServo(getHardwareMap().get(Servo.class, "sampleManipulator")));
    private final Cell<CachingServo> clawRot = subsystemCell(()-> new CachingServo(getHardwareMap().get(Servo.class,"clawRot")));

    public boolean clawOpen = false;
    public double clawAdjustmentStage = 0;
    private HashMap<Object, Command> stateToCommandMap;

    private SampleManipulator(){
        stateToCommandMap = new HashMap<Object, Command>() {{
            put(RobotState.IDLE, new Lambda("IDLE ARM").addExecute(() -> {
                clawOpen=false;
                clawAdjustmentStage=0;
            }));
            put(RobotState.DEPOSIT, new Lambda("DEPOSIT ARM").addExecute(() -> {
                clawOpen=false;
                clawAdjustmentStage=4;
            }));
            put(RobotState.HOVERBEFOREGRAB, new Lambda("HBG ARM").addExecute(() -> {
                clawOpen=true;
                clawAdjustmentStage=0;
            }));
            put(RobotState.GRAB, new Lambda("GRAB ARM").addExecute(() -> {
                clawOpen=true;
                clawAdjustmentStage=0;;
            }));
            put(RobotState.HOVERAFTERGRAB, new Lambda("HAG ARM").addExecute(() -> {
                clawOpen=false;
                clawAdjustmentStage=0;
            }));
            put(RobotState.INTAKESPECIMEN, new Lambda("INTAKE SPECIMEN ARM").addExecute(() -> {
                clawOpen=true;
                clawAdjustmentStage=0;
            }));
            put(RobotState.DEPOSITSPECIMEN,new Lambda("DEPOSIT SPECIMEN ARM").addExecute(() -> {
                clawOpen=true;
                clawAdjustmentStage=4;
            }));
            put(RobotState.PARKASCENT, new Lambda("PARK ASCENT ARM").addExecute(() -> {
                //This doesn't exist rn
            }));
            put(RobotState.PARKNOASCENT, new Lambda("PARK NO ASCENT ARM").addExecute(() -> {
                // Yeah nah don't got time for this
            }));
        }};
    }

    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        // Init sequence
        getTelemetry().addLine("Claw Initalising");

        setDefaultCommand(
                new Parallel(
                updateClaw(),
                rotateClaw()
                )
        );

    }

    @NonNull
    public Lambda updateClaw() {
        return new Lambda("updateClaw")
                .addRequirements(INSTANCE)
                .addExecute(()-> {
                            if (Globals.updateRobotStateTrue == true) {
                                new Lambda("Run Change State for Claw").addExecute(() -> stateToCommandMap.get(Globals.getRobotState()));
                            }
                        }
                );
    }

    @NonNull
    public Lambda rotateClaw(){
        return new Lambda("rotateClaw")
                .addExecute(()->{
                    clawRot.get().setPosition(clawAdjustmentStage*0.15);
                });
    }

    @NonNull
    public Lambda toggleClaw() {
        return new Lambda ("Toggle Claw")
                .addExecute(()-> {
                    if (clawOpen == true) {
                        clawOpen = false;
                    } else if (clawOpen == false) {
                        clawOpen = true;
                    }
                    if (clawOpen == true) {
                        clawServo.get().setPosition(0.8);
                        //Turn forever
                    } else if (clawOpen != true) {
                        clawServo.get().setPosition(0.36);
                        //Don't turn
                    }
                    getTelemetry().addLine("Claw Toggled");
                    getTelemetry().update();
                });
    }

    @NonNull
    public Lambda rotateClawForwards(){
        return new Lambda("Rotate Claw")
                .addExecute(()->{
                    if (!(clawAdjustmentStage+1 >6 || clawAdjustmentStage+1 <0)){
                        clawAdjustmentStage=clawAdjustmentStage+1;
                    }
                });
    }

    @NonNull
    public Lambda rotateClawBackwards(){
        return new Lambda("Rotate Claw Back")
                .addExecute(()->{
                    if (!(clawAdjustmentStage-1 >6 || clawAdjustmentStage-1 <0)){
                        clawAdjustmentStage=clawAdjustmentStage-1;
                    }
                });
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @MustBeDocumented
    @Inherited

    public @interface Attach{}

    private Dependency<?> dependency = Subsystem.DEFAULT_DEPENDENCY.and(new SingleAnnotations<>(SampleManipulator.Attach.class));

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
