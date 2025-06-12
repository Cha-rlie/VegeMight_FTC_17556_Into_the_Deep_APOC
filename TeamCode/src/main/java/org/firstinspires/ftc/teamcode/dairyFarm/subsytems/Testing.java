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
    public static final Testing INSTANCE= new Testing();

    public final Cell<CachingServo> Testing1 = subsystemCell(() -> new CachingServo(getHardwareMap().get(Servo.class, "tR"/*testingRight*/)));
    public final Cell<CachingServo> Testing2 = subsystemCell(() -> new CachingServo(getHardwareMap().get(Servo.class, "tL"/*testingLeft*/)));
    public final Cell<CachingDcMotorEx> Testing3 = subsystemCell(()->new CachingDcMotorEx(getHardwareMap().get(DcMotorEx.class, "tRM")/*testingRightMotor*/));
    public final Cell<CachingDcMotorEx> Testing4 = subsystemCell(()-> new CachingDcMotorEx(getHardwareMap().get(DcMotorEx.class, "tLM")/*testingLeftMotor*/));


    public int motorPos1=0;
    public int motorPos2=750;
    public int saveState=0;
    public boolean pos1true = true;
    public boolean servopos1true = true;
    public Testing() {}

    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        // Init sequence
        getTelemetry().addLine("Testing Initalising");
        Testing2.get().setDirection(Servo.Direction.REVERSE);
        Testing4.get().setDirection(DcMotorEx.Direction.REVERSE);
        Testing3.get().setTargetPosition(0);
        Testing4.get().setTargetPosition(0);
        Testing3.get().setPower(1);
        Testing4.get().setPower(1);
        Testing3.get().setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        Testing4.get().setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
    }

    @NonNull
    public Lambda adjustServo(double Adjustment){
        return new Lambda("Turn Servo")
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
                .addExecute(()->{
                   Testing3.get().setPower(1);
                   Testing4.get().setPower(1);
                   Testing3.get().setTargetPosition(motorRTP);
                   Testing4.get().setTargetPosition(motorRTP);

                   getTelemetry().addData("Motor Position",Testing3.get().getCurrentPosition());
                });
    }
    @NonNull
    public Lambda toggleMotor(){
        return new Lambda("Turn Motor")
                .addExecute(()->{
                    if(pos1true) {
                        Testing3.get().setPower(1);
                        Testing4.get().setPower(1);
                        Testing3.get().setTargetPosition(0);
                        Testing4.get().setTargetPosition(0);
                        pos1true=false;
                    } else {
                        Testing3.get().setPower(1);
                        Testing4.get().setPower(1);
                        Testing3.get().setTargetPosition(700);
                        Testing4.get().setTargetPosition(700);
                        pos1true=true;
                    }

                    getTelemetry().addData("Motor Position",Testing3.get().getCurrentPosition());
                });
    }

    @NonNull
    public Lambda saveState(){
        return new Lambda("Turn Motor")
                .addExecute(()->{
                    if(saveState==0) {
                        motorPos1= Testing3.get().getCurrentPosition();
                        saveState=1;
                    } else {
                        motorPos2= Testing3.get().getCurrentPosition();
                        saveState=0;
                    }
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
