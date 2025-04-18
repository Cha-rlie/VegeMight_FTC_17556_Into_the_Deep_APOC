package org.firstinspires.ftc.teamcode.dairyFarm.subsytems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.camembert.cheeseFactory.Globals;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.cachinghardware.CachingDcMotorEx;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotations;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.SDKSubsystem;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.util.cell.Cell;
import kotlin.annotation.MustBeDocumented;

public class Pitching extends SDKSubsystem {
    public static final Pitching INSTANCE = new Pitching();

    public final Cell<CachingDcMotorEx> pitchingMotor = subsystemCell(()-> new CachingDcMotorEx(getHardwareMap().get(DcMotorEx.class, "pitching")));

    private Pitching() {
    }
    //This has not been attatched yet
    //And hence will do nothing yet
    //Attach when get new robot
    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        getTelemetry().addLine("Pitching Initalising");

        setDefaultCommand(turnPitching());
    }

    @NonNull
    public Lambda turnPitching(){
        return new Lambda("Pitching")
                .addRequirements(INSTANCE)
                .addExecute(()-> {
                    switch (Globals.INSTANCE.getRobotState()) {
                        case IDLE:
                            pitchingMotor.get().setTargetPosition(1000 /* change */);
                            break;
                        case GRAB:
                            pitchingMotor.get().setTargetPosition(1000 /* change */);
                            break;
                        case HOVERAFTERGRAB:
                            pitchingMotor.get().setTargetPosition(1000 /* change */);
                            break;
                        case HOVERBEFOREGRAB:
                            pitchingMotor.get().setTargetPosition(1000 /* change */);
                            break;
                        case DEPOSIT:
                            pitchingMotor.get().setTargetPosition(1000 /* change */);
                            break;
                        default:
                            pitchingMotor.get().setTargetPosition(1000 /* change */);
                            break;
                    }

                    pitchingMotor.get().setMode(DcMotor.RunMode.RUN_TO_POSITION);
                });
    }

    @NonNull
    public Lambda adjustPitching(){
        return new Lambda("Adjust Pitching")
                .addExecute(()-> {
                    pitchingMotor.get().setTargetPosition(pitchingMotor.get().getCurrentPosition()+10);
                    pitchingMotor.get().setMode(DcMotor.RunMode.RUN_TO_POSITION);
                });

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @MustBeDocumented
    @Inherited
    public @interface Attach{}

    private Dependency<?> dependency = Subsystem.DEFAULT_DEPENDENCY.and(new SingleAnnotations<>(Pitching.Attach.class));

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
