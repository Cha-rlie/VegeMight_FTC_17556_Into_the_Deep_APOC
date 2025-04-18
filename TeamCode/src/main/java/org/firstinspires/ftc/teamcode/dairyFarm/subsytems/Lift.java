package org.firstinspires.ftc.teamcode.dairyFarm.subsytems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.camembert.cheeseFactory.Globals;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotations;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.SDKSubsystem;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.dairy.cachinghardware.CachingDcMotor;
import dev.frozenmilk.util.cell.Cell;
import kotlin.annotation.MustBeDocumented;


public class Lift extends SDKSubsystem {
    public static final Lift INSTANCE = new Lift();

    // Declare the motors and mecanum drive
    // @charlie please check motor names
    private final Cell<CachingDcMotor> motorLiftL = subsystemCell(() -> new CachingDcMotor(getHardwareMap().get(DcMotorEx.class, "leftSpool")));
    private final Cell<CachingDcMotor> motorLiftR = subsystemCell(() -> new CachingDcMotor(getHardwareMap().get(DcMotorEx.class, "rightSpool")));

    private Lift() {
    }

    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        // Init sequence
        getTelemetry().addLine("Slides Initalising");

        motorLiftL.get().setDirection(DcMotorSimple.Direction.REVERSE);
        motorLiftR.get().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorLiftL.get().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        stopAllMotors();
        setDefaultCommand(goToPosition());

    }

    @NonNull
    public Lambda stopAllMotors() {
        return new Lambda("Stop All Motors")
                .addRequirements(INSTANCE)
                .addExecute(() -> {
                    motorLiftL.get().setTargetPosition(0);
                    motorLiftL.get().setTargetPosition(0);
                    motorLiftL.get().setPower(1);
                    motorLiftR.get().setPower(1);
                });
    }


    @NonNull
    public Lambda goToPosition() {
        return new Lambda("ChangePosition")
                .addRequirements(INSTANCE)
                .addExecute(() -> {
                    switch (Globals.INSTANCE.getRobotState()) {
                        case IDLE:
                            motorLiftL.get().setTargetPosition(0);
                            motorLiftR.get().setTargetPosition(0);
                            break;
                        case DEPOSIT:
                            motorLiftL.get().setTargetPosition(1700);
                            motorLiftR.get().setTargetPosition(1700);
                            break;
                        default:
                            motorLiftL.get().setTargetPosition(0);
                            motorLiftR.get().setTargetPosition(0);
                    }

                    motorLiftL.get().setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    motorLiftR.get().setMode(DcMotor.RunMode.RUN_TO_POSITION);
                });
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @MustBeDocumented
    @Inherited
    public @interface Attach{}

    private Dependency<?> dependency = Subsystem.DEFAULT_DEPENDENCY.and(new SingleAnnotations<>(Lift.Attach.class));

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

