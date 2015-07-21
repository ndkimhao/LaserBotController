/*
 *  Copyright Nguyen Duong Kim Hao @ 2014
 */
package laserbot_galileo;

import laserbot_galileo.io.MegaCommand;
import laserbot_galileo.laserDraw.FileReceive;
import laserbot_galileo.laserDraw.Image;
import laserbot_galileo.laserDraw.LaserDrawing;
import laserbot_galileo.io.GPIO;
import laserbot_galileo.io.StepperControl;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import static laserbot_galileo.Common.*;
import static laserbot_galileo.io.MegaCommand.*;
import laserbot_galileo.svgPlotter.SVGPlotter;
import org.imgscalr.Scalr;

/**
 *
 * @author KimHao
 */
public class Main {

    private static int curSizeSettingButton = -1;
    private static int delay_X, delay_Y;
    private static int old_X;
    private static int quality;
    private static Image originalImage, lcdImage;
    public static int waitTime = DEFAULT_MAIN_LOOP_WAIT_TIME;
    private static long startTime;
    private static int curState = STATE_WAIT_START_COMMAND;

    public static final Object monitor = new Object();

    public static void main(String[] args) {
        try {
            FileLog.begin();
            GPIO.begin();
            MegaCommand.begin();
            StepperControl.begin();
            StepperControl.findHome();

            sendCommand(COMMAND_HOME);
            checkRecover();
            synchronized (monitor) {
                for (;;) {
                    checkRestartShutdown();
                    switch (curState) {
                        case STATE_WAIT_START_COMMAND:
                            if (isCompleteCommand) {
                                isCompleteCommand = false;
                                /*System.out.println("RECV (" + commandCount + ")"
                                 + Arrays.toString(command));*/
                                if (command[0] == COMMAND_START) {
                                    if (checkDrawNow()) {
                                        startDrawNow();
                                    } else if (checkDrawNowSVG()) {
                                        startDrawNowSVG();
                                    } else {
                                        startDrawNormal();
                                    }
                                } else if (command[0] == COMMAND_ENTER_RECOVER) {
                                    enterRecover();
                                }
                            }
                            break;
                        case STATE_WAIT_FILE:
                            if (FileReceive.isRunning()) {
                                fileRecvProgressCheck();
                            } else {
                                fileRecvFinish();
                            }
                            break;
                        case STATE_SETTING:
                            settingCheck();
                            break;
                        case STATE_RUN:
                            prepareImageToDraw();
                            break;
                        case STATE_RUNING:
                            runningCheck();
                            break;
                        case STATE_RUNING_SVG:
                            runningSVGCheck();
                            break;
                    }
                    if (waitTime != -1) {
                        try {
                            monitor.wait(waitTime);
                        } catch (InterruptedException ex) {
                        }
                    }
                }
            }
        } catch (Exception ex) {
            try {
                FileLog.log(Level.SEVERE, ex);
            } catch (Exception ex1) {
            }
            restart(false);
        }
    }

    private static void runningCheck() {
        if (LaserDrawing.isRunning()) {
            int new_X = map(LaserDrawing.cur_X, 0, LaserDrawing.imageWidth,
                    0, lcdImage.oldWidth - 1);
            if (new_X - old_X >= 5) {
                sendCommand_runState(old_X, new_X);
                old_X = new_X;
            }

            long establishTime = System.currentTimeMillis() - startTime;
            long remainingTime
                    = (establishTime * LaserDrawing.drawWidth
                    / (LaserDrawing.cur_X - LaserDrawing.cur_X_begin + 1)) - establishTime;
            sendCommand_runTimeInfo(establishTime, remainingTime);

            FileLog.setCurX(LaserDrawing.cur_X);
        } else {
            waitTime = DEFAULT_MAIN_LOOP_WAIT_TIME;
            sendCommand(COMMAND_HOME);
            curState = STATE_WAIT_START_COMMAND;
            priority(NORM_PRIORITY);
            MegaCommand.beginListener();
            FileLog.log(Level.INFO, "Draw image => end!\n"
                    + "MegaCommand.beginListener() => ok!");
            FileLog.newLog();
            checkRecover();
        }
    }

