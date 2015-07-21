/*
 *  Copyright Nguyen Duong Kim Hao @ 2014
 */
package laserbot_galileo.io;

import laserbot_galileo.laserDraw.Image;
import static laserbot_galileo.Common.*;
import static laserbot_galileo.io.GPIO.*;

/**
 *
 * @author KimHao
 */
public class StepperControl {

    public static int STEP_DRAWLINE_X_DELAY = DEFAULT_STEP_DRAWLINE_X_DELAY;
    public static int STEP_DRAWLINE_Y_DELAY = DEFAULT_STEP_DRAWLINE_Y_DELAY;
    public static int STEP_DRAWLINE_XY_DELAY = DEFAULT_STEP_DRAWLINE_XY_DELAY;

    public static int curX;
    public static int curY;

    public static boolean dir_X;
    public static boolean dir_Y;

    public static void begin() {
        pinMode(STEPPER_X_LIMIT, INPUT_PULLUP);
        pinMode(STEPPER_X_DIR, OUTPUT);
        pinMode(STEPPER_X_CLK, OUTPUT);
        digitalWrite(STEPPER_X_DIR, LOW);
        digitalWrite(STEPPER_X_CLK, LOW);
        dir_X = BACKWARD;
        curX = 0;

        pinMode(STEPPER_Y_LIMIT, INPUT_PULLUP);
        pinMode(STEPPER_Y_DIR, OUTPUT);
        pinMode(STEPPER_Y_CLK, OUTPUT);
        digitalWrite(STEPPER_Y_DIR, LOW);
        digitalWrite(STEPPER_Y_CLK, LOW);
        dir_Y = BACKWARD;
        curY = 0;

        pinMode(LASER_CONTROL, OUTPUT);
        digitalWrite(LASER_CONTROL, false);

        pinMode(13, OUTPUT);
        digitalWrite(13, LOW);
    }

    public static void findHome() {
        dirXY(BACKWARD);
        boolean isRunX = true;
        boolean isRunY = true;
        while (isRunX || isRunY) {
            if (isRunX && isNotLimitX()) {
                digitalWrite(STEPPER_X_CLK, HIGH);
            } else {
                isRunX = false;
            }
            if (isRunY && isNotLimitY()) {
                digitalWrite(STEPPER_Y_CLK, HIGH);
            } else {
                isRunY = false;
            }
            delayMicros(PULSE_DELAY);
            if (isRunX) {
                digitalWrite(STEPPER_X_CLK, LOW);
            }
            if (isRunY) {
                digitalWrite(STEPPER_Y_CLK, LOW);
            }
            if (!(isRunX && isRunY)) {
                delayMicros(STEP_MAXSPEED_DELAY);
            }
        }
        curX = curY = 0;
        dirXY(FORWARD);
    }

    public static void findHome_X() {
        dirX(BACKWARD);
        while (isNotLimitX()) {
            stepX();
            delayMicros(STEP_MAXSPEED_DELAY);
        }
        curX = 0;
        dirX(FORWARD);
    }

    public static void laser(boolean state) {
        digitalWrite(LASER_CONTROL, state);
    }

    public static boolean isNotLimitX() {
        return digitalRead(STEPPER_X_LIMIT) != BUTTON_PRESS;
    }

    public static boolean isNotLimitY() {
        return digitalRead(STEPPER_Y_LIMIT) != BUTTON_PRESS;
    }

    public static void stepX() {
        digitalWrite(STEPPER_X_CLK, HIGH);
        delayMicros(PULSE_DELAY);
        digitalWrite(STEPPER_X_CLK, LOW);
    }

    public static void stepX(int num, int delayTime) {
        for (int i = 0; i < num; i++) {
            digitalWrite(STEPPER_X_CLK, HIGH);
            delayMicros(PULSE_DELAY);
            digitalWrite(STEPPER_X_CLK, LOW);
            delayMicros(delayTime);
        }
    }

    public static void dirX(boolean dir) {
        dir_X = dir;
        digitalWrite(STEPPER_X_DIR, dir);
    }

