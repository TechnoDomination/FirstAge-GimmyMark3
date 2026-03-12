package org.firstinspires.ftc.teamcode.Util;

public class SmoothGamepad {

    public double smoothGamepadInput;

    public double smoothGamepad(double gamepadInput){

        if (gamepadInput > 0) {
            return  Math.pow(gamepadInput, 2.2);
        } else {
            return  -1 * Math.pow(-gamepadInput, 2.2);
        }
    }
}
