package org.firstinspires.ftc.teamcode.dairyFarm.subsytems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.camembert.cheeseFactory.Globals;
import org.firstinspires.ftc.teamcode.camembert.driveStuff.MecanumDriveCalculator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.cachinghardware.CachingCRServo;
import dev.frozenmilk.dairy.cachinghardware.CachingServo;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.dairy.core.FeatureRegistrar;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotations;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.SDKSubsystem;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.dairy.cachinghardware.CachingDcMotor;
import dev.frozenmilk.util.cell.Cell;
import kotlin.annotation.MustBeDocumented;


public class Arm extends SDKSubsystem {

    public static final Arm INSTANCE = new Arm();

    public final Cell<CachingServo> leftArm = subsystemCell(() -> new CachingServo(getHardwareMap().get(Servo.class, "leftarm")));
    public final Cell<CachingServo> rightArm = subsystemCell(() -> new CachingServo(getHardwareMap().get(Servo.class, "rightarm")));

    private Arm(){
    }

    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        // Init sequence
        getTelemetry().addLine("Arm Initalising");
        getTelemetry().update();
        leftArm.get().setDirection(Servo.Direction.REVERSE);

        setDefaultCommand(turnArm());
    }

    @NonNull
    public Lambda turnArm(){
        return new Lambda("TurnArm")
        .addRequirements(INSTANCE)
        .addExecute(() -> {
            switch (Globals.getCurrentInstance().getCurrentRobotState()) {
                case IDLE:
                    leftArm.get().setPosition(0);
                    rightArm.get().setPosition(0);
                    break;
                case DEPOSIT:
                    leftArm.get().setPosition(0.237);
                    rightArm.get().setPosition(0.237);
                    break;
                case HOVERAFTERGRAB:
                    leftArm.get().setPosition(0.4942);
                    rightArm.get().setPosition(0.4942);
                    break;
                case HOVERBEFOREGRAB:
                    leftArm.get().setPosition(0.4942);
                    rightArm.get().setPosition(0.4942);
                    break;
                case GRAB:
                    leftArm.get().setPosition(0.5723);
                    rightArm.get().setPosition(0.5723);
                    break;
                default:
                    break;
            }
        });
    }

    @NonNull
    public Lambda adjustArmUp() {
        return new Lambda("adjust wrist")
                .addExecute(()-> {
                    leftArm.get().setPosition(leftArm.get().getPosition()+0.01);
                    rightArm.get().setPosition(rightArm.get().getPosition()+0.01);
                    getTelemetry().addLine("Arm Adjusted");
                    getTelemetry().update();
                });
    }

    @NonNull
    public Lambda adjustArmDown() {
        return new Lambda("adjust wrist")
                .addExecute(()-> {
                    leftArm.get().setPosition(leftArm.get().getPosition()-0.01);
                    rightArm.get().setPosition(rightArm.get().getPosition()+0.01);
                    getTelemetry().addLine("Arm Adjusted");
                    getTelemetry().update();
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
