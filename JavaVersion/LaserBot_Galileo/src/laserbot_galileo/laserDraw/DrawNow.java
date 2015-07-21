/*
 *  Copyright Nguyen Duong Kim Hao @ 2014
 */
package laserbot_galileo.laserDraw;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import static laserbot_galileo.Common.*;
import laserbot_galileo.svgPlotter.SVGProcess;
import org.imgscalr.Scalr;

/**
 *
 * @author KimHao
 */
public class DrawNow {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Image image = new Image(DRAWNOW_PATH + "image.jpg");

        int width, height;
        final float ratio = image.ratio;
        width = readInt("Enter width", 0, "1/10mm");
        if (width == 0) {
            height = readInt("Enter height", 0, "1/10mm");
            if (height != 0) {
                width = readInt("Enter width", (int) (height * ratio), "1/10mm");
            }
        } else {
            height = readInt("Enter height", (int) (width / ratio), "1/10mm");
        }

        int quality = readInt("Enter quality", DEFAULT_QUALITY);
        int step_X = 1, step_Y = 1;
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

        if (width != 0) {
            image.newWidth = width * 4 / step_X;
        }
        if (height != 0) {
            image.newHeight = height * 2 / step_Y;
        }

        int delay_X = readInt("Enter delay X", DEFAULT_X_DELAY);
        int delay_Y = readInt("Enter delay Y", DEFAULT_Y_DELAY);

        Image thumb = image.createThumbnail(Scalr.Method.ULTRA_QUALITY);
        thumb.grayscale();
        thumb.save(DRAWNOW_PATH + "\\lcdImage.gif");

        image.resize(Scalr.Method.QUALITY);
        image.dither();
        savePreview(image.newWidth, image.newHeight, image.laserControl);

        image.saveLaserControl(DRAWNOW_PATH + "\\laserControl.array", thumb.oldWidth, delay_X, delay_Y, quality);

        System.out.println("Actual width = " + image.newWidth);
        System.out.println("Actual height = " + image.newHeight);
        System.out.println("Array size = " + image.laserControl.length * image.laserControl[0].length);
    }

    private static void savePreview(int width, int height, short[][] laserControl) {
        BufferedImage preview = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = preview.createGraphics();
        File backFile = new File(DRAWNOW_PATH + "background.jpg");
        if (backFile.exists()) {
            try {
                BufferedImage backImage = ImageIO.read(backFile);
                for (int i = 0; i < preview.getWidth(); i += backImage.getWidth()) {
                    for (int j = 0; j < preview.getHeight(); j += backImage.getHeight()) {
                        g2d.drawImage(backImage, i, j, null);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(SVGProcess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        g2d.dispose();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (laserControl[i][j] == 0) {
                    preview.setRGB(i, j, 0xFF000000);
                }
            }
        }

        try {
            ImageIO.write(preview, "jpg", new File(DRAWNOW_PATH + "preview.jpg"));
        } catch (IOException ex) {
            Logger.getLogger(DrawNow.class.getName()).log(Level.SEVERE, null, ex);
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
