package org.firstinspires.ftc.teamcode.pedroPathing.constants;

import com.pedropathing.localization.*;
import com.pedropathing.localization.constants.*;

public class LConstants {
    static {
        ThreeWheelConstants.forwardTicksToInches = (0.0019904669446149615+0.0019673056630080655+0.001996300138454484+0.0019706190275176195+0.001959027387379787)/5;
        ThreeWheelConstants.strafeTicksToInches = (-0.001960830642622272-0.00196217412057263-0.001955760572972816)/3;
        ThreeWheelConstants.turnTicksToInches =0.00202849478374536261446831678543;
        ThreeWheelConstants.leftY = 5.91;
        ThreeWheelConstants.rightY = -5.91;
        ThreeWheelConstants.strafeX = 4.72;
        ThreeWheelConstants.leftEncoder_HardwareMapName = "FR";
        ThreeWheelConstants.rightEncoder_HardwareMapName = "BL";
        ThreeWheelConstants.strafeEncoder_HardwareMapName = "FL";
        ThreeWheelConstants.leftEncoderDirection = Encoder.REVERSE;
        ThreeWheelConstants.rightEncoderDirection = Encoder.REVERSE;
        ThreeWheelConstants.strafeEncoderDirection = Encoder.REVERSE ;
    }
}