    private static void prepareImageToDraw() {
        priority(MAX_PRIORITY);
        lcdImage = originalImage.createThumbnail(Scalr.Method.BALANCED);
        originalImage.resize(Scalr.Method.SPEED);
        sendImageColor(lcdImage);
        originalImage.dither();
        lcdImage.grayscale();
        sendImageBW(lcdImage);

        //originalImage.save(FileLog.path + "originalImage.gif");
        originalImage.saveLaserControl(FileLog.path + "laserControl.array",
                lcdImage.oldWidth, delay_X, delay_Y, quality);
        try {
            Files.copy(new File(GALILEO_PATH + "image.jpg").toPath(),
                    new File(FileLog.path + "recvImage.jpg").toPath());
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        lcdImage.save(FileLog.path + "lcdImage.gif");
        FileLog.setPropertyAndSave("lcdImageOldWidth", lcdImage.oldWidth);
        FileLog.log(Level.INFO, "Save image => ok!"
                + "\nImage prepare => ok!");

        priority(MIN_PRIORITY);
        endListener();
        curState = STATE_RUNING;
        old_X = 0;
        startTime = System.currentTimeMillis();
        LaserDrawing.begin(originalImage, delay_X, delay_Y, quality, 0);
    }

    private static void settingCheck() {
        switch (curSizeSettingButton) {
            case SIZE_ADD_BUTTON:
                originalImage.increaseSize();
                StepperControl.goToImagePosition(originalImage, quality);
                break;
            case SIZE_SUB_BUTTON:
                originalImage.decreaseSize();
                StepperControl.goToImagePosition(originalImage, quality);
                break;
        }
        if (isCompleteCommand) {
            isCompleteCommand = false;
            switch (command[0]) {
                case COMMAND_SIZE_ADJUST_PRESS:
                    curSizeSettingButton = command[1];
                    waitTime = SIZE_ADJUST_WAIT_TIME;
                    break;
                case COMMAND_DELAY_QUAL_ADJUST:
                    switch (command[1]) {
                        case DELAY_X_ADD_BUTTON:
                            delay_X += DELAY_ADJUST_STEP;
                            sendCommand_newDelayXY(delay_X, delay_Y);
                            break;
                        case DELAY_X_SUB_BUTTON:
                            delay_X -= DELAY_ADJUST_STEP;
                            sendCommand_newDelayXY(delay_X, delay_Y);
                            break;
                        case DELAY_Y_ADD_BUTTON:
                            delay_Y += DELAY_ADJUST_STEP;
                            sendCommand_newDelayXY(delay_X, delay_Y);
                            break;
                        case DELAY_Y_SUB_BUTTON:
                            delay_Y -= DELAY_ADJUST_STEP;
                            sendCommand_newDelayXY(delay_X, delay_Y);
                            break;
                        case QUAN_LOW_BUTTON:
                            changeQuality(QUAL_LOW);
                            break;
                        case QUAN_MED_BUTTON:
                            changeQuality(QUAL_MED);
                            break;
                        case QUAN_HIGH_BUTTON:
                            changeQuality(QUAL_HIGH);
                            break;
                    }
                    break;
                case COMMAND_SIZE_ADJUST_RELEASE:
                    curSizeSettingButton = -1;
                    sendCommand_newSize(originalImage);
                    waitTime = DEFAULT_MAIN_LOOP_WAIT_TIME;
                    break;
                case COMMAND_RUN:
                    curState = STATE_RUN;
                    break;
                case COMMAND_SETTING_EXIT:
                    curState = STATE_WAIT_START_COMMAND;
                    sendCommand(COMMAND_HOME);
                    checkRecover();
                    break;
            }
        }
    }

    private static void fileRecvFinish() {
        waitTime = DEFAULT_MAIN_LOOP_WAIT_TIME;
        if (FileReceive.isError) {
            sendCommand(COMMAND_RECVFILE_FAIL);
            curState = STATE_WAIT_START_COMMAND;
            checkRecover();
            FileLog.log(Level.INFO, "FileReceive => error!");
            System.out.println("curState = STATE_WAIT_START_COMMAND;");
        } else {
            FileLog.log(Level.INFO, "FileReceive => successful!");
            curState = STATE_SETTING;
            delay_X = DEFAULT_X_DELAY;
            delay_Y = DEFAULT_Y_DELAY;
            quality = DEFAULT_QUALITY;
            sendCommand(COMMAND_RECVFILE_END);
            originalImage = new Image(SFILE_PATH);
            sendCommand(COMMAND_RECVFILE_LOAD_FINISH);
            StepperControl.findHome();
            sendCommand(COMMAND_RECVFILE_GOTO_XY);
            originalImage.checkSize_while();
            StepperControl.goToImagePosition_line(originalImage, quality);
            sendCommand(COMMAND_SWITCH_SETTING_SCREEN);
            sendCommand_newDelayXY(delay_X, delay_Y);
            sendCommand_newSize(originalImage);
        }
    }

    private static void fileRecvProgressCheck() {
        if (FileReceive.receivedBytes > 0) {
            if (!FileReceive.isSentStartCommand) {
                FileReceive.isSentStartCommand = true;
                sendCommand(COMMAND_RECVFILE_START);
            }
            sendCommand(COMMAND_RECVFILE_PROCCESS,
                    (byte) (FileReceive.receivedBytes * 100.0 / FileReceive.fileSize));
            delay(RECVFILE_WAIT_TIME);
        }
    }

    private static void enterRecover() {
        priority(MAX_PRIORITY);
        try {
            Files.move(new File(FileLog.oldPath + "lcdImage.gif").toPath(),
                    new File(FileLog.path + "lcdImage.gif").toPath());
            Files.move(new File(FileLog.oldPath + "laserControl.array").toPath(),
                    new File(FileLog.path + "laserControl.array").toPath());
            Files.move(new File(FileLog.oldPath + "recvImage.jpg").toPath(),
                    new File(FileLog.path + "recvImage.jpg").toPath());
            Files.createFile(new File(FileLog.oldPath + "recovered-"
                    + String.format("%06d", FileLog.id)).toPath());
        } catch (IOException ex) {
            FileLog.log(Level.SEVERE, ex);
        }
        lcdImage = new Image(FileLog.path + "lcdImage.gif");
        originalImage = new Image();
        originalImage.loadLaserControl(FileLog.path + "laserControl.array");
        lcdImage.oldWidth = originalImage.oldWidth;
        FileLog.loadCurXProperties();
        sendImageBW(lcdImage);

        priority(MIN_PRIORITY);
        endListener();
        curState = STATE_RUNING;
        int beginX = FileLog.getCurX();
        old_X = map(beginX, 0, originalImage.newWidth,
                0, lcdImage.oldWidth - 1);
        sendCommand_runState(0, old_X);
        /*delay_X = FileLog.getProperty("delay_X");
         delay_Y = FileLog.getProperty("delay_Y");
         quality = FileLog.getProperty("quality");*/
        delay_X = originalImage.delay_X;
        delay_Y = originalImage.delay_Y;
        quality = originalImage.quality;
        startTime = System.currentTimeMillis();
        LaserDrawing.begin(originalImage, delay_X, delay_Y, quality, beginX);

        FileLog.log(Level.INFO, "Enter recover => ok");
    }

    private static void startDrawNormal() {
        curState = STATE_WAIT_FILE;
        System.out.println("curState = STATE_WAIT_FILE;");
        waitTime = RECVFILE_WAIT_TIME;
        FileReceive.begin();
    }

    private static void startDrawNow() {
        curState = STATE_RUNING;
        System.out.println("DrawNow!");
        sendCommand(COMMAND_ENTER_RUN);
        priority(MAX_PRIORITY);
        lcdImage = new Image(FileLog.path + "lcdImage.gif");
        originalImage = new Image();
        originalImage.loadLaserControl(FileLog.path + "laserControl.array");
        lcdImage.oldWidth = originalImage.oldWidth;
        sendImageBW(lcdImage);
        priority(MIN_PRIORITY);
        endListener();
        startTime = System.currentTimeMillis();

        delay_X = originalImage.delay_X;
        delay_Y = originalImage.delay_Y;
        quality = originalImage.quality;
        old_X = 0;
        LaserDrawing.begin(originalImage, delay_X, delay_Y, quality, 0);
    }

    private static void startDrawNowSVG() {
        curState = STATE_RUNING_SVG;
        System.out.println("DrawNow SVG!");
        sendCommand(COMMAND_ENTER_RUN);
        priority(MAX_PRIORITY);
        lcdImage = new Image(FileLog.path + "lcdImage.gif");
        sendImageBW(lcdImage);
        priority(MIN_PRIORITY);
        endListener();
        startTime = System.currentTimeMillis();

        SVGPlotter.begin(FileLog.path + "xyData.array");
    }

    private static void runningSVGCheck() {
        if (!SVGPlotter.isRunning()) {
            waitTime = DEFAULT_MAIN_LOOP_WAIT_TIME;
            sendCommand(COMMAND_HOME);
            curState = STATE_WAIT_START_COMMAND;
            priority(NORM_PRIORITY);
            MegaCommand.beginListener();
            FileLog.log(Level.INFO, "SVG Plotter => end!\n"
                    + "MegaCommand.beginListener() => ok!");
            FileLog.newLog();
            checkRecover();
        }
    }

    private static void checkRestartShutdown() {
        if (isCompleteCommand) {
            if (command[0] == COMMAND_RESTART) {
                isCompleteCommand = false;
                MegaCommand.endListener();
                GPIO.cleanUp();
                restart(true);
            } else if (command[0] == COMMAND_SHUTDOWN) {
                isCompleteCommand = false;
                MegaCommand.endListener();
                GPIO.cleanUp();
                shutdown();
            }

        }
    }

    private static void checkRecover() {
        if (FileLog.getProperty("isNotFinish") == 1) {
            if (new File(FileLog.oldPath + "laserControl.array").exists()) {
                sendCommand(COMMAND_ENABLE_RECOVER);
                FileLog.log(Level.FINE, "Recover ready");
            } else {
                FileLog.setPropertyAndSave("isNotFinish", 0);
            }
        }
    }

    private static boolean checkDrawNow() {
        File arr = new File(GALILEO_PATH + "drawNow/laserControl.array");
        File lcd = new File(GALILEO_PATH + "drawNow/lcdImage.gif");
        if (arr.exists() && lcd.exists()) {
            File img = new File(GALILEO_PATH + "drawNow/image.jpg");
            try {
                Files.move(lcd.toPath(),
                        new File(FileLog.path + "lcdImage.gif").toPath());
                Files.move(arr.toPath(),
                        new File(FileLog.path + "laserControl.array").toPath());
                if (img.exists()) {
                    Files.move(img.toPath(),
                            new File(FileLog.path + "recvImage.jpg").toPath());
                }
            } catch (IOException ex) {
                FileLog.log(Level.SEVERE, ex);
                return false;
            }
            FileLog.log(Level.FINE, "Drawnow ready!");
            return true;
        }
        return false;
    }

    private static boolean checkDrawNowSVG() {
        File arr = new File(GALILEO_PATH + "drawNow/xyData.array");
        File lcd = new File(GALILEO_PATH + "drawNow/lcdImage.gif");
        if (arr.exists() && lcd.exists()) {
            File svg = new File(GALILEO_PATH + "drawNow/drawing.svg");
            try {
                Files.move(lcd.toPath(),
                        new File(FileLog.path + "lcdImage.gif").toPath());
                Files.move(arr.toPath(),
                        new File(FileLog.path + "xyData.array").toPath());
                if (svg.exists()) {
                    Files.move(svg.toPath(),
                            new File(FileLog.path + "drawing.svg").toPath());
                }
            } catch (IOException ex) {
                FileLog.log(Level.SEVERE, ex);
                return false;
            }
            FileLog.log(Level.FINE, "Drawnow SVG ready!");
            return true;
        }
        return false;
    }

    private static void changeQuality(int q) {
        quality = q;
        originalImage.setQuality(quality);
        originalImage.checkSize_while();
        sendCommand_newSize(originalImage);
        sendCommand_newQuality(quality);
        StepperControl.goToImagePosition_line(originalImage, quality);
    }

    public static void restart(boolean byUser) {
        if (byUser) {
            FileLog.log(Level.INFO, "Restart by user !");
        } else {
            FileLog.log(Level.INFO, "Restart because error !");
        }
        /* try {
         Runtime.getRuntime().exec("shutdown -r now");
         } catch (IOException ex) {
         FileLog.log(Level.SEVERE, ex);
         }*/
        System.exit(0);
    }

    public static void shutdown() {
        FileLog.log(Level.INFO, "Shutdown by user !");
        try {
            Runtime.getRuntime().exec("shutdown -h now");
        } catch (IOException ex) {
            FileLog.log(Level.SEVERE, ex);
        }
        //System.exit(0);
    }

}
