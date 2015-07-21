/*
 *  Copyright Nguyen Duong Kim Hao @ 2014
 */
package laserbot_galileo.io;

import laserbot_galileo.laserDraw.Image;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import laserbot_galileo.FileLog;
import laserbot_galileo.Main;
import static laserbot_galileo.Common.*;
import static laserbot_galileo.io.GPIO.*;

/**
 *
 * @author KimHao
 */
public class MegaCommand {

    private static InputStream inputStream;
    private static OutputStream outputStream;
    private static SerialPort serialPort;
    private static Thread receiveThread;

    public static byte[] command;
    public static int commandCount;
    public static boolean isCompleteCommand;
    public static boolean isListening;

    public static final Object monitor = new Object();

    static class CommmandReceiveThread extends Thread {

        @Override
        public void run() {
            boolean isStartedCommand = false;
            synchronized (monitor) {
                while (isListening) {
                    try {
                        while (inputStream.available() != 0) {
                            int data = inputStream.read();
                            if (isStartedCommand) {
                                if (data == '\n') {
                                    isCompleteCommand = true;
                                    isStartedCommand = false;
                                    mainNotify();
                                } else {
                                    command[commandCount++] = (byte) data;
                                }
                            } else if (data == '`') {
                                isStartedCommand = true;
                                isCompleteCommand = false;
                                commandCount = 0;
                            }
                        }
                        monitor.wait(MEGACOMMAND_LOOP_WAIT_TIME);
                    } catch (IOException | InterruptedException | ArrayIndexOutOfBoundsException ex) {
                        FileLog.log(Level.SEVERE, ex);
                    }
                }
            }
        }

        private void mainNotify() {
            synchronized (Main.monitor) {
                Main.monitor.notify();
            }
        }

    }

    public static void sendCommand(byte command) {
        try {
            outputStream.write('`');
            outputStream.write(command);
            outputStream.write('\n');
        } catch (IOException ex) {
            FileLog.log(Level.SEVERE, ex);
        }
    }

    public static void sendCommand(byte command, byte... arg) {
        int arglen = arg.length;
        byte[] dataToSend = new byte[2 + arglen + 1];
        dataToSend[0] = '`';
        dataToSend[1] = command;
        System.arraycopy(arg, 0, dataToSend, 2, arglen);
        dataToSend[2 + arglen] = '\n';
        try {
            outputStream.write(dataToSend);
        } catch (IOException ex) {
            FileLog.log(Level.SEVERE, ex);
        }
    }

    public static void sendCommand_newSize(Image image) {
        int width = image.newWidth;
        int height = image.newHeight;
        MegaCommand.sendCommand(COMMAND_NEW_SIZE,
                (byte) ((width >> 7 & 0x7F) + 20), (byte) ((width & 0x7F) + 20),
                (byte) ((height >> 7 & 0x7F) + 20), (byte) ((height & 0x7F) + 20));
    }

    public static void sendCommand_newDelayXY(int delay_X, int delay_Y) {
        MegaCommand.sendCommand(COMMAND_NEW_DELAY_XY,
                (byte) ((delay_X >> 7 & 0x7F) + 20), (byte) ((delay_X & 0x7F) + 20),
                (byte) ((delay_Y >> 7 & 0x7F) + 20), (byte) ((delay_Y & 0x7F) + 20));
    }

    public static void sendCommand_newQuality(int quanlity) {
        MegaCommand.sendCommand(COMMAND_NEW_QUALITY, (byte) quanlity);
    }

    public static void sendCommand_runState(int oldX, int newX) {
        byte[] dataToSend = new byte[2 + 4 + 1];
        dataToSend[0] = '`';
        dataToSend[1] = COMMAND_RUN_STATE;

        dataToSend[2] = (byte) ((oldX >> 7 & 0x7F) + 20);
        dataToSend[3] = (byte) ((oldX & 0x7F) + 20);
        dataToSend[4] = (byte) ((newX >> 7 & 0x7F) + 20);
        dataToSend[5] = (byte) ((newX & 0x7F) + 20);

        dataToSend[6] = '\n';
        try {
            outputStream.write(dataToSend);
        } catch (IOException ex) {
            FileLog.log(Level.SEVERE, ex);
        }
    }

