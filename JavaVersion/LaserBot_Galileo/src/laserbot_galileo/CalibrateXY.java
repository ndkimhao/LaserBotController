/*
 *  Copyright Nguyen Duong Kim Hao @ 2014
 */
package laserbot_galileo;

import java.util.logging.Level;
import laserbot_galileo.io.GPIO;
import laserbot_galileo.io.StepperControl;

/**
 *
 * @author KimHao
 */
public class CalibrateXY {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        FileLog.begin();
        GPIO.begin();
        StepperControl.begin();
        StepperControl.findHome();

        StepperControl.STEP_DRAWLINE_X_DELAY = 500;
        StepperControl.STEP_DRAWLINE_Y_DELAY = 750;
        StepperControl.STEP_DRAWLINE_XY_DELAY = 600;
        FileLog.log(Level.INFO, "Begin CalibrateXY !");
        for (int i = 0; i < 1000; i++) {
            StepperControl.drawLine(14000, 6000);
            if (i % 10 == 0) {
                StepperControl.findHome();
            } else {
                StepperControl.drawLine(0, 0);
            }
        }
    }

}
