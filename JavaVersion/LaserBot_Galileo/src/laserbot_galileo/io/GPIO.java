/*
 *  Copyright Nguyen Duong Kim Hao @ 2014
 */
package laserbot_galileo.io;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import laserbot_galileo.FileLog;

/**
 *
 * @author KimHao
 */
public final class GPIO {

    private static Set<Integer> GPIOExporteds;
    private static Set<Integer> PWMExporteds;
    private static RandomAccessFile[] GPIOHandlers;
    private static long PWMPeriod;
    private static SerialPort[] serialPorts;
    private static InputStream[] serialInputs;
    private static OutputStream[] serialOutputs;

    public static void begin() {
        setPWMPeriod(PWM_DEFAULT_PERIOD);
        GPIOHandlers = new RandomAccessFile[NUM_GPIO];
        GPIOExporteds = new HashSet<>();
        PWMExporteds = new HashSet<>();
        serialPorts = new SerialPort[MAX_SERIAL];
        serialInputs = new InputStream[MAX_SERIAL];
        serialOutputs = new OutputStream[MAX_SERIAL];
    }

    public static void pinMode(final int pin, final int mode) {
        int[][] mux = selectMuxing(pin, mode);
        if (mux == null) {
            return;
        }

        int GPIO_ID = GPIO_MAPPING[pin];
        GPIOExport(GPIO_ID);

        int pwmChanel = getPWMChanel(pin);
        try {
            if (mode == OUTPUT || mode == INPUT || mode == INPUT_PULLUP
                    || mode == INPUT_PULLDOWN) {
                GPIOHandlers[pin] = new RandomAccessFile(
                        String.format("/sys/class/gpio/gpio%d/value", GPIO_ID), "rwd");
            } else if (mode == ANALOG_INPUT) {
                GPIOHandlers[pin] = new RandomAccessFile(
                        String.format("/sys/bus/iio/devices/iio:device0/in_voltage%d_raw",
                                pin - MIN_ANALOG), "rwd");
            } else if (mode == PWM) {
                PWMExport(pwmChanel);
                GPIOHandlers[pin] = new RandomAccessFile(
                        String.format("/sys/class/pwm/pwmchip0/pwm%d/duty_cycle",
                                pwmChanel), "rwd");
            }
        } catch (FileNotFoundException ex) {
            FileLog.log(Level.SEVERE, ex, new String[][]{
                {"pin", String.valueOf(pin)},
                {"mode", String.valueOf(mode)}
            });
            return;
        }

        runMux(mux);

        if (mode == OUTPUT) {
            setGPIODirection(GPIO_ID, OUTPUT);
            setGPIODrive(GPIO_ID, DRIVE_STRONG);
            digitalWrite(pin, GPIO_LOW);
        } else if (mode == INPUT || mode == INPUT_PULLDOWN || mode == INPUT_PULLUP) {
            setGPIODirection(GPIO_ID, INPUT);
        } else if (mode == PWM) {
            analogWrite(pin, 0);
            writeInt(String.format("/sys/class/pwm/pwmchip0/pwm%d/enable", pwmChanel), 1);
        }
    }

