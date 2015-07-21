/*
 *  Copyright Nguyen Duong Kim Hao @ 2014
 */
package laserbot_galileo;

/**
 *
 * @author KimHao
 */
public class Common {

    public static final String GALILEO_PATH = "/root/netbeans/LaserBot_Galileo/dist/";

    public static final String COMPUTER_PATH = "D:\\Projects\\Arduino\\Galileo\\LaserBot_Galileo\\";
    public static final String DRAWNOW_PATH = COMPUTER_PATH + "drawNow\\";
    public static final String DRAWNOW_SVG_PATH = COMPUTER_PATH + "drawNowSVG\\";

    public static final int SMEGA = 0;
    public static final int SMEGA_BAUD = 115200;
    public static final int COMMAND_BUFFER = 32;

    public static final byte COMMAND_START = 20;
    public static final byte COMMAND_RECVFILE_FAIL = 21;
    public static final byte COMMAND_RECVFILE_START = 22;
    public static final byte COMMAND_RECVFILE_PROCCESS = 23;
    public static final byte COMMAND_RECVFILE_END = 24;
    public static final byte COMMAND_RECVFILE_LOAD_FINISH = 25;
    public static final byte COMMAND_RECVFILE_GOTO_XY = 26;
    public static final byte COMMAND_SWITCH_SETTING_SCREEN = 27;
    public static final byte COMMAND_SIZE_ADJUST_PRESS = 28;
    public static final byte COMMAND_SIZE_ADJUST_RELEASE = 29;
    public static final byte COMMAND_NEW_SIZE = 30;
    public static final byte COMMAND_DELAY_QUAL_ADJUST = 31;
    public static final byte COMMAND_NEW_DELAY_XY = 32;
    public static final byte COMMAND_NEW_QUALITY = 33;
    public static final byte COMMAND_RUN = 34;
    public static final byte COMMAND_RUN_DITHERING = 35;
    public static final byte COMMAND_RUN_STATE = 36;
    public static final byte COMMAND_RUN_TIME_INFO = 37;
    public static final byte COMMAND_HOME = 38;
    public static final byte COMMAND_ENABLE_RECOVER = 39;
    public static final byte COMMAND_ENTER_RECOVER = 40;
    public static final byte COMMAND_DRAWING_BREAK = 41;
    public static final byte COMMAND_ENTER_RUN = 42;
    public static final byte COMMAND_RESTART = 43;
    public static final byte COMMAND_SETTING_EXIT = 44;
    public static final byte COMMAND_SHUTDOWN = 45;

    public static final byte SIZE_ADD_BUTTON = 0;
    public static final byte SIZE_SUB_BUTTON = 1;
    public static final byte DELAY_X_ADD_BUTTON = 2;
    public static final byte DELAY_X_SUB_BUTTON = 3;
    public static final byte DELAY_Y_ADD_BUTTON = 4;
    public static final byte DELAY_Y_SUB_BUTTON = 5;
    public static final byte QUAN_LOW_BUTTON = 6;
    public static final byte QUAN_MED_BUTTON = 7;
    public static final byte QUAN_HIGH_BUTTON = 8;

    public static final int QUAL_LOW = 0;
    public static final int QUAL_MED = 1;
    public static final int QUAL_HIGH = 2;

    public static final int DELAY_ADJUST_STEP = 50;
    public static final int DEFAULT_X_DELAY = 2500;
    public static final int DEFAULT_Y_DELAY = 350;
    public static final int Y_MAXSPEED_DELAY = 300;
    public static final int DEFAULT_QUALITY = QUAL_HIGH;

    public static final int DEFAULT_STEP_DRAWLINE_X_DELAY = 2750;
    public static final int DEFAULT_STEP_DRAWLINE_Y_DELAY = 6000;
    public static final int DEFAULT_STEP_DRAWLINE_XY_DELAY = 4500;

