package org.firstinspires.ftc.teamcode.pedroPathing.constants;

import com.pedropathing.localization.*;
import com.pedropathing.localization.constants.*;

public class LConstants {
    static {
        ThreeWheelConstants.forwardTicksToInches = .001989436789;
        ThreeWheelConstants.strafeTicksToInches = .001989436789;
        ThreeWheelConstants.turnTicksToInches = .001989436789;
        ThreeWheelConstants.leftY = 5.91;
        ThreeWheelConstants.rightY = -5.91;
        ThreeWheelConstants.strafeX = 4.72;
        ThreeWheelConstants.leftEncoder_HardwareMapName = "frontL";
        ThreeWheelConstants.rightEncoder_HardwareMapName = "backR";
        ThreeWheelConstants.strafeEncoder_HardwareMapName = "backL";
        ThreeWheelConstants.leftEncoderDirection = Encoder.FORWARD;
        ThreeWheelConstants.rightEncoderDirection = Encoder.REVERSE;
        ThreeWheelConstants.strafeEncoderDirection = Encoder.REVERSE;
    }
}




