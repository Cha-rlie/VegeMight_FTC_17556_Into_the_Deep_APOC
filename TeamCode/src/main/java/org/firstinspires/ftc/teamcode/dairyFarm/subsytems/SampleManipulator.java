package org.firstinspires.ftc.teamcode.dairyFarm.subsytems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.camembert.cheeseFactory.Globals;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.cachinghardware.CachingCRServo;
import dev.frozenmilk.dairy.cachinghardware.CachingDcMotor;
import dev.frozenmilk.dairy.cachinghardware.CachingServo;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotations;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.SDKSubsystem;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.util.cell.Cell;
import kotlin.annotation.MustBeDocumented;

public class SampleManipulator extends SDKSubsystem {

    public static final SampleManipulator INSTANCE = new SampleManipulator();
    public boolean clawOpen = false;
    private final Cell<CachingServo> clawServo = subsystemCell(() -> new CachingServo(getHardwareMap().get(Servo.class, "frontL")));
    //Use CRServo for Hybrid
    //private final Cell<CachingCRServo> clawServo = subsystemCell(() -> new CachingCRServo(getHardwareMap().get(CRServo.class, "frontL")));
    //For old bot sample and specimen manip is the same

    private SampleManipulator(){
    }

    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        // Init sequence
        getTelemetry().addLine("Claw Initalising");
        getTelemetry().update();

        setDefaultCommand(openCloseClaw());

    }

    @NonNull
    public Lambda openCloseClaw() {
        return new Lambda("Claw")
                .addRequirements(INSTANCE)
                .addExecute(()-> {
                    switch (Globals.getCurrentInstance().getCurrentRobotState()) {
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
