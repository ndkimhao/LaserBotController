/*
 *  Copyright Nguyen Duong Kim Hao @ 2014
 */
package laserbot_galileo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import static laserbot_galileo.Common.*;

/**
 *
 * @author KimHao
 */
public final class FileLog {

    public static final Logger logger = Logger.getLogger(LOGGER_NAME);
    private static final Formatter formatter = new CustomFormatter();
    public static FileHandler fileHandler;
    public static Properties properties;
    public static Properties curX_property;
    public static String oldPath;
    public static int id;
    public static String path;

    public static void begin() {
        loadProperties();
        newLog();
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.FINEST);
    }

    public static void newLog() {
        logger.removeHandler(fileHandler);
        id = getProperty("count");
        try {
            path = GALILEO_PATH + String.format("logs/log-%06d/", id);
            oldPath = GALILEO_PATH + String.format("logs/log-%06d/", getProperty("oldLogID"));
            new File(path).mkdir();
            fileHandler = new FileHandler(path + "log.txt");
        } catch (IOException | SecurityException ex) {
            log(Level.SEVERE, ex);
        }
        setPropertyAndSave("count", id + 1);
        fileHandler.setFormatter(formatter);
        logger.addHandler(fileHandler);
        curX_property = new Properties();
        logger.log(Level.INFO, "FileLog.newLog() => ok!");
    }

    public static void saveProperties() {
        try (OutputStream out = new FileOutputStream(GALILEO_PATH + "logs/log.properties")) {
            properties.store(out, "Laserbot log configuration");
        } catch (IOException ex) {
            log(Level.SEVERE, ex);
        }
    }

    public static void loadProperties() {
        properties = new Properties();
        try (InputStream is = new FileInputStream(GALILEO_PATH + "logs/log.properties")) {
            properties.load(is);
        } catch (IOException ex) {
            log(Level.SEVERE, ex);
        }
    }

    public static void loadCurXProperties() {
        curX_property = new Properties();
        try (InputStream is = new FileInputStream(oldPath + "curX.properties")) {
            curX_property.load(is);
        } catch (IOException ex) {
            log(Level.SEVERE, ex);
        }
    }

    public static void setCurX(int value) {
        curX_property.setProperty("curX", String.valueOf(value));
        try (OutputStream out = new FileOutputStream(path + "curX.properties")) {
            curX_property.store(out, "");
        } catch (IOException ex) {
            log(Level.SEVERE, ex);
        }
    }

    public static int getCurX() {
        return Integer.parseInt(curX_property.getProperty("curX"));
    }

    public static void setProperty(String key, int value) {
        properties.setProperty(key, String.valueOf(value));
    }

    public static void setPropertyAndSave(String key, int value) {
        properties.setProperty(key, String.valueOf(value));
        saveProperties();
    }

    public static int getProperty(String key) {
        return Integer.parseInt(properties.getProperty(key, "0"));
    }

    public static void log(Level level, Exception e, String[][] arrInfo) {
        StringBuilder info = new StringBuilder();
        if (arrInfo != null && arrInfo.length != 0) {
            for (String[] data : arrInfo) {
                info.append(data[0])
                        .append(": ")
                        .append(data[1])
                        .append("\n");
            }
        }
        logger.log(level, info.toString(), e);
    }

    public static void log(Level level, Exception e) {
        logger.log(level, null, e);
    }

    public static void log(Level level, String message) {
        logger.log(level, message);
    }

    public static void log(Level level, String message, String[][] arrInfo) {
        StringBuilder info = new StringBuilder();
        if (message != null) {
            info.append(message).append("\n");
        }
        if (arrInfo != null && arrInfo.length != 0) {
            for (String[] data : arrInfo) {
                info.append(data[0])
                        .append(": ")
                        .append(data[1])
                        .append("\n");
            }
        }
        logger.log(level, info.toString());
    }

}

class CustomFormatter extends Formatter {

    private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS a");
    private static final Date date = new Date();
    private static final String boxLine
            = "==============================================================="
            + "===============================================================";
    private static final String spaces
            = "                                                                  ";
    private static final String format = "%1$-" + (boxLine.length() - 2) + "s";
    private static int dataCount = 0;

    @Override
    public String format(LogRecord record) {
        dataCount++;
        date.setTime(record.getMillis());
        StringBuilder builder = new StringBuilder(1000);
        Level l = record.getLevel();
        builder.append("(").append(String.format("%05d", dataCount)).append(") ")
                .append("[").append(record.getLevel()).append("] - ")
                .append(df.format(date));
        if (l.intValue() >= Level.CONFIG.intValue()) {
            builder.append(spaces + "__[").append(record.getLevel()).append("]__");
        }
        builder.append("\n");
        String m = record.getMessage();
        if (m != null) {
            builder.append(m);
        }
        Throwable t = record.getThrown();
        if (t != null) {
            StringWriter sw = new StringWriter();
            try (PrintWriter pw = new PrintWriter(sw)) {
                pw.println();
                t.printStackTrace(pw);
            }
            builder.append(sw.toString());
        } else {
            builder.append("\n");
        }

        String[] lines = builder.toString().replaceAll("\\t", "    ").split("\\n");
        StringBuilder message = new StringBuilder();
        message.append("|" + boxLine + "|\n");
        for (String line : lines) {
            message.append("|  ").append(String.format(format, line)).append("|\n");
        }
        message.append("|" + boxLine + "|\n\n\n");
        return message.toString();
    }

}
