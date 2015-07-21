/*
 *  Copyright Nguyen Duong Kim Hao @ 2014
 */
package laserbot_galileo.laserDraw;

import java.util.logging.Level;
import laserbot_galileo.FileLog;
import laserbot_galileo.Main;
import laserbot_galileo.io.MegaCommand;
import static laserbot_galileo.Common.*;
import static laserbot_galileo.io.StepperControl.*;
import static laserbot_galileo.io.GPIO.*;

/**
 *
 * @author KimHao
 */
public class LaserDrawing {

    private static Thread thread;
    private static short[][] laserControl;

    public static int imageWidth, imageHeight, drawWidth;
    private static int imageHeightS1;
    public static int cur_X, cur_Y;
    public static int cur_X_begin;
    private static int count_Y;
    private static int delay_X, delay_Y;
    private static int step_X, step_Y, quality;

    static class LaserDrawingThread extends Thread {

        @Override
        public void run() {
            priority(MAX_PRIORITY);
            findHome();
            boolean laserState;
            if (cur_X_begin % 2 != 0) {
                dirY(FORWARD);
                stepY(imageHeight * step_Y, delay_Y);
                count_Y = imageHeightS1;
            }
            dirX(FORWARD);
            stepX(cur_X_begin * step_X, delay_X);
            for (cur_X = cur_X_begin; cur_X < imageWidth; cur_X++) {
                dirY(cur_X % 2 == 0);
                for (cur_Y = 0; cur_Y < imageHeight; cur_Y++) {
                    digitalWrite(LASER_CONTROL, laserState = (laserControl[cur_X][count_Y] == 0));
                    if (dir_Y) {
                        count_Y++;
                    } else {
                        count_Y--;
                    }
                    stepY(step_Y, laserState ? delay_Y : Y_MAXSPEED_DELAY);
                    if (digitalRead(STEPPER_Y_LIMIT) == BUTTON_PRESS) {
                        if (dir_Y == BACKWARD) {
                            break;
                        } else if (cur_Y >= MIN_Y) {
                            laser(OFF);
                            laserControl = null;
                            MegaCommand.sendCommand(COMMAND_DRAWING_BREAK);
                            delay(BUTTON_DELAY);

                            Main.waitTime = DRAW_FINISH_WAIT_TIME;
                            synchronized (Main.monitor) {
                                Main.monitor.notify();
                            }

                            findHome();
                            FileLog.log(Level.INFO, "Drawing break by user");
                            return;
                        }
                    }
                }
                if (dir_Y == BACKWARD) {
                    while (digitalRead(STEPPER_Y_LIMIT) != BUTTON_PRESS) {
                        stepY();
                        delayMicros(STEP_MAXSPEED_DELAY);
                    }
                    count_Y = 0;
                } else {
                    count_Y = imageHeightS1;
                }
                synchronized (Main.monitor) {
                    Main.monitor.notify();
                }
                stepX(step_X, delay_X);
            }
            laser(OFF);
            laserControl = null;
            FileLog.setPropertyAndSave("isNotFinish", 0);

            Main.waitTime = DRAW_FINISH_WAIT_TIME;
            synchronized (Main.monitor) {
                Main.monitor.notify();
            }

            findHome();
        }

    }

    public static void begin(Image i, int dX, int dY, int qu, int curX) {
        Main.waitTime = DRAW_WAIT_TIME;
        laserControl = i.laserControl;
        i.laserControl = null;
        imageWidth = i.newWidth;
        imageHeight = i.newHeight;
        imageHeightS1 = imageHeight - 1;
        cur_X = cur_Y = count_Y = 0;
        delay_X = dX;
        delay_Y = dY;
        cur_X_begin = curX;
        quality = qu;
        drawWidth = imageWidth - cur_X_begin;
        switch (quality) {
            case QUAL_HIGH:
                step_X = 2;
                step_Y = 1;
                break;
            case QUAL_MED:
                step_X = 4;
                step_Y = 2;
                break;
            case QUAL_LOW:
                step_X = 6;
                step_Y = 3;
                break;
        }
        System.out.println(String.format("sX = %d ; sY = %d", step_X, step_Y));
        System.out.println(String.format("dX = %d ; dY = %d", delay_X, delay_Y));
        log();
        thread = new LaserDrawingThread();
        thread.start();
    }

    private static void log() {
        FileLog.log(Level.CONFIG, "LaserDrawing.begin(...)", new String[][]{
            {"laserControlSize", String.valueOf(laserControl.length * laserControl[0].length)},
            {"imageWidth", String.valueOf(imageWidth)},
            {"imageHeight", String.valueOf(imageHeight)},
            {"delay_X", String.valueOf(delay_X)},
            {"delay_Y", String.valueOf(delay_Y)},
            {"step_X", String.valueOf(step_X)},
            {"step_Y", String.valueOf(step_Y)},
            {"cur_X_begin", String.valueOf(cur_X_begin)},
            {"quality", String.valueOf(quality)}
        });

        FileLog.setProperty("isNotFinish", 1);
        FileLog.setProperty("delay_X", delay_X);
        FileLog.setProperty("delay_Y", delay_Y);
        FileLog.setProperty("quality", quality);
        FileLog.setProperty("oldLogID", FileLog.id);
        FileLog.saveProperties();
    }

    public static boolean isRunning() {
        return thread.isAlive();
    }

}
