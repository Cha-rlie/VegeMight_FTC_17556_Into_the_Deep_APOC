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

public class SampleManipulator extends SDKSubsystem {

    public static final SampleManipulator INSTANCE = new SampleManipulator();
    public boolean clawOpen = false;
    public double clawAdjustmentStage = 0;
    private final Cell<CachingServo> clawServo = subsystemCell(() -> new CachingServo(getHardwareMap().get(Servo.class, "sampleManipulator")));
    private final Cell<CachingServo> clawRot = subsystemCell(()-> new CachingServo(getHardwareMap().get(Servo.class,"clawRot")));
    //Use CRServo for Hybrid
    //private final Cell<CachingCRServo> clawServo = subsystemCell(() -> new CachingCRServo(getHardwareMap().get(CRServo.class, "frontL")));
    //For old bot sample and specimen manip is the same

    private SampleManipulator(){
    }

    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        // Init sequence
        getTelemetry().addLine("Claw Initalising");

        setDefaultCommand(
                new Parallel(
                openCloseClaw(),
                defaultClaw()
                )
        );

    }


    @NonNull
    public Lambda defaultClaw() {
        return new Lambda ("Default Claw Command")
                .addExecute(()-> {
                    clawRot.get().setPosition(0 + clawAdjustmentStage*0.15);
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

    @NonNull
    public Lambda resetClawRotAutomatically(){
        return new Lambda("Reset Adjustment")
                .addExecute(()->{
                    switch (Globals.INSTANCE.getRobotState()) {
                        case IDLE:
                            clawAdjustmentStage=0;
                            break;
                        case DEPOSIT:
                            clawAdjustmentStage=0;
                            break;
                        case DEPOSITSPECIMEN:
                            clawAdjustmentStage=4;
                            break;
                        case INTAKESPECIMEN:
                            clawAdjustmentStage=0;
                            break;
                        default:
                            break;
                    }
                });
    }

    @NonNull
    public Lambda openCloseClaw() {
        return new Lambda("Claw")
                .addRequirements(INSTANCE)
                .addExecute(()-> {
                    if (Globals.updateRobotStateTrue == true) {
                    switch (Globals.INSTANCE.getRobotState()) {
                        case IDLE:
                            clawOpen = true;
                            break;
                        case GRAB:
                            clawOpen = true;
                            break;
                        case DEPOSIT:
                            clawOpen = true;
                            break;
                        case HOVERAFTERGRAB:
                            clawOpen = true;
                            break;
                        default:
                            clawOpen = false;
                            break;
                    }
                    if (clawOpen == true) {
                        clawServo.get().setPosition(0.8);
                        //Turn forever
                    } else if (clawOpen != true) {
                        clawServo.get().setPosition(0.36);
                        //Don't turn
                    }
                }
                });
    }

    @NonNull
    public Lambda intakeSpecimenSequence(){
        return new Lambda ("Claw Intake")
                .addExecute(()->{
                    clawOpen=false;
                    clawServo.get().setPosition(0.8);
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
