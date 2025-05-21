package org.firstinspires.ftc.teamcode.dairyFarm.subsytems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

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
import dev.frozenmilk.util.cell.Cell;
import kotlin.annotation.MustBeDocumented;



public class Testing extends SDKSubsystem {
    public static final Testing INSTANCE= new Testing();

    public final Cell<CachingServo> Testing1 = subsystemCell(() -> new CachingServo(getHardwareMap().get(Servo.class, "testingRight")));
    public final Cell<CachingServo> Testing2 = subsystemCell(() -> new CachingServo(getHardwareMap().get(Servo.class, "testingLeft")));
    public final Cell<CachingDcMotorEx> Testing3 = subsystemCell(()->new CachingDcMotorEx(getHardwareMap().get(DcMotorEx.class, "testingRightMotor")));
    public final Cell<CachingDcMotorEx> Testing4 = subsystemCell(()-> new CachingDcMotorEx(getHardwareMap().get(DcMotorEx.class, "testingLeftMotor")));

    public double servoPos = 0.00;
    public int motorRTP = 0;

    public int motorPos1=0;
    public int motorPos2=0;
    public int saveState=0;
    public boolean pos1true = true;

    public Testing() {}

    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        // Init sequence
        getTelemetry().addLine("Testing Initalising");
        Testing2.get().setDirection(Servo.Direction.REVERSE);
        Testing4.get().setDirection(DcMotorEx.Direction.REVERSE);
        Testing3.get().setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        Testing4.get().setMode(DcMotorEx.RunMode.RUN_TO_POSITION);

        setDefaultCommand(
        new Parallel(
                adjustMotor(),
                adjustServo()
        ));
    }

    @NonNull
    public Lambda adjustServo(){
        return new Lambda("Turn Servo")
                .addExecute(()-> {
                    Testing1.get().setPosition(servoPos);
                    Testing2.get().setPosition(servoPos);
                });
    }

    @NonNull
    public Lambda adjustMotor(){
        return new Lambda("Turn Motor")
                .addExecute(()->{
                   Testing3.get().setTargetPosition(motorRTP);
                   Testing4.get().setTargetPosition(motorRTP);
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

    @NonNull
    public Lambda togglePos() {
        return new Lambda("Toggle")
                .addExecute(()->{
                    if (pos1true==true){
                        motorRTP=motorPos1;
                    } else {
                        motorRTP=motorPos2;
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
