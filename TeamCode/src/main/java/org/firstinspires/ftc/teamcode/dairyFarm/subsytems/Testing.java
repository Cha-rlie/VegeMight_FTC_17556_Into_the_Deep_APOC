package org.firstinspires.ftc.teamcode.dairyFarm.subsytems;

import android.os.TestLooperManager;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.opModes.teleOp.TestOpMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.cachinghardware.CachingDcMotorEx;
import dev.frozenmilk.dairy.cachinghardware.CachingServo;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotations;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.commands.groups.Parallel;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import dev.frozenmilk.mercurial.subsystems.SDKSubsystem;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.sinister.util.flag.Test;
import dev.frozenmilk.util.cell.Cell;
import kotlin.annotation.MustBeDocumented;



public class Testing extends SDKSubsystem {
    public static final Testing INSTANCE = new Testing();

    public final Cell<CachingServo> Testing1 = subsystemCell(() -> new CachingServo(getHardwareMap().get(Servo.class, "tR"/*testingRight*/)));
    public final Cell<CachingServo> Testing2 = subsystemCell(() -> new CachingServo(getHardwareMap().get(Servo.class, "tL"/*testingLeft*/)));
    public final Cell<CachingDcMotorEx> Testing3 = subsystemCell(()->new CachingDcMotorEx(getHardwareMap().get(DcMotorEx.class, "tRM")/*testingRightMotor*/));
    public final Cell<CachingDcMotorEx> Testing4 = subsystemCell(()-> new CachingDcMotorEx(getHardwareMap().get(DcMotorEx.class, "tLM")/*testingLeftMotor*/));



    public boolean pos1true = true;
    public boolean servopos1true = true;
    public Testing() {}

    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        // Init sequence
        getTelemetry().addLine("Testing Initalising");
        Testing3.get().resetDeviceConfigurationForOpMode();
        Testing4.get().resetDeviceConfigurationForOpMode();
        Testing3.get().setTargetPositionTolerance(50);
        Testing4.get().setTargetPositionTolerance(50);
        Testing3.get().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Testing4.get().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Testing2.get().setDirection(Servo.Direction.REVERSE);
        Testing3.get().setDirection(DcMotorEx.Direction.REVERSE);
        Testing3.get().setTargetPosition(300);
        Testing4.get().setTargetPosition(300);
        Testing3.get().setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        Testing4.get().setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        Testing3.get().setPower(1);
        Testing4.get().setPower(1);
    }
    @Override
    public void preUserLoopHook(@NonNull Wrapper opMode) {
        getTelemetry().addData("Motor Position",Testing3.get().getCurrentPosition());
        getTelemetry().addData("Motor Position",Testing3.get().getTargetPosition());
        getTelemetry().addData("Motor Position",Testing3.get().getPower());
    }

    @NonNull
    public Lambda adjustServo(double Adjustment){
        return new Lambda("Turn Servo")
                .addRequirements(INSTANCE)
                .addExecute(()-> {
                    if(servopos1true) {
                        Testing1.get().setPosition(0.2);
                        servopos1true=false;
                    } else {
                        Testing1.get().setPosition(0.4);
                        servopos1true=true;
                    }
                });
    }

    @NonNull
    public Lambda adjustMotor(int motorRTP){
        return new Lambda("Turn Motor")
                .addRequirements(INSTANCE)
                .addExecute(()->{
                    Testing3.get().setTargetPosition(motorRTP);
                    Testing4.get().setTargetPosition(motorRTP);
                    Testing3.get().setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
                    Testing4.get().setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
                    Testing3.get().setPower(1);
                    Testing4.get().setPower(1);

                   getTelemetry().addData("Motor Position",Testing3.get().getCurrentPosition());
                });
    }
    @NonNull
    public Lambda toggleMotor(){
        return new Lambda("Turn Motor")
                .addRequirements(INSTANCE)
                .addExecute(()->{
                    if(pos1true) {
                        Testing3.get().setTargetPosition(0);
                        Testing4.get().setTargetPosition(0);
                        Testing3.get().setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
                        Testing4.get().setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
                        Testing3.get().setPower(1);
                        Testing4.get().setPower(1);
                        pos1true=false;
                    } else {
                        Testing3.get().setTargetPosition(500);
                        Testing4.get().setTargetPosition(500);
                        Testing3.get().setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
                        Testing4.get().setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
                        Testing3.get().setPower(1);
                        Testing4.get().setPower(1);
                        pos1true=true;
                    }

                    getTelemetry().addData("Motor Position",Testing3.get().getCurrentPosition());
                });
    }



    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @MustBeDocumented
    @Inherited
    public @interface Attach{}

    private Dependency<?> dependency = Subsystem.DEFAULT_DEPENDENCY.and(new SingleAnnotations<>(Testing.Attach.class));

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
