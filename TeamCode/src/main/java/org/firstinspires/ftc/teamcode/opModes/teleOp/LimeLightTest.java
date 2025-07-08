package org.firstinspires.ftc.teamcode.opModes.teleOp;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp(name="LimeLight Test", group="Testing OpModes")
public class LimeLightTest extends LinearOpMode {
    private Limelight3A limelight;

    double limelightHeight = 0;
    double limelightAngle = 0;
    double sampleHeight = 0;

    @Override
    public void runOpMode() throws InterruptedException
    {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        telemetry.setMsTransmissionInterval(11);

        limelight.pipelineSwitch(0);

        /*
         * Starts polling for data.
         */
        limelight.start();

        while (opModeIsActive()) {
            LLResult result = limelight.getLatestResult();
            if (result != null) {
                if (result.isValid()) {
                    telemetry.addData("tx", result.getTx());
                    telemetry.addData("ty", result.getTy());
                    telemetry.addData("est distance from target",(limelightHeight-sampleHeight)/Math.tan(Math.toRadians(limelightAngle+result.getTy())));
                }
            }
        }
    }
}
