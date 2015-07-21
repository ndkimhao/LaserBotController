/*
 *  Copyright Nguyen Duong Kim Hao @ 2014
 */
package laserbot_galileo.svgPlotter;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import static laserbot_galileo.Common.DRAWNOW_SVG_PATH;
import laserbot_galileo.laserDraw.Image;

/**
 *
 * @author KimHao
 */
public class DrawNowSVG {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        /*int delayX = readInt("Enter delay X", DEFAULT_STEP_DRAWLINE_X_DELAY);
        int delayY = readInt("Enter delay Y", DEFAULT_STEP_DRAWLINE_Y_DELAY);
        int delayXY = readInt("Enter delay XY", DEFAULT_STEP_DRAWLINE_XY_DELAY);
        int scanLine = readInt("Enter scanline", 0, "2->5");*/
        int delayX = 0, delayY = 0, delayXY = 0, scanLine = 0;
        try {
            SVGProcess.proccess(delayX, delayY, delayXY, scanLine, 5);
            new Image(SVGProcess.preview).save(DRAWNOW_SVG_PATH + "preview.jpg");
        } catch (Exception ex) {
            Logger.getLogger(DrawNowSVG.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static int readInt(String mess, int deaultVal) {
        System.out.print(String.format("%s (%d): ", mess, deaultVal));
        int ret = deaultVal;
        try {
            ret = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
        }
        return ret;
    }

    private static int readInt(String mess, int deaultVal, String hint) {
        System.out.print(String.format("%s (%d - %s): ", mess, deaultVal, hint));
        int ret = deaultVal;
        try {
            ret = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
        }
        return ret;
    }

}