    public static void serialBegin(final int index, final int baudRate) {
        runMux(GPIO_MUX_SERIAL[index]);
        try {
            CommPortIdentifier portIdentifier;
            portIdentifier = CommPortIdentifier.getPortIdentifier(
                    String.format("/dev/ttyS%d", index));
            SerialPort port = serialPorts[index]
                    = (SerialPort) portIdentifier.open("SerialPort" + index, TIMEOUT_SERIAL);
            port.setSerialPortParams(baudRate,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            serialInputs[index] = port.getInputStream();
            serialOutputs[index] = port.getOutputStream();
        } catch (NoSuchPortException | PortInUseException |
                UnsupportedCommOperationException | IOException ex) {
            FileLog.log(Level.SEVERE, ex, new String[][]{
                {"index", String.valueOf(index)},
                {"baudRate", String.valueOf(baudRate)}
            });
        }
    }

    public static void serialClose(final int index) {
        SerialPort port = serialPorts[index];
        port.removeEventListener();
        port.close();
    }

    public static InputStream getSerialInput(final int index) {
        return serialInputs[index];
    }

    public static SerialPort getSerialPort(final int index) {
        return serialPorts[index];
    }

    public static OutputStream getSerialOutput(final int index) {
        return serialOutputs[index];
    }

    public static String serialReadLine(final int index, final long timeOut) {
        long startTime = System.currentTimeMillis();
        StringBuilder result = new StringBuilder();
        boolean isStartedString = false;
        InputStream stream = serialInputs[index];
        try {
            while (System.currentTimeMillis() - startTime < timeOut) {
                if (stream.available() != 0) {
                    int data = stream.read();
                    if (isStartedString) {
                        if (data == '\n') {
                            return result.toString();
                        } else {
                            result.append((char) data);
                        }
                    } else if (data == '`') {
                        isStartedString = true;
                    }
                }
            }
        } catch (IOException ex) {
            FileLog.log(Level.SEVERE, ex, new String[][]{
                    {"index", String.valueOf(index)},
                    {"timeOut", String.valueOf(timeOut)}
                });
        }
        return null;
    }

    private static void runMux(int[][] mux) {
        for (int[] gpioData : mux) {
            int gid = gpioData[0];
            int val = gpioData[1];
            GPIOExport(gid);
            if (val == GPIO_NONE) {
                setGPIODirection(gid, INPUT);
                setGPIODrive(gid, DRIVE_HIZ);
            } else {
                setGPIODirection(gid, OUTPUT);
                setGPIODrive(gid, DRIVE_STRONG);
                setGPIOValue(gid, val);
            }
        }
    }

    public static void digitalWrite(final int pin, final int value) {
        RandomAccessFile file = GPIOHandlers[pin];
        try {
            file.seek(0);
            file.write(value + '0');
        } catch (IOException ex) {
            FileLog.log(Level.SEVERE, ex, new String[][]{
                    {"pin", String.valueOf(pin)},
                    {"value", String.valueOf(value)}
                });
        }
    }

    public static void digitalWrite(final int pin, final boolean value) {
        RandomAccessFile file = GPIOHandlers[pin];
        try {
            file.seek(0);
            file.write(value ? '1' : '0');
        } catch (IOException ex) {
            FileLog.log(Level.SEVERE, ex, new String[][]{
                    {"pin", String.valueOf(pin)},
                    {"value", String.valueOf(value)}
                });
        }
    }

    public static boolean digitalRead(final int pin) {
        RandomAccessFile file = GPIOHandlers[pin];
        try {
            file.seek(0);
            return file.read() == '1';
        } catch (IOException ex) {
            FileLog.log(Level.SEVERE, ex, new String[][]{
                {"pin", String.valueOf(pin)}
            });
            return false;
        }
    }

    public static void analogWrite(int pin, int value) {
        if (pin < MIN_GPIO || pin > MAX_GPIO) {
            return;
        }
        int pwmChanel = getPWMChanel(pin);
        if (pwmChanel != -1) {
            if (value < PWM_MIN_VALUE) {
                value = PWM_MIN_VALUE;
            } else if (value > PWM_MAX_VALUE) {
                value = PWM_MAX_VALUE;
            }
            RandomAccessFile file = GPIOHandlers[pin];
            long duty = PWMPeriod * value / PWM_MAX_VALUE;
            try {
                file.seek(0);
                file.writeBytes(Long.toString(duty));
            } catch (IOException ex) {
                FileLog.log(Level.SEVERE, ex, new String[][]{
                    {"pin", String.valueOf(pin)},
                    {"value", String.valueOf(value)}
                });
            }
        }
    }

    private static int getPWMChanel(int pin) {
        int pwmChanel = -1;
        for (int[] pwmData : PWM_MAPPING) {
            if (pwmData[0] == pin) {
                pwmChanel = pwmData[1];
                break;
            }
        }
        return pwmChanel;
    }

    public static int analogRead(int pin) {
        if (pin < MIN_ANALOG || pin > MAX_ANALOG) {
            return -1;
        }
        RandomAccessFile file = GPIOHandlers[pin];
        try {
            file.seek(0);
            return Integer.parseInt(file.readLine());
        } catch (IOException ex) {
            FileLog.log(Level.SEVERE, ex, new String[][]{
                {"pin", String.valueOf(pin)}
            });
            return -1;
        }
    }

    public static void setPWMPeriod(long period) {
        if (period < PWM_MIN_PERIOD || period > PWM_MAX_PERIOD) {
            return;
        }
        writeLong("/sys/class/pwm/pwmchip0/device/pwm_period", period);
        PWMPeriod = period;
    }

    private static void setGPIODirection(int gpioID, int direction) {
        writeString(String.format("/sys/class/gpio/gpio%d/direction", gpioID),
                direction == OUTPUT ? "out" : "in");
    }

    private static void setGPIODirection(int gpioID, String value) {
        writeString(String.format("/sys/class/gpio/gpio%d/direction", gpioID), value);
    }

    private static void setGPIOValue(int gpioID, int value) {
        writeInt(String.format("/sys/class/gpio/gpio%d/value", gpioID), value);
    }

    private static void setGPIODrive(int gpioID, int drive) {
        writeString(String.format("/sys/class/gpio/gpio%d/drive", gpioID),
                drive == DRIVE_STRONG ? "strong" : "hiz");
    }

    private static void GPIOExport(int gpioID) {
        GPIOExporteds.add(gpioID);
        writeInt("/sys/class/gpio/export", gpioID);
    }

    private static void PWMExport(int pwmChanel) {
        PWMExporteds.add(pwmChanel);
        writeInt("/sys/class/pwm/pwmchip0/export", pwmChanel);
    }

    public static void cleanUp() {
        try {
            for (RandomAccessFile file : GPIOHandlers) {
                if (file != null) {
                    file.close();
                }
            }
        } catch (IOException ex) {
            FileLog.log(Level.FINEST, ex);
        }

        GPIOExporteds.stream().forEach((gpioID) -> {
            writeInt("/sys/class/gpio/unexport", gpioID);
        });
        GPIOExporteds.clear();

        PWMExporteds.stream().forEach((PWMChanel) -> {
            writeInt("/sys/class/pwm/pwmchip0/unexport", PWMChanel);
        });
        PWMExporteds.clear();

        for (int i = 0; i < MAX_SERIAL; i++) {
            try {
                if (serialInputs[i] != null) {
                    serialInputs[i].close();
                }
                if (serialOutputs[i] != null) {
                    serialOutputs[i].close();
                }
                if (serialPorts[i] != null) {
                    serialPorts[i].close();
                }
            } catch (IOException ex) {
                FileLog.log(Level.FINEST, ex);
            }
        }
    }

    private static void writeInt(String filePath, int data) {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            writer.print(data);
        } catch (FileNotFoundException ex) {
            FileLog.log(Level.FINEST, ex, new String[][]{
                {"filePath", filePath},
                {"data", String.valueOf(data)}
            });
        }
    }