    public static final int PULSE_DELAY = 50;
    public static final int STEP_MAXSPEED_DELAY = 200;
    public static final int STEP_XY_DELAY = 750;

    public static final int CALIBRATE_SVG_THRESHOLD = 10000;
    public static final int BUTTON_DELAY = 1000;

    public static final int SIZE_ADJUST_WAIT_TIME = -1;
    public static final int DRAW_WAIT_TIME = 0;
    public static final int RECVFILE_WAIT_TIME = 10;
    public static final int DEFAULT_MAIN_LOOP_WAIT_TIME = 10000;
    public static final int MEGACOMMAND_LOOP_WAIT_TIME = 10000;
    public static final int DRAW_FINISH_WAIT_TIME = 1000;

    public static final int STATE_WAIT_START_COMMAND = 0;
    public static final int STATE_WAIT_FILE = 1;
    public static final int STATE_SETTING = 2;
    public static final int STATE_RUN = 3;
    public static final int STATE_RUNING = 4;
    public static final int STATE_RUNING_SVG = 5;

    public static final int SFILE = 1;
    public static final int SFILE_BAUD = 230400;
    public static final String COMMAND_SFILE_BEGIN = "BEGIN_TRANSFER_IMAGE";
    public static final int SFILE_BEGIN_TIMEOUT = 15000;
    public static final String COMMAND_SFILE_START = "START_TRANSFER_IMAGE";
    public static final int SFILE_FILESIZE_TIMEOUT = 1000;
    public static final int SFILE_START_TIMEOUT = 1000;
    public static final int SFILE_TRANSFER_TIMEOUT = 1000;
    public static final int SFILE_BUFFER = 4096;
    public static final String SFILE_PATH = GALILEO_PATH + "image.jpg";

    public static final int STEPPER_X_LIMIT = 5;
    public static final int STEPPER_X_DIR = 6;
    public static final int STEPPER_X_CLK = 7;
    public static final int STEPPER_Y_LIMIT = 8;
    public static final int STEPPER_Y_DIR = 9;
    public static final int STEPPER_Y_CLK = 10;
    public static final int LASER_CONTROL = 11;

    public static final boolean HIGH = true;
    public static final boolean LOW = false;
    public static final boolean BUTTON_PRESS = LOW;
    public static final boolean ON = HIGH;
    public static final boolean OFF = LOW;

    public static final int SCREEN_WIDTH = 320;
    public static final int SCREEN_HEIGHT = 240;
    public static final int MAX_X = 14000;
    public static final int MAX_Y = 9000;
    public static final int MIN_X = 200;
    public static final int MIN_Y = 100;

    public static final int DITHER_ERROR_DIFFUSION = 8;
    public static final int DITHER_THRESHOLD = 127;

    public static final boolean FORWARD = true;
    public static final boolean BACKWARD = false;

    public static final int PIXEL_X_MULTIPLIER = 4;
    public static final int PIXEL_Y_MULTIPLIER = 2;

    public final static int MIN_PRIORITY = 1;
    public final static int NORM_PRIORITY = 5;
    public final static int MAX_PRIORITY = 10;

    public final static int DEFAULT_PRECISION = 500;
    public final static int DEFAULT_SEGMENTS = 500;

    public final static String LOGGER_NAME = "LaserBot_Log";

    public static void priority(int newPriority) {
        Thread.currentThread().setPriority(newPriority);
    }

    public static void delay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
        }
    }

    public static void delay(long millis, int micros) {
        try {
            Thread.sleep(millis, micros);
        } catch (InterruptedException ex) {
        }
    }

    public static void delayMicros(int micros) {
        try {
            Thread.sleep(micros / 1000, micros % 1000);
        } catch (InterruptedException ex) {
        }
    }

    public static int map(int value, int inputMin, int inputMax, int outputMin, int outputMax) {
        return (value - inputMin) * (outputMax - outputMin) / (inputMax - inputMin) + outputMin;
    }

}
