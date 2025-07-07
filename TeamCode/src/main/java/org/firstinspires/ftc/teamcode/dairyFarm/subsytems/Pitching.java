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
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import dev.frozenmilk.mercurial.commands.util.IfElse;
import dev.frozenmilk.mercurial.commands.util.StateMachine;
import dev.frozenmilk.mercurial.subsystems.SDKSubsystem;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.util.cell.Cell;
import dev.frozenmilk.util.cell.RefCell;
import kotlin.annotation.MustBeDocumented;

public class Pitching extends SDKSubsystem {
    public static final Pitching INSTANCE = new Pitching();

    public final Cell<CachingDcMotorEx> pitchingMotor = subsystemCell(()-> new CachingDcMotorEx(getHardwareMap().get(DcMotorEx.class, "P")));
    private HashMap<Object, Command> stateToCommandMap;
    private StateMachine<RobotState> stateToCommandMachine;
    private HashMap<RobotState, Integer> stateToValueMap;
    public int runToPos = 0;

    private Pitching() {
        /*stateToCommandMachine = new StateMachine<>(RobotState.IDLE)
                .withState(RobotState.IDLE, (RefCell<RobotState> state, String name) -> new Lambda("IDLE WRIST").addEnd(interrupted -> runToPos = 0))
                .withState(RobotState.DEPOSIT, (state, name) -> new Lambda(name).addEnd(interrupted -> runToPos = 0))
                .withState(RobotState.HOVERBEFOREGRAB, (state, name) -> new Lambda(name).addEnd(interrupted -> runToPos = 700))
                .withState(RobotState.GRAB, (state, name) -> new Lambda(name).addEnd(interrupted -> runToPos = 700))
                .withState(RobotState.HOVERAFTERGRAB, (state, name) -> new Lambda(name).addEnd(interrupted -> runToPos = 700))
                .withState(RobotState.SPECHOVER, (state, name) -> new Lambda(name).addEnd(interrupted -> runToPos = 0))
                .withState(RobotState.SPECGRAB, (state, name) -> new Lambda(name).addEnd(interrupted -> runToPos = 0))
                .withState(RobotState.DEPOSITSPECIMEN, (state, name) -> new Lambda(name).addEnd(interrupted -> runToPos = 0))
                .withState(RobotState.PARKASCENT, (state, name) -> new Lambda(name).addEnd(interrupted -> runToPos = 0))
                .withState(RobotState.PARKNOASCENT, (state, name) -> new Lambda(name).addEnd(interrupted -> runToPos = 0));

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
        }};*/

        stateToValueMap = new HashMap<RobotState, Integer>() {{
            put(RobotState.IDLE, 0);
            put(RobotState.DEPOSIT, 0);
            put(RobotState.HOVERBEFOREGRAB, 830);
            put(RobotState.GRAB, 830);
            put(RobotState.HOVERAFTERGRAB, 830);
            put(RobotState.SPECHOVER, 0);
            put(RobotState.SPECGRAB, 0);
            put(RobotState.DEPOSITSPECIMEN, 0);
            put(RobotState.PARKASCENT, 0);
            put(RobotState.PARKNOASCENT, 0);
        }};
    }

    //Attach Laterrrr
    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        getTelemetry().addLine("Pitching Initalising");
        pitchingMotor.get().setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        pitchingMotor.get().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        pitchingMotor.get().setTargetPositionTolerance(50);
        pitchingMotor.get().setTargetPosition(0);
        pitchingMotor.get().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        pitchingMotor.get().setPower(1);
        runToPos = 0;
        setDefaultCommand(turnPitching());
    }

    @Override
    public void preUserLoopHook(@NonNull Wrapper opMode) {
        getTelemetry().addData("Pitch Position", pitchingMotor.get().getCurrentPosition());
        getTelemetry().addData("Pitching RTP", runToPos);
    }

    @NonNull
    public Sequential turnPitching(){
        return new Sequential(
                new IfElse(
                        () -> Globals.updateRobotStateTrue && !Globals.pitchAcceptState,
                        new Lambda("Run Change State for Wrist").setExecute(() -> {
                            if (stateToValueMap.containsKey(Globals.getRobotState())) {
                                if (Globals.getRobotState() == RobotState.IDLE) {
                                    if (Globals.lastRobotState == RobotState.GRAB || Globals.lastRobotState == RobotState.HOVERBEFOREGRAB || Globals.lastRobotState == RobotState.HOVERAFTERGRAB) {
                                        if (Lift.INSTANCE.notAdjusted()) {
                                            runToPos = stateToValueMap.get(Globals.getRobotState());
                                        }
                                    }
                                } else {
                                    runToPos = stateToValueMap.get(Globals.getRobotState());
                                }
                            } else {
                                runToPos = stateToValueMap.get(RobotState.IDLE);
                            }
                            Globals.pitchAcceptState = true;
                        }),
                        new Lambda("EMPTY")
                ),
                new Lambda("Pitching")
                .addRequirements(INSTANCE)
                .addExecute(()-> {
                    pitchingMotor.get().setTargetPosition(runToPos);
                    pitchingMotor.get().setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
                    pitchingMotor.get().setPower(1);
                })
        );
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
