package org.firstinspires.ftc.teamcode.dairyFarm.subsytems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.camembert.cheeseFactory.Globals;
import org.firstinspires.ftc.teamcode.camembert.cheeseFactory.RobotState;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.cachinghardware.CachingCRServo;
import dev.frozenmilk.dairy.cachinghardware.CachingServo;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotations;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.commands.groups.Parallel;
import dev.frozenmilk.mercurial.commands.util.Wait;
import dev.frozenmilk.mercurial.subsystems.SDKSubsystem;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.util.cell.Cell;
import kotlin.annotation.MustBeDocumented;

public class Intake extends SDKSubsystem {
    public static final Intake INSTANCE = new Intake();

    private final Cell<CachingServo> intakeServo = subsystemCell(() -> new CachingServo(getHardwareMap().get(Servo.class, "SM")));

    private static double targetPos = 0;

    public static boolean intakeIsTrue = true;
    public static double speedOfRotation=1;

    public Intake() {}

    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        // Init sequence
        intakeServo.get().setDirection(Servo.Direction.REVERSE);

        targetPos = 0;

        getTelemetry().addLine("Intake Initalising");

        setDefaultCommand(turnClawWithState());

    }

    @Override
    public void preUserLoopHook(@NonNull Wrapper opMode) {
        getTelemetry().addData("Actual SM Servo Pos", intakeServo.get().getPosition());
        getTelemetry().addData("Target SM Servo Pos", targetPos);
    }

    public Lambda adjustClawForwards() {
        return new Lambda ("TESTING")
                .addExecute(()-> {
                    targetPos += 0.01;
                    intakeServo.get().setPosition(targetPos);
                    getTelemetry().addData("ServoPosition",intakeServo.get().getPosition());
                });
    }

    public Lambda adjustClawBackwards() {
        return new Lambda ("TESTING")
                .addExecute(()-> {
                    targetPos -= 0.01;
                    intakeServo.get().setPosition(targetPos);
                    getTelemetry().addData("ServoPosition",intakeServo.get().getPosition());
                });
    }

    @NonNull
    public Lambda turnClawWithState() {
        return new Lambda("Spin SM by State")
                .addRequirements(INSTANCE)
                .addExecute(() -> {
                    if (Globals.updateRobotStateTrue && !Globals.intakeAcceptState) {
                        Globals.intakeAcceptState = true;
                        switch (Globals.getRobotState()) {
                            case GRAB:
                                targetPos += 0.5; // Used to be 0.25
                                break;
                            case DEPOSIT:
                                //new Wait(800);
                                //targetPos = 0;
                                break;
                            case IDLE:
                                if (Globals.lastRobotState == RobotState.DEPOSIT) {
                                    targetPos = 0;
                                }
                            default:
                                // Keep targetPos the same
                                break;
                        }
                    }
                    intakeServo.get().setPosition(targetPos);
                });
    }

    @NonNull
    public Lambda turnClaw() {
        return new Lambda ("Turn Intake")
                .addExecute(()-> {
                    if (intakeIsTrue) {
                        intakeServo.get().setPosition(0.77); // 0.77
                    } else {
                        intakeServo.get().setPosition(0.52); // 0.52
                    }
                    getTelemetry().addLine("Intake Turning");
                });
    }

    @NonNull
    public Lambda toggleIntake() {
        return new Lambda ("Toggle Intake")
                .addExecute(()-> {
                    targetPos += 0.25;
                    getTelemetry().addLine("Intake Toggled");
                });
    }

    @NonNull
    public Lambda resetIntake() {
        return new Lambda("Reset Intake")
                .addRequirements(INSTANCE)
                .addExecute(() -> {
                   targetPos = 0;
                });
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @MustBeDocumented
    @Inherited

    public @interface Attach{}

    private Dependency<?> dependency = Subsystem.DEFAULT_DEPENDENCY.and(new SingleAnnotations<>(Intake.Attach.class));

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
