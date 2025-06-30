package org.firstinspires.ftc.teamcode.dairyFarm.subsytems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.camembert.cheeseFactory.Globals;
import org.firstinspires.ftc.teamcode.camembert.cheeseFactory.RobotState;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;

import dev.frozenmilk.dairy.cachinghardware.CachingDcMotorEx;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotations;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Command;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.SDKSubsystem;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.util.cell.Cell;
import kotlin.annotation.MustBeDocumented;

public class Pitching extends SDKSubsystem {
    public static final Pitching INSTANCE = new Pitching();

    public final Cell<CachingDcMotorEx> pitchingMotor = subsystemCell(()-> new CachingDcMotorEx(getHardwareMap().get(DcMotorEx.class, "P")));
    private HashMap<Object, Command> stateToCommandMap;
    public int runToPos = 0;

    private Pitching() {
        stateToCommandMap = new HashMap<Object, Command>() {{
            put(RobotState.IDLE, new Lambda("IDLE WRIST").addExecute(() -> runToPos = 0));
            put(RobotState.DEPOSIT, new Lambda("DEPOSIT WRIST").addExecute(() -> runToPos = 0));
            put(RobotState.HOVERBEFOREGRAB, new Lambda("HBG WRIST").addExecute(() -> runToPos = 700));
            put(RobotState.GRAB, new Lambda("GRAB WRIST").addExecute(() -> runToPos = 700));
            put(RobotState.HOVERAFTERGRAB, new Lambda("HAG WRIST").addExecute(() -> runToPos = 700));
            put(RobotState.SPECHOVER, new Lambda("INTAKE SPECIMEN WRIST").addExecute(() -> runToPos = 0));
            put(RobotState.SPECGRAB, new Lambda("INTAKE SPECIMEN WRIST").addExecute(() -> runToPos = 0));
            put(RobotState.DEPOSITSPECIMEN, new Lambda("DEPOSIT SPECIMEN WRIST").addExecute(() -> runToPos = 0));
            put(RobotState.PARKASCENT, new Lambda("PARK ASCENT WRIST").addExecute(() -> runToPos = 0));
            put(RobotState.PARKNOASCENT, new Lambda("PARK NO ASCENT WRIST").addExecute(() -> runToPos = 0));
        }};
    }

    //Attach Laterrrr
    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        getTelemetry().addLine("Pitching Initalising");
        pitchingMotor.get().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        pitchingMotor.get().setTargetPositionTolerance(50);
        pitchingMotor.get().setTargetPosition(0);
        pitchingMotor.get().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        pitchingMotor.get().setPower(1);
        setDefaultCommand(turnPitching());
    }

    @Override
    public void preUserLoopHook(@NonNull Wrapper opMode) {
        getTelemetry().addData("Pitch Position", pitchingMotor.get().getCurrentPosition());
        getTelemetry().addData("Pitching RTP",runToPos);
    }

    @NonNull
    public Lambda turnPitching(){
        return new Lambda("Pitching")
                .addRequirements(INSTANCE)
                .addExecute(()-> {
                    if (Globals.updateRobotStateTrue == true) {
                        new Lambda("Run Change State for Wrist").addExecute(() -> stateToCommandMap.get(Globals.getRobotState()));
                    }
                    pitchingMotor.get().setTargetPosition(runToPos);
                    pitchingMotor.get().setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    pitchingMotor.get().setPower(1);
                });
    }

    @NonNull
    public Lambda adjustPitchingUp(){
        return new Lambda("Adjust Pitching")
                .addExecute(()-> {
                    runToPos += 50;
                });

    }

    @NonNull
    public Lambda adjustPitchingDown(){
        return new Lambda("Adjust Pitching")
                .addExecute(()-> {
                    runToPos -= 50;
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
