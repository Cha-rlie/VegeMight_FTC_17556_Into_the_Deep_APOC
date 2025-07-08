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

public class OLDINTAKE extends SDKSubsystem {

    public static final OLDINTAKE INSTANCE = new OLDINTAKE();

    private final Cell<CachingServo> clawServo = subsystemCell(() -> new CachingServo(getHardwareMap().get(Servo.class, "sampleManipulator")));
    private final Cell<CachingServo> clawRot = subsystemCell(()-> new CachingServo(getHardwareMap().get(Servo.class,"claw")));
    private final Cell<CachingServo> wristServo = subsystemCell(()-> new CachingServo(getHardwareMap().get(Servo.class, "wrist")));
    private final Cell<CachingServo> armWristServo = subsystemCell(()-> new CachingServo(getHardwareMap().get(Servo.class,"armWrist")));

    public boolean clawOpen = false;
    public double clawAdjustmentStage = 0;

    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        // Init sequence
        getTelemetry().addLine("Claw Initalising");

        setDefaultCommand(
                new Parallel(
                updateIntake(),
                rotateClaw()
                )
        );

    }

    @NonNull
    public Lambda updateIntake() {
        return new Lambda("turnwrist")
                .addRequirements(INSTANCE)
                .addExecute(()-> {
                            if (Globals.updateRobotStateTrue == true) {
                                switch (Globals.INSTANCE.getRobotState()) {
                                    case IDLE:
                                        wristServo.get().setPosition(0.12);
                                        armWristServo.get().setPosition(0);
                                        clawOpen=false;
                                        clawAdjustmentStage=0;
                                        break;
                                    case DEPOSIT:
                                        wristServo.get().setPosition(0.51);
                                        armWristServo.get().setPosition(0);
                                        clawOpen=false;
                                        clawAdjustmentStage=4;
                                        break;
                                    case HOVERAFTERGRAB:
                                        wristServo.get().setPosition(0.21);
                                        armWristServo.get().setPosition(0);
                                        clawOpen=false;
                                        clawAdjustmentStage=0;
                                        break;
                                    case HOVERBEFOREGRAB:
                                        wristServo.get().setPosition(0.21);
                                        armWristServo.get().setPosition(0);
                                        clawOpen=true;
                                        clawAdjustmentStage=0;
                                        break;
                                    case GRAB:
                                        wristServo.get().setPosition(0.21);
                                        armWristServo.get().setPosition(0);
                                        clawOpen=true;
                                        clawAdjustmentStage=0;
                                        break;
                                    default:
                                        wristServo.get().setPosition(0.12);
                                        armWristServo.get().setPosition(0);
                                        clawOpen=false;
                                        clawAdjustmentStage=0;
                                        break;
                                }
                            }
                        }
                );
    }

    @NonNull
    public Lambda adjustWristDown() {
        return new Lambda("adjust wrist")
                .addExecute(()-> {
                    wristServo.get().setPosition(wristServo.get().getPosition()-0.01);
                    getTelemetry().addLine("Wrist Adjusted");
                    getTelemetry().update();
                });
    }

    @NonNull
    public Lambda adjustWristUp() {
        return new Lambda("adjust wrist")
                .addExecute(()-> {
                    wristServo.get().setPosition(wristServo.get().getPosition()+0.01);
                    getTelemetry().addLine("Wrist Adjusted");
                    getTelemetry().update();
                });
    }

    @NonNull
    public Lambda adjustWristArmDown() {
        return new Lambda("adjust wrist")
                .addExecute(()-> {
                    armWristServo.get().setPosition(wristServo.get().getPosition()-0.01);
                    getTelemetry().addLine("Wrist Adjusted");
                    getTelemetry().update();
                });
    }

    @NonNull
    public Lambda adjustWristArmUp() {
        return new Lambda("adjust wrist")
                .addExecute(()-> {
                    armWristServo.get().setPosition(wristServo.get().getPosition()+0.01);
                    getTelemetry().addLine("Wrist Adjusted");
                    getTelemetry().update();
                });
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

    private Dependency<?> dependency = Subsystem.DEFAULT_DEPENDENCY.and(new SingleAnnotations<>(OLDINTAKE.Attach.class));

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
