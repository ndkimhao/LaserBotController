/*
 *  Copyright Nguyen Duong Kim Hao @ 2014
 */
package laserbot_galileo.laserDraw;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import laserbot_galileo.FileLog;
import static laserbot_galileo.Common.*;
import org.imgscalr.Scalr;

/**
 *
 * @author KimHao
 */
public final class Image {

    public BufferedImage image;
    public float ratio;
    public int oldWidth, oldHeight, oldRatio;
    public int newWidth, newHeight;
    public short[][] laserControl;
    public int max_X = MAX_X / 2, max_Y = MAX_Y;

    public Image() {
    }

    public Image(BufferedImage image) {
        this.image = image;
        ratio = (float) image.getWidth() / (float) image.getHeight();
        oldWidth = oldHeight = oldRatio = -1;
        newWidth = image.getWidth();
        newHeight = image.getHeight();
    }

    public Image(BufferedImage image, int oldWidth, int oldHeight) {
        this.image = image;
        ratio = (float) image.getWidth() / (float) image.getHeight();
        this.oldWidth = oldWidth;
        this.oldHeight = oldHeight;
        oldRatio = oldWidth / oldHeight;
        newWidth = image.getWidth();
        newHeight = image.getHeight();
    }

    public Image(String path) {
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException ex) {
            FileLog.log(Level.SEVERE, ex);
        }
        ratio = (float) image.getWidth() / (float) image.getHeight();
        oldWidth = oldHeight = oldRatio = -1;
        newWidth = image.getWidth();
        newHeight = image.getHeight();
    }

    public void save(String path) {
        try {
            ImageIO.write(image, "gif", new File(path));
        } catch (IOException ex) {
            FileLog.log(Level.SEVERE, ex);
        }
    }

    public void saveLaserControl(String path, int oldWidth, int delay_X, int delay_Y, int quality) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(path))) {
            outputStream.writeObject(laserControl);
            outputStream.writeInt(newWidth);
            outputStream.writeInt(newHeight);
            outputStream.writeInt(oldWidth);
            outputStream.writeInt(delay_X);
            outputStream.writeInt(delay_Y);
            outputStream.writeInt(quality);
        } catch (IOException ex) {
            FileLog.log(Level.SEVERE, ex);
        }
    }

    public int delay_X, delay_Y, quality;

    public void loadLaserControl(String path) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(path))) {
            laserControl = (short[][]) inputStream.readObject();
            newWidth = inputStream.readInt();
            newHeight = inputStream.readInt();

            oldWidth = inputStream.readInt();
            delay_X = inputStream.readInt();
            delay_Y = inputStream.readInt();
            quality = inputStream.readInt();
        } catch (IOException | ClassNotFoundException ex) {
            FileLog.log(Level.SEVERE, ex);
        }
    }

    public void increaseSize() {
        newHeight++;
        newWidth = (int) (ratio * newHeight);
        checkSize();
    }

    public void decreaseSize() {
        newHeight--;
        newWidth = (int) (ratio * newHeight);
        checkSize();
    }

    private void checkSize() {
        if (newWidth < MIN_X || newHeight < MIN_Y) {
            increaseSize();
        } else if (newWidth > max_X || newHeight > max_Y) {
            decreaseSize();
        }
    }

    public void checkSize_while() {
        while (newWidth < MIN_X || newHeight < MIN_Y) {
            increaseSize();
        }
        while (newWidth > max_X || newHeight > max_Y) {
            decreaseSize();
        }
    }

    public void setQuality(int quality) {
        if (quality == QUAL_HIGH) {
            max_X = MAX_X / 2;
            max_Y = MAX_Y / 1;
        } else if (quality == QUAL_MED) {
            max_X = MAX_X / 4;
            max_Y = MAX_Y / 2;
        } else if (quality == QUAL_LOW) {
            max_X = MAX_X / 6;
            max_Y = MAX_Y / 3;
        }
    }

    public void grayscale() {
        int width = image.getWidth();
        int height = image.getHeight();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int rgb = image.getRGB(j, i);
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;
                int grey = (int) (red * 0.2126f + green * 0.7152f + blue * 0.0722f);
                image.setRGB(j, i, grey << 16 | grey << 8 | grey);
            }
        }
    }

    public void dither() {
        int width = image.getWidth();
        int height = image.getHeight();
        laserControl = new short[width + 2][height + 2];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = image.getRGB(i, j);
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;
                laserControl[i][j] = (short) (red * 0.2126f + green * 0.7152f + blue * 0.0722f);
                //laserControl[i][j] = (short) ((red + green + blue) / 3);
            }
        }
        image.flush();
        image = null;
        for (int i = 0; i < height; i++) {
            /*if (i % 10 == 0) {
             System.out.println("  Process line " + i);
             }*/
            for (int j = 0; j < width; j++) {
                short gray = laserControl[j][i];
                int error;
                if (gray < DITHER_THRESHOLD) {
                    error = gray;
                    gray = 0;
                } else {
                    error = gray - 255;
                    gray = 255;
                }
                laserControl[j][i] = gray;
                error /= DITHER_ERROR_DIFFUSION;
                laserControl[j][i + 1] += error;
                if (j > 0) {
                    laserControl[j - 1][i + 1] += error;
                }
                laserControl[j + 1][i + 1] += error;
                laserControl[j][i + 2] += error;
                laserControl[j + 1][i] += error;
                laserControl[j + 2][i] += error;
            }
        }
        /*for (int i = 0; i < width; i++) {
         for (int j = 0; j < height; j++) {
         image.setRGB(i, j, (laserControl[i][j] == 255 ? 0xFFFFFF : 0x000000));
         }
         }*/
    }

    public int[][] getPixels() {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] pixels = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                pixels[i][j] = image.getRGB(i, j);
            }
        }
        return pixels;
    }

    public void resize(Scalr.Method quality) {
        if (image.getWidth() != newWidth || image.getHeight() != newHeight) {
            Scalr.Mode mode = ratio > ((float) newWidth / (float) newHeight)
                    ? Scalr.Mode.FIT_TO_WIDTH : Scalr.Mode.FIT_TO_HEIGHT;
            BufferedImage oldImage = image;
            image = Scalr.resize(image, quality, mode, newWidth, newHeight);
            oldImage.flush();
            newWidth = image.getWidth();
            newHeight = image.getHeight();
        }
    }

    public void resize(Scalr.Method quality, boolean isFlush) {
        if (image.getWidth() != newWidth || image.getHeight() != newHeight) {
            Scalr.Mode mode = ratio > ((float) newWidth / (float) newHeight)
                    ? Scalr.Mode.FIT_TO_WIDTH : Scalr.Mode.FIT_TO_HEIGHT;
            BufferedImage oldImage = image;
            image = Scalr.resize(image, quality, mode, newWidth, newHeight);
            if (isFlush) {
                oldImage.flush();
            }
            newWidth = image.getWidth();
            newHeight = image.getHeight();
        }
    }

    public Image createThumbnail(Scalr.Method quality) {
        Scalr.Mode mode = ratio > ((float) SCREEN_WIDTH / (float) SCREEN_HEIGHT)
                ? Scalr.Mode.FIT_TO_WIDTH : Scalr.Mode.FIT_TO_HEIGHT;
        BufferedImage tmp = Scalr.resize(image, quality, mode, SCREEN_WIDTH, SCREEN_HEIGHT);
        BufferedImage result = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = result.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        tmp.flush();
        return new Image(result, tmp.getWidth(), tmp.getHeight());
    }

    public Image createCopy() {
        ColorModel cm = image.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = image.copyData(null);
        return new Image(new BufferedImage(cm, raster, isAlphaPremultiplied, null));
    }

}
