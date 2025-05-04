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
import dev.frozenmilk.mercurial.subsystems.SDKSubsystem;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.util.cell.Cell;
import kotlin.annotation.MustBeDocumented;

public class Wrist extends SDKSubsystem {
    public static final Wrist INSTANCE = new Wrist();

    public final Cell<CachingServo> wristServo= subsystemCell(() -> new CachingServo(getHardwareMap().get(Servo.class, "wristRot")));
    private HashMap<Object, Command> stateToCommandMap;

    private Wrist(){
        stateToCommandMap = new HashMap<Object, Command>() {{
            put(RobotState.IDLE, new Lambda("IDLE WRIST").addExecute(() -> wristServo.get().setPosition(0.12)));
            put(RobotState.DEPOSIT, new Lambda("DEPOSIT WRIST").addExecute(() -> wristServo.get().setPosition(0.51)));
            put(RobotState.HOVERBEFOREGRAB, new Lambda("HBG WRIST").addExecute(() -> wristServo.get().setPosition(0.21)));
            put(RobotState.GRAB, new Lambda("GRAB WRIST").addExecute(() -> wristServo.get().setPosition(0.21)));
            put(RobotState.HOVERAFTERGRAB, new Lambda("HAG WRIST").addExecute(() -> wristServo.get().setPosition(0.21)));
            put(RobotState.INTAKESPECIMEN, new Lambda("INTAKE SPECIMEN WRIST").addExecute(() -> wristServo.get().setPosition(0.55)));
            put(RobotState.DEPOSITSPECIMEN,new Lambda("DEPOSIT SPECIMEN WRIST").addExecute(() -> wristServo.get().setPosition(0.12)));
            put(RobotState.PARKASCENT, new Lambda("PARK ASCENT WRIST").addExecute(() -> wristServo.get().setPosition(0.12)));
            put(RobotState.PARKNOASCENT, new Lambda("PARK NO ASCENT WRIST").addExecute(() -> wristServo.get().setPosition(0.12)));
        }};
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
                                new Lambda("Run Change State for Wrist").addExecute(() -> stateToCommandMap.get(Globals.getRobotState()));
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
