/*
 *  Copyright Nguyen Duong Kim Hao @ 2014
 */
package laserbot_galileo.svgPlotter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import static laserbot_galileo.Common.*;
import laserbot_galileo.FileLog;
import laserbot_galileo.Main;
import static laserbot_galileo.io.GPIO.digitalRead;
import laserbot_galileo.io.MegaCommand;
import laserbot_galileo.io.StepperControl;
import static laserbot_galileo.io.StepperControl.*;

/**
 *
 * @author KimHao
 */
public class SVGPlotter {

    private static Thread thread;
    private static short[] xyControl;
    public static int distance;

    static class SVGPlotterThread extends Thread {

        @Override
        public void run() {
            priority(MAX_PRIORITY);
            laser(OFF);
            int lastCalibrate = 0;
            for (int i = 0; i < xyControl.length;) {
                short tmp = xyControl[i++];
                if (tmp == -1) {
                    laser(OFF);
                    if (digitalRead(STEPPER_Y_LIMIT) == BUTTON_PRESS) {
                        if (StepperControl.curY >= MIN_Y) {
                            MegaCommand.sendCommand(COMMAND_DRAWING_BREAK);
                            delay(BUTTON_DELAY);

                            Main.waitTime = DRAW_FINISH_WAIT_TIME;
                            synchronized (Main.monitor) {
                                Main.monitor.notify();
                            }

                            findHome();
                            FileLog.log(Level.INFO, "Plotter break by user");
                            return;
                        }
                    } else if (i - lastCalibrate >= CALIBRATE_SVG_THRESHOLD) {
                        findHome();
                        lastCalibrate = i;
                    }
                } else if (tmp == -2) {
                    laser(ON);
                } else {
                    StepperControl.drawLine(tmp, xyControl[i++]);
                }
            }

            Main.waitTime = DRAW_FINISH_WAIT_TIME;
            synchronized (Main.monitor) {
                Main.monitor.notify();
            }

            findHome();
        }

    }

    public static void begin(String path) {
        Main.waitTime = DRAW_WAIT_TIME;
        int delayX = 0, delayY = 0, delayXY = 0;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(path))) {
            xyControl = (short[]) inputStream.readObject();
            delayX = StepperControl.STEP_DRAWLINE_X_DELAY = inputStream.readInt();
            delayY = StepperControl.STEP_DRAWLINE_Y_DELAY = inputStream.readInt();
            delayXY = StepperControl.STEP_DRAWLINE_XY_DELAY = inputStream.readInt();
            distance = inputStream.readInt();
        } catch (IOException | ClassNotFoundException ex) {
            FileLog.log(Level.SEVERE, ex);
        }

        FileLog.log(Level.CONFIG, "SVGPlotter.begin(...)", new String[][]{
            {"xyControl", String.valueOf(xyControl.length)},
            {"delay_X", String.valueOf(delayX)},
            {"delay_Y", String.valueOf(delayY)},
            {"delay_XY", String.valueOf(delayXY)},
            {"distance", String.valueOf(distance)}
        });

        thread = new SVGPlotterThread();
        thread.start();
    }

    public static boolean isRunning() {
        return thread.isAlive();
    }

}
