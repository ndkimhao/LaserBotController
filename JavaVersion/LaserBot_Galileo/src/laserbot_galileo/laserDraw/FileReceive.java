/*
 *  Copyright Nguyen Duong Kim Hao @ 2014
 */
package laserbot_galileo.laserDraw;

import laserbot_galileo.io.GPIO;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import laserbot_galileo.Common;
import laserbot_galileo.FileLog;
import static laserbot_galileo.Common.*;
import static laserbot_galileo.io.GPIO.*;

/**
 *
 * @author KimHao
 */
public class FileReceive {

    private static InputStream inputStream;
    private static Thread thread;

    public static int fileSize, receivedBytes;
    public static boolean isError;
    public static boolean isSentStartCommand;

    static class FileReceiveThread extends Thread {

        @Override
        public void run() {
            if (COMMAND_SFILE_BEGIN.equals(serialReadLine(SFILE, SFILE_BEGIN_TIMEOUT))) {
                try {
                    fileSize = Integer.parseInt(serialReadLine(SFILE, SFILE_FILESIZE_TIMEOUT));
                } catch (NumberFormatException e) {
                    isError = true;
                }
                if (COMMAND_SFILE_START.equals(serialReadLine(SFILE, SFILE_START_TIMEOUT))) {
                    priority(MAX_PRIORITY);
                    long lastReceive = System.currentTimeMillis();
                    byte[] buffer = new byte[SFILE_BUFFER];
                    try (FileOutputStream fileOutputStream
                            = new FileOutputStream(SFILE_PATH, false)) {
                        while (receivedBytes < fileSize) {
                            if (inputStream.available() != 0) {
                                int recv = inputStream.read(buffer);
                                fileOutputStream.write(buffer, 0, recv);
                                receivedBytes += recv;
                                lastReceive = System.currentTimeMillis();
                            } else if (System.currentTimeMillis() - lastReceive > SFILE_TRANSFER_TIMEOUT) {
                                isError = true;
                                break;
                            }
                        }
                    } catch (IOException ex) {
                        FileLog.log(Level.SEVERE, ex);
                        isError = true;
                    }
                } else {
                    isError = true;
                }
            } else {
                isError = true;
            }
            serialClose(SFILE);
        }

    }

    public static void begin() {
        isError = false;
        isSentStartCommand = false;
        receivedBytes = 0;
        fileSize = 0;
        serialBegin(SFILE, SFILE_BAUD);
        inputStream = GPIO.getSerialInput(Common.SFILE);

        FileLog.log(Level.INFO, "FileReceive.begin()");
        thread = new FileReceiveThread();
        thread.start();
    }

    public static boolean isRunning() {
        return thread.isAlive();
    }

}
