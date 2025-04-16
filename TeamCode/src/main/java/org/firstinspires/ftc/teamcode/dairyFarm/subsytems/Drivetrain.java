package org.firstinspires.ftc.teamcode.dairyFarm.subsytems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.camembert.cheeseFactory.Globals;
import org.firstinspires.ftc.teamcode.camembert.driveStuff.MecanumDriveCalculator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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

public class Drivetrain extends SDKSubsystem {

    public static final Drivetrain INSTANCE = new Drivetrain();

    // Declare the motors and mecanum drive
    private final Cell<CachingDcMotor> motorFL = subsystemCell(() -> new CachingDcMotor(getHardwareMap().get(DcMotorEx.class, "frontL")));
    private final Cell<CachingDcMotor> motorFR = subsystemCell(() -> new CachingDcMotor(getHardwareMap().get(DcMotorEx.class, "frontR")));
    private final Cell<CachingDcMotor> motorBL = subsystemCell(() -> new CachingDcMotor(getHardwareMap().get(DcMotorEx.class, "backL")));
    private final Cell<CachingDcMotor> motorBR = subsystemCell(() -> new CachingDcMotor(getHardwareMap().get(DcMotorEx.class, "backR")));

    private static double velocityAdjuster;

    // Constructor that builds the drivetrain subsystem class
    private Drivetrain() {
    }

    // SubsystemObjectCells get eagerly reevaluated at the start of every OpMode, if this subsystem is attached
    // this means that we can always rely on motor to be correct and up-to-date for the current OpMode
    // this can also work with Calcified

    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        // Init sequence
        getTelemetry().addLine("Drivetrain Initalising");
        getTelemetry().update();

        motorFR.get().setDirection(DcMotorSimple.Direction.REVERSE);
        motorBR.get().setDirection(DcMotorSimple.Direction.REVERSE);
        motorFL.get().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFR.get().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBL.get().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBR.get().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        velocityAdjuster = 1;
        stopAllMotors();

        setDefaultCommand(drive());
    }

    @Override
    public void preUserLoopHook(@NonNull Wrapper opMode) {
        getTelemetry().addLine("Drivetrain Initalised");
        getTelemetry().update();
    }

    @NonNull
    public Lambda drive() {
        return new Lambda("drive")
                .addRequirements(INSTANCE)
                .addExecute(() -> {
                    double[] calculatedMotorSpeeds = MecanumDriveCalculator.calculateDriveSpeedsRobotCentric(Mercurial.gamepad1().leftStickX().state()*velocityAdjuster, Mercurial.gamepad1().leftStickY().state()*velocityAdjuster, Mercurial.gamepad1().rightStickX().state()*velocityAdjuster);
                    motorFL.get().setPower(calculatedMotorSpeeds[0]);
                    motorFR.get().setPower(calculatedMotorSpeeds[1]);
                    motorBL.get().setPower(calculatedMotorSpeeds[2]);
                    motorBR.get().setPower(calculatedMotorSpeeds[3]);
                });
    }

    @NonNull
    public Lambda stopAllMotors() {
        return new Lambda("Stop All Motors")
                .addRequirements(INSTANCE)
                .addExecute(() -> {
                    motorFL.get().setPower(0);
                    motorFR.get().setPower(0);
                    motorBL.get().setPower(0);
                    motorBR.get().setPower(0);
                });
    }

    public static double getVelocityAdjuster() {
        return INSTANCE.velocityAdjuster;
    }

    public static void updateVelocityAdjuster(float newAdjustor) {
        switch (Globals.getCurrentInstance().getCurrentRobotState()) {
            case IDLE:
                if (FeatureRegistrar.getActiveOpMode().gamepad1.right_trigger > 0.5) {
                    INSTANCE.velocityAdjuster = 0.7;
                } else {
                    INSTANCE.velocityAdjuster = 1.0;
                }
                break;
            case DEPOSIT:
                if (FeatureRegistrar.getActiveOpMode().gamepad1.right_trigger > 0.5) {
                    INSTANCE.velocityAdjuster = 0.7;
                } else {
                    INSTANCE.velocityAdjuster = 0.8;
                }
                break;
            default:
                if (FeatureRegistrar.getActiveOpMode().gamepad1.right_trigger > 0.5) {
                    INSTANCE.velocityAdjuster = 0.7;
                } else {
                    INSTANCE.velocityAdjuster = 0.5;
                }
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @MustBeDocumented
    @Inherited
    public @interface Attach{}

    private Dependency<?> dependency = Subsystem.DEFAULT_DEPENDENCY.and(new SingleAnnotations<>(Attach.class));

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