    public static void dirXY(boolean dir) {
        dir_X = dir_Y = dir;
        digitalWrite(STEPPER_X_DIR, dir);
        digitalWrite(STEPPER_Y_DIR, dir);
    }

    public static void stepY() {
        digitalWrite(STEPPER_Y_CLK, HIGH);
        delayMicros(PULSE_DELAY);
        digitalWrite(STEPPER_Y_CLK, LOW);
    }

    public static void stepXY() {
        digitalWrite(STEPPER_Y_CLK, HIGH);
        digitalWrite(STEPPER_X_CLK, HIGH);
        delayMicros(PULSE_DELAY);
        digitalWrite(STEPPER_Y_CLK, LOW);
        digitalWrite(STEPPER_X_CLK, LOW);
    }

    public static void goToXY(int x, int y) {
        dirX(curX < x ? FORWARD : BACKWARD);
        dirY(curY < y ? FORWARD : BACKWARD);
        int incX = dir_X == FORWARD ? 1 : -1;
        int incY = dir_Y == FORWARD ? 1 : -1;
        while (curX != x || curY != y) {
            if (curX != x) {
                digitalWrite(STEPPER_X_CLK, HIGH);
            }
            if (curY != y) {
                digitalWrite(STEPPER_Y_CLK, HIGH);
            }
            delayMicros(PULSE_DELAY);
            if (curX != x) {
                digitalWrite(STEPPER_X_CLK, LOW);
                curX += incX;
            }
            if (curY != y) {
                digitalWrite(STEPPER_Y_CLK, LOW);
                curY += incY;
            }
            delayMicros(STEP_XY_DELAY);
        }
    }

    // Bresenham's line algorithm
    public static void drawLine(int x2, int y2) {
        if (x2 > MAX_X || y2 > MAX_Y) {
            return;
        }

        // delta of exact value and rounded value of the dependant variable
        int d = 0;

        int dy = Math.abs(y2 - curY);
        int dx = Math.abs(x2 - curX);

        int dy2 = (dy << 1); // slope scaling factors to avoid floating
        int dx2 = (dx << 1); // point
        int dxy2 = Math.abs((dx << 1) - (dy << 1));

        int ix = curX < x2 ? 1 : -1; // increment direction
        int iy = curY < y2 ? 1 : -1;

        if (ix == 1) {
            dirX(FORWARD);
        } else {
            dirX(BACKWARD);
        }
        if (iy == 1) {
            dirY(FORWARD);
        } else {
            dirY(BACKWARD);
        }

        if (dy <= dx) {
            for (;;) {
                if (curX == x2) {
                    break;
                }
                if (d > dx) {
                    stepXY();
                    delayMicros(STEP_DRAWLINE_XY_DELAY);
                    curX += ix;
                    curY += iy;
                    d -= dxy2;
                } else {
                    stepX();
                    delayMicros(STEP_DRAWLINE_X_DELAY);
                    curX += ix;
                    d += dy2;
                }
            }
        } else {
            for (;;) {
                if (curY == y2) {
                    break;
                }
                if (d > dy) {
                    stepXY();
                    delayMicros(STEP_DRAWLINE_XY_DELAY);
                    curY += iy;
                    curX += ix;
                    d -= dxy2;
                } else {
                    stepY();
                    delayMicros(STEP_DRAWLINE_Y_DELAY);
                    curY += iy;
                    d += dx2;
                }
            }
        }
    }

    public static void drawLine_maxSpeed(int x2, int y2) {
        if (x2 > MAX_X || y2 > MAX_Y) {
            return;
        }

        // delta of exact value and rounded value of the dependant variable
        int d = 0;

        int dy = Math.abs(y2 - curY);
        int dx = Math.abs(x2 - curX);

        int dy2 = (dy << 1); // slope scaling factors to avoid floating
        int dx2 = (dx << 1); // point

        int ix = curX < x2 ? 1 : -1; // increment direction
        int iy = curY < y2 ? 1 : -1;

        if (ix == 1) {
            dirX(FORWARD);
        } else {
            dirX(BACKWARD);
        }
        if (iy == 1) {
            dirY(FORWARD);
        } else {
            dirY(BACKWARD);
        }

        if (dy <= dx) {
            for (;;) {
                if (curX == x2) {
                    break;
                }
                if (d > dx) {
                    stepXY();
                    delayMicros(STEP_XY_DELAY);
                    curX += ix;
                    curY += iy;
                    d += dy2 - dx2;
                } else {
                    stepX();
                    delayMicros(STEP_XY_DELAY);
                    curX += ix;
                    d += dy2;
                }
            }
        } else {
            for (;;) {
                if (curY == y2) {
                    break;
                }
                if (d > dy) {
                    stepXY();
                    delayMicros(STEP_XY_DELAY);
                    curY += iy;
                    curX += ix;
                    d += dx2 - dy2;
                } else {
                    stepY();
                    delayMicros(STEP_XY_DELAY);
                    curY += iy;
                    d += dx2;
                }
            }
        }
    }