    public static void sendCommand_runTimeInfo(long establishTime, long remainingTime) {
        byte[] dataToSend = new byte[2 + 6 + 1];
        dataToSend[0] = '`';
        dataToSend[1] = COMMAND_RUN_TIME_INFO;

        dataToSend[2] = (byte) ((establishTime / (1000 * 60 * 60)) + 20);
        dataToSend[3] = (byte) (((establishTime / (1000 * 60)) % 60) + 20);
        dataToSend[4] = (byte) (((establishTime / 1000) % 60) + 20);

        dataToSend[5] = (byte) ((remainingTime / (1000 * 60 * 60)) + 20);
        dataToSend[6] = (byte) (((remainingTime / (1000 * 60)) % 60) + 20);
        dataToSend[7] = (byte) (((remainingTime / 1000) % 60) + 20);

        dataToSend[8] = '\n';
        try {
            outputStream.write(dataToSend);
        } catch (IOException ex) {
            FileLog.log(Level.SEVERE, ex);
        }
    }

    public static void sendImageColor(Image image) {
        BufferedImage img = image.image;
        int[][] pixels = image.getPixels();
        int width = img.getWidth();
        int height = img.getHeight();
        try {
            outputStream.write('~');
            byte[] dataToSend = new byte[width * 2];
            for (int i = 0; i < height; i++) {
                int dataToSendCount = 0;
                for (int j = 0; j < width; j++) {
                    int rgb = pixels[j][i];
                    int red = (rgb >> 16) & 0xFF;
                    int green = (rgb >> 8) & 0xFF;
                    int blue = rgb & 0xFF;
                    int highByte = ((red & 248) | green >> 5);
                    int lowByte = ((green & 28) << 3 | blue >> 3);
                    dataToSend[dataToSendCount++] = (byte) highByte;
                    dataToSend[dataToSendCount++] = (byte) lowByte;
                }
                outputStream.write(dataToSend);
                //System.out.println(i);
            }
        } catch (IOException ex) {
            FileLog.log(Level.SEVERE, ex);
        }
    }

    public static void sendImageBW(Image image) {
        BufferedImage img = image.image;
        int[][] pixels = image.getPixels();
        int width = img.getWidth();
        int height = img.getHeight();
        try {
            outputStream.write('~');
            byte[] dataToSend = new byte[width];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    dataToSend[j] = (byte) (pixels[j][i] & 0xFF);
                }
                outputStream.write(dataToSend);
            }
        } catch (IOException ex) {
            FileLog.log(Level.SEVERE, ex);
        }
    }

    public static void begin() {
        serialBegin(SMEGA, SMEGA_BAUD);
        inputStream = getSerialInput(SMEGA);
        outputStream = getSerialOutput(SMEGA);
        serialPort = getSerialPort(SMEGA);
        try {
            serialPort.addEventListener((SerialPortEvent spe) -> {
                if (spe.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
                    synchronized (monitor) {
                        monitor.notify();
                    }
                }
            });
        } catch (TooManyListenersException ex) {
        }
        serialPort.notifyOnDataAvailable(true);
        command = new byte[COMMAND_BUFFER];
        beginListener();
    }

    public static void beginListener() {
        isCompleteCommand = false;
        isListening = true;
        commandCount = 0;
        try {
            while (inputStream.available() != 0) {
                inputStream.read(new byte[COMMAND_BUFFER]);
            }
        } catch (IOException ex) {
            FileLog.log(Level.SEVERE, ex);
        }
        receiveThread = new CommmandReceiveThread();
        receiveThread.setDaemon(true);
        receiveThread.start();
    }

    public static void endListener() {
        isListening = false;
        serialPort.notifyOnDataAvailable(false);
    }

}