    private static void writeLong(String filePath, long data) {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            writer.print(data);
        } catch (FileNotFoundException ex) {
            FileLog.log(Level.FINEST, ex, new String[][]{
                {"filePath", filePath},
                {"data", String.valueOf(data)}
            });
        }
    }

    private static void writeString(String filePath, String data) {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            writer.print(data);
        } catch (FileNotFoundException ex) {
            FileLog.log(Level.FINEST, ex, new String[][]{
                {"filePath", filePath},
                {"data", String.valueOf(data)}
            });
        }
    }

    public static int[][] selectMuxing(int pin, int mode) {
        switch (mode) {
            case OUTPUT:
                return GPIO_MUX_OUTPUT[pin];
            case INPUT:
                return GPIO_MUX_INPUT[pin];
            case INPUT_PULLUP:
                return GPIO_MUX_INPUT_PULLUP[pin];
            case INPUT_PULLDOWN:
                return GPIO_MUX_INPUT_PULLDOWN[pin];
            case ANALOG_INPUT:
                if (pin < MIN_ANALOG || pin > MAX_ANALOG) {
                    return null;
                } else {
                    return GPIO_MUX_ANALOG_INPUT[pin - MIN_ANALOG];
                }
            case PWM:
                for (int[] pwmData : PWM_MAPPING) {
                    if (pwmData[0] == pin) {
                        return GPIO_MUX_PWM[pwmData[2]];
                    }
                }
                return null;
            default:
                return null;
        }
    }

    /* GPIO DEFINE */
    public static final int GPIO_LOW = 0;
    public static final int GPIO_HIGH = 1;
    public static final int GPIO_NONE = 2;
    public static final int INPUT = 3;
    public static final int INPUT_PULLUP = 4;
    public static final int INPUT_PULLDOWN = 5;
    public static final int OUTPUT = 6;
    public static final int ANALOG_INPUT = 7;
    public static final int PWM = 8;
    public static final int DRIVE_STRONG = 9;
    public static final int DRIVE_HIZ = 10;

    public static final int A0 = 14;
    public static final int A1 = 15;
    public static final int A2 = 16;
    public static final int A3 = 17;
    public static final int A4 = 18;
    public static final int A5 = 19;

    private static final int MIN_GPIO = 0;
    private static final int MAX_GPIO = 19;
    private static final int NUM_GPIO = MAX_GPIO - MIN_GPIO;
    private static final int MIN_ANALOG = 14;
    private static final int MAX_ANALOG = 19;
    private static final int MIN_SERIAL = 0;
    private static final int MAX_SERIAL = 2;
    private static final int TIMEOUT_SERIAL = 2000;

    public static final long PWM_MIN_PERIOD = 666666;
    public static final long PWM_MAX_PERIOD = 41666666;
    public static final long PWM_DEFAULT_PERIOD = 5000000;
    public static final int PWM_MIN_VALUE = 0;
    public static final int PWM_MAX_VALUE = 4095;

    private static final int[] GPIO_MAPPING
            = {11, 12, 61, 62, 6, 0, 1, 38, 40, 4, 10, 5, 15, 7, 48, 50, 52, 54, 56, 58};

    private static final int[][] ADC_MAPPING = {
        {14, 0},
        {15, 1},
        {16, 2},
        {17, 3},
        {18, 4},
        {19, 5}
    };

    private static final int[][] PWM_MAPPING = {
        {3, 1, 0},
        {5, 3, 1},
        {6, 5, 2},
        {9, 7, 3},
        {10, 11, 4},
        {11, 9, 5}
    };

    private static final int[][][] GPIO_MUX_OUTPUT = {
        {{32, GPIO_LOW}, {33, GPIO_NONE}},
        {{45, GPIO_LOW}, {28, GPIO_LOW}, {29, GPIO_NONE}},
        {{77, GPIO_LOW}, {34, GPIO_LOW}, {35, GPIO_NONE}, {13, GPIO_NONE}},
        {{64, GPIO_LOW}, {76, GPIO_LOW}, {16, GPIO_LOW}, {17, GPIO_NONE}, {14, GPIO_NONE}},
        {{36, GPIO_LOW}, {37, GPIO_NONE}},
        {{66, GPIO_LOW}, {18, GPIO_LOW}, {19, GPIO_NONE}},
        {{68, GPIO_LOW}, {20, GPIO_LOW}, {21, GPIO_NONE}},
        {{39, GPIO_NONE}},
        {{41, GPIO_NONE}},
        {{70, GPIO_LOW}, {22, GPIO_LOW}, {23, GPIO_NONE}},
        {{74, GPIO_LOW}, {26, GPIO_LOW}, {27, GPIO_NONE}},
        {{44, GPIO_LOW}, {72, GPIO_LOW}, {24, GPIO_LOW}, {25, GPIO_NONE}},
        {{42, GPIO_LOW}, {43, GPIO_NONE}},
        {{46, GPIO_LOW}, {30, GPIO_LOW}, {31, GPIO_NONE}},
        {{49, GPIO_NONE}},
        {{51, GPIO_NONE}},
        {{53, GPIO_NONE}},
        {{55, GPIO_NONE}},
        {{78, GPIO_HIGH}, {60, GPIO_HIGH}, {57, GPIO_NONE}},
        {{79, GPIO_HIGH}, {60, GPIO_HIGH}, {59, GPIO_NONE}}
    };

    private static final int[][][] GPIO_MUX_INPUT = {
        {{32, GPIO_HIGH}, {33, GPIO_NONE}},
        {{45, GPIO_LOW}, {28, GPIO_HIGH}, {29, GPIO_NONE}},
        {{77, GPIO_LOW}, {34, GPIO_HIGH}, {35, GPIO_NONE}, {13, GPIO_NONE}},
        {{64, GPIO_LOW}, {76, GPIO_LOW}, {16, GPIO_HIGH}, {17, GPIO_NONE}, {14, GPIO_NONE}},
        {{36, GPIO_HIGH}, {37, GPIO_NONE}},
        {{66, GPIO_LOW}, {18, GPIO_HIGH}, {19, GPIO_NONE}},
        {{68, GPIO_LOW}, {20, GPIO_HIGH}, {21, GPIO_NONE}},
        {{39, GPIO_NONE},},
        {{41, GPIO_NONE},},
        {{70, GPIO_LOW}, {22, GPIO_HIGH}, {23, GPIO_NONE}},
        {{74, GPIO_LOW}, {26, GPIO_HIGH}, {27, GPIO_NONE}},
        {{44, GPIO_LOW}, {72, GPIO_LOW}, {24, GPIO_HIGH}, {25, GPIO_NONE}},
        {{42, GPIO_HIGH}, {43, GPIO_NONE}},
        {{46, GPIO_LOW}, {30, GPIO_HIGH}, {31, GPIO_NONE}},
        {{49, GPIO_NONE}},
        {{51, GPIO_NONE}},
        {{53, GPIO_NONE}},
        {{55, GPIO_NONE}},
        {{78, GPIO_HIGH}, {60, GPIO_HIGH}, {57, GPIO_NONE}},
        {{79, GPIO_HIGH}, {60, GPIO_HIGH}, {59, GPIO_NONE}}
    };

    private static final int[][][] GPIO_MUX_INPUT_PULLUP = {
        {{32, GPIO_HIGH}, {33, GPIO_HIGH}},
        {{45, GPIO_LOW}, {28, GPIO_HIGH}, {29, GPIO_HIGH}},
        {{77, GPIO_LOW}, {34, GPIO_HIGH}, {35, GPIO_HIGH}, {13, GPIO_NONE}},
        {{64, GPIO_LOW}, {76, GPIO_LOW}, {16, GPIO_HIGH}, {17, GPIO_HIGH}, {14, GPIO_NONE}},
        {{36, GPIO_HIGH}, {37, GPIO_HIGH}},
        {{66, GPIO_LOW}, {18, GPIO_HIGH}, {19, GPIO_HIGH}},
        {{68, GPIO_LOW}, {20, GPIO_HIGH}, {21, GPIO_HIGH}},
        {{39, GPIO_HIGH}},
        {{41, GPIO_HIGH}},
        {{70, GPIO_LOW}, {22, GPIO_HIGH}, {23, GPIO_HIGH}},
        {{74, GPIO_LOW}, {26, GPIO_HIGH}, {27, GPIO_HIGH}},
        {{44, GPIO_LOW}, {72, GPIO_LOW}, {24, GPIO_HIGH}, {25, GPIO_HIGH}},
        {{42, GPIO_HIGH}, {43, GPIO_HIGH}},
        {{46, GPIO_LOW}, {30, GPIO_HIGH}, {31, GPIO_HIGH}},
        {{49, GPIO_HIGH}},
        {{51, GPIO_HIGH}},
        {{53, GPIO_HIGH}},
        {{55, GPIO_HIGH}},
        {{78, GPIO_HIGH}, {60, GPIO_HIGH}, {57, GPIO_HIGH}},
        {{79, GPIO_HIGH}, {60, GPIO_HIGH}, {59, GPIO_HIGH}}
    };

    private static final int[][][] GPIO_MUX_INPUT_PULLDOWN = {
        {{32, GPIO_HIGH}},
        {{45, GPIO_LOW}, {28, GPIO_HIGH}, {29, GPIO_LOW}},
        {{77, GPIO_LOW}, {34, GPIO_HIGH}, {35, GPIO_LOW}, {13, GPIO_NONE}},
        {{64, GPIO_LOW}, {76, GPIO_LOW}, {16, GPIO_HIGH}, {17, GPIO_LOW}, {14, GPIO_NONE}},
        {{36, GPIO_HIGH}, {37, GPIO_LOW}},
        {{66, GPIO_LOW}, {18, GPIO_HIGH}, {19, GPIO_LOW}},
        {{68, GPIO_LOW}, {20, GPIO_HIGH}, {21, GPIO_LOW}},
        {{39, GPIO_LOW}},
        {{41, GPIO_LOW}},
        {{70, GPIO_LOW}, {22, GPIO_HIGH}, {23, GPIO_LOW}},
        {{74, GPIO_LOW}, {26, GPIO_HIGH}, {27, GPIO_LOW}},
        {{44, GPIO_LOW}, {72, GPIO_LOW}, {24, GPIO_HIGH}, {25, GPIO_LOW}},
        {{42, GPIO_HIGH}, {43, GPIO_LOW}},
        {{46, GPIO_LOW}, {30, GPIO_HIGH}, {31, GPIO_LOW}},
        {{49, GPIO_LOW}},
        {{51, GPIO_LOW}},
        {{53, GPIO_LOW}},
        {{55, GPIO_LOW}},
        {{78, GPIO_HIGH}, {60, GPIO_HIGH}, {57, GPIO_LOW}},
        {{79, GPIO_HIGH}, {60, GPIO_HIGH}, {59, GPIO_LOW}}
    };

    private static final int[][][] GPIO_MUX_ANALOG_INPUT = {
        {{48, GPIO_NONE}, {49, GPIO_NONE}},
        {{50, GPIO_NONE}, {51, GPIO_NONE}},
        {{52, GPIO_NONE}, {53, GPIO_NONE}},
        {{54, GPIO_NONE}, {55, GPIO_NONE}},
        {{78, GPIO_LOW}, {60, GPIO_HIGH}, {56, GPIO_NONE}, {57, GPIO_NONE}},
        {{79, GPIO_LOW}, {60, GPIO_HIGH}, {58, GPIO_NONE}, {59, GPIO_NONE}}
    };

    private static final int[][][] GPIO_MUX_PWM = {
        {{64, GPIO_HIGH}, {76, GPIO_LOW}, {16, GPIO_LOW}, {17, GPIO_NONE}, {62, GPIO_NONE}},
        {{66, GPIO_HIGH}, {18, GPIO_LOW}, {19, GPIO_NONE}},
        {{68, GPIO_HIGH}, {20, GPIO_LOW}, {21, GPIO_NONE}},
        {{70, GPIO_HIGH}, {22, GPIO_LOW}, {23, GPIO_NONE}},
        {{74, GPIO_HIGH}, {26, GPIO_LOW}, {27, GPIO_NONE}},
        {{72, GPIO_HIGH}, {24, GPIO_LOW}, {25, GPIO_NONE}}
    };

    private static final int[][][] GPIO_MUX_SERIAL = {
        {{32, GPIO_HIGH}, {28, GPIO_LOW}, {33, GPIO_NONE}, {29, GPIO_NONE}, {45, GPIO_HIGH}},
        {{34, GPIO_HIGH}, {16, GPIO_LOW}, {35, GPIO_NONE}, {17, GPIO_NONE}, {76, GPIO_HIGH}, {77, GPIO_HIGH}}
    };

}