    public static void goToImagePosition_line(Image image, int quality) {
        int width = image.newWidth;
        int height = image.newHeight;
        if (quality == QUAL_HIGH) {
            width *= 2;
            height *= 1;
        } else if (quality == QUAL_MED) {
            width *= 4;
            height *= 2;
        } else if (quality == QUAL_LOW) {
            width *= 6;
            height *= 3;
        }
        drawLine_maxSpeed(width, height);
    }

    public static void goToImagePosition(Image image, int quality) {
        int width = image.newWidth;
        int height = image.newHeight;
        if (quality == QUAL_HIGH) {
            width *= 2;
            height *= 1;
        } else if (quality == QUAL_MED) {
            width *= 4;
            height *= 2;
        } else if (quality == QUAL_LOW) {
            width *= 6;
            height *= 3;
        }
        goToXY(width, height);
    }

//    public static void stepY(int num, int delayTime) {
//        if (num == 1) {
//            digitalWrite(STEPPER_Y_CLK, HIGH);
//            delayMicros(PULSE_DELAY);
//            digitalWrite(STEPPER_Y_CLK, LOW);
//            delayMicros(delayTime);
//        } else {
//            for (int i = 0; i < num; i++) {
//                digitalWrite(STEPPER_Y_CLK, HIGH);
//                delayMicros(PULSE_DELAY);
//                digitalWrite(STEPPER_Y_CLK, LOW);
//                delayMicros(delayTime);
//            }
//        }
//    }
    public static void stepY(int num, int delayTime) {
        if (num == 1) {
            digitalWrite(STEPPER_Y_CLK, HIGH);
            delayMicros(PULSE_DELAY);
            digitalWrite(STEPPER_Y_CLK, LOW);
            if (delayTime > Y_MAXSPEED_DELAY) {
                delayMicros(delayTime - Y_MAXSPEED_DELAY);
            }
        } else if (num == 2) {
            digitalWrite(STEPPER_Y_CLK, HIGH);
            delayMicros(PULSE_DELAY);
            digitalWrite(STEPPER_Y_CLK, LOW);
            delayMicros(delayTime);
            digitalWrite(STEPPER_Y_CLK, HIGH);
            delayMicros(PULSE_DELAY);
            digitalWrite(STEPPER_Y_CLK, LOW);
            if (delayTime > Y_MAXSPEED_DELAY) {
                delayMicros(delayTime - Y_MAXSPEED_DELAY);
            }
        } else {
            digitalWrite(STEPPER_Y_CLK, HIGH);
            delayMicros(PULSE_DELAY);
            digitalWrite(STEPPER_Y_CLK, LOW);
            delayMicros(delayTime);
            digitalWrite(STEPPER_Y_CLK, HIGH);
            delayMicros(PULSE_DELAY);
            digitalWrite(STEPPER_Y_CLK, LOW);
            delayMicros(delayTime);
            digitalWrite(STEPPER_Y_CLK, HIGH);
            delayMicros(PULSE_DELAY);
            digitalWrite(STEPPER_Y_CLK, LOW);
            if (delayTime > Y_MAXSPEED_DELAY) {
                delayMicros(delayTime - Y_MAXSPEED_DELAY);
            }
        }
    }

    public static void dirY(boolean dir) {
        dir_Y = dir;
        digitalWrite(STEPPER_Y_DIR, dir);
    }

}
