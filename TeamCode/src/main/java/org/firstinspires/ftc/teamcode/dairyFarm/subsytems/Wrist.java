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
import dev.frozenmilk.mercurial.subsystems.SDKSubsystem;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.util.cell.Cell;
import kotlin.annotation.MustBeDocumented;

public class Wrist extends SDKSubsystem {
    public static final Wrist INSTANCE = new Wrist();

    public final Cell<CachingServo> wristServo= subsystemCell(() -> new CachingServo(getHardwareMap().get(Servo.class, "wristRot")));

    private Wrist(){
    }

    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        // Init sequence
        getTelemetry().addLine("Wrist Initalising");

        setDefaultCommand(turnWrist());
    }

    @NonNull
    public Lambda turnWrist() {
        return new Lambda("turnwrist")
                .addRequirements(INSTANCE)
                .addExecute(()-> {
                            if (Globals.updateRobotStateTrue == true) {
                                switch (Globals.INSTANCE.getRobotState()) {
                                    case IDLE:
                                        wristServo.get().setPosition(0.12);
                                        break;
                                    case DEPOSIT:
                                        wristServo.get().setPosition(0.51);
                                        break;
                                    case HOVERAFTERGRAB:
                                        wristServo.get().setPosition(0.21);
                                        break;
                                    case HOVERBEFOREGRAB:
                                        wristServo.get().setPosition(0.21);
                                        break;
                                    case GRAB:
                                        wristServo.get().setPosition(0.21);
                                        break;
                                    case INTAKESPECIMEN:
                                        wristServo.get().setPosition(0.55);
                                        break;
                                    case NULL:
                                        break;
                                    default:
                                        wristServo.get().setPosition(0.12);
                                        break;
                                }
                            }
                        }
                );
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
    public Lambda intakeSpecimenSequence(){
        return new Lambda("Wrist Specimen")
                .addExecute(()->{
                   wristServo.get().setPosition(0.79);
                });
    }

    @NonNull
    public Lambda specimenDepositSequence(){
        return new Lambda("Wrist Specimen")
                .addExecute(()->{
                   wristServo.get().setPosition(0.9);
                });
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

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @MustBeDocumented
    @Inherited

    public @interface Attach{}

    private Dependency<?> dependency = Subsystem.DEFAULT_DEPENDENCY.and(new SingleAnnotations<>(Wrist.Attach.class));

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
