/*
 *  Copyright Nguyen Duong Kim Hao @ 2014
 */
package laserbot_galileo.svgPlotter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import static laserbot_galileo.Common.*;
import laserbot_galileo.laserDraw.Image;
import org.imgscalr.Scalr;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author KimHao
 */
public class SVGProcess {

    private final static int MIN_SCANLINE = 2;

    private static ArrayList<Path> pathsArray;
    private static int max_X, max_Y, distance;
    public static BufferedImage preview;

    public static void proccess(int delayX, int delayY, int delayXY, int scanLine, int maxScanLine) throws Exception {
        pathsArray = new ArrayList<>();
        loadData(DRAWNOW_SVG_PATH + "drawing.svg", scanLine, maxScanLine);
        alignMargin();
        reorderPaths();
        saveImage();
        saveData(delayX, delayY, delayXY);
        pathsArray = null;
        //System.out.println("Total " + pathsArray.size() + " paths");
    }

    private static void saveData(int delayX, int delayY, int delayXY) {
        distance = 0;
        Point curPoint = new Point(0, 0);
        ArrayList<Short> data = new ArrayList<>();
        for (Path path : pathsArray) {
            for (int i = 0; i < path.points.size(); i++) {
                if (i == 1) {
                    data.add((short) -2);
                }
                Point point = path.points.get(i);
                distance += point.distance(curPoint);
                curPoint = point;
                data.add((short) (point.x * PIXEL_X_MULTIPLIER));
                data.add((short) (point.y * PIXEL_Y_MULTIPLIER));
            }
            data.add((short) -1);
        }
        short[] arrData = new short[data.size()];
        for (int i = 0; i < data.size(); i++) {
            arrData[i] = data.get(i);
        }

        try (ObjectOutputStream outputStream = new ObjectOutputStream(
                new FileOutputStream(DRAWNOW_SVG_PATH + "xyData.array"))) {
            outputStream.writeObject(arrData);
            outputStream.writeInt(delayX);
            outputStream.writeInt(delayY);
            outputStream.writeInt(delayXY);
            outputStream.writeInt(distance);
        } catch (IOException ex) {
            Logger.getLogger(SVGProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println("Distance = " + distance);
    }

    private static void loadData(String path, int scanLine, int maxScanLine) throws Exception {
        try {
            InputStream xmlInput = new FileInputStream(path);
            SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
            final int brightnessStep;
            if (scanLine == 0) {
                brightnessStep = 255 / (maxScanLine - MIN_SCANLINE + 1);
            } else {
                brightnessStep = 0;
            }
            DefaultHandler handler = new DefaultHandler() {
                @Override
                public void startElement(String uri, String localName,
                        String qName, Attributes attributes) throws SAXException {
                    if ("path".equals(qName)) {
                        String style = attributes.getValue("style");
                        int idx = style == null ? -1 : style.indexOf("fill:#");
                        Point translate = null;
                        String transform = attributes.getValue("transform");
                        if (transform != null && transform.startsWith("translate(")) {
                            int open = transform.indexOf("(");
                            int comma = transform.indexOf(",");
                            int close = transform.indexOf(")");
                            int x = Path.parseInt(transform.substring(open + 1, comma));
                            int y = Path.parseInt(transform.substring(comma + 1, close));
                            translate = new Point(x, y);
                        }
                        try {
                            if (idx == -1) {
                                pathsArray.addAll(Path.parseSVG(attributes.getValue("d"), translate, false, 0));
                            } else {
                                if (scanLine == 0) {
                                    Color color = Color.decode(style.substring(idx + 5).substring(0, 7));
                                    int brightness = (int) Math.sqrt(
                                            color.getRed() * color.getRed() * 0.241
                                            + color.getGreen() * color.getGreen() * 0.691
                                            + color.getBlue() * color.getBlue() * 0.068);
                                    pathsArray.addAll(Path.parseSVG(attributes.getValue("d"), translate, true,
                                            MIN_SCANLINE + (brightness / brightnessStep)));
                                } else {
                                    pathsArray.addAll(Path.parseSVG(attributes.getValue("d"), translate, true, scanLine));
                                }
                            }
                        } catch (Exception e) {
                            throw new SAXException(e);
                        }
                    }
                }
            };
            saxParser.parse(xmlInput, handler);
        } catch (ParserConfigurationException | IOException ex) {
            Logger.getLogger(SVGProcess.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            throw ex.getException();
        }
    }

    private static void saveImage() {
        preview = new BufferedImage(max_X + 1, max_Y + 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = preview.createGraphics();
        //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        File backFile = new File(DRAWNOW_SVG_PATH + "background.jpg");
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
        g2d.setStroke(new BasicStroke(1));
        g2d.setColor(Color.black);
        Point prevPoint = new Point(0, 0);
        for (Path path : pathsArray) {
            for (int j = 0; j < path.points.size(); j++) {
                Point point = path.points.get(j);
                if (j > 0) {
                    g2d.drawLine(prevPoint.x, prevPoint.y, point.x, point.y);
                }
                prevPoint = point;
            }
        }
        //ImageIO.write(preview, "png", new File(DRAWNOW_SVG_PATH + "image.png"));

        BufferedImage biThumb = deepCopy(preview);
        for (int i = 0; i < biThumb.getWidth(); i++) {
            for (int j = 0; j < biThumb.getHeight(); j++) {
                biThumb.setRGB(i, j, biThumb.getRGB(i, j) == 0xFF000000 ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        Image thumb = new Image(biThumb).createThumbnail(Scalr.Method.ULTRA_QUALITY);
        thumb.grayscale();
        thumb.save(DRAWNOW_SVG_PATH + "lcdImage.gif");
    }

    private static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    private static void alignMargin() throws Exception {
        int min_x = Integer.MAX_VALUE;
        int min_y = Integer.MAX_VALUE;
        for (Path path : pathsArray) {
            if (path.minPoint == null) {
                continue;
            }
            if (path.minPoint.x < min_x) {
                min_x = path.minPoint.x;
            }
            if (path.minPoint.y < min_y) {
                min_y = path.minPoint.y;
            }
        }
        if (min_x != 0 || min_y != 0) {
            for (Path path : pathsArray) {
                path.alignMargin(min_x, min_y);
            }
        }

        max_X = Integer.MIN_VALUE;
        max_Y = Integer.MIN_VALUE;
        pathsArray.stream().filter((path) -> !(path.maxPoint == null)).forEach((path) -> {
            if (path.maxPoint.x > max_X) {
                max_X = path.maxPoint.x;
            }
            if (path.maxPoint.y > max_Y) {
                max_Y = path.maxPoint.y;
            }
        });
        max_X -= min_x;
        max_Y -= min_y;
        if (max_X * PIXEL_X_MULTIPLIER > MAX_X || max_Y * PIXEL_Y_MULTIPLIER > MAX_Y) {
            throw new Exception("File is over-size (Max Width = 3500, Max Height = 3000)");
        }
    }

    private static void reorderPaths() {
        ArrayList<Path> newPaths = new ArrayList<>();
        Point curPoint = new Point(0, 0);
        Path nearestPath = new Path();
        while (!pathsArray.isEmpty()) {
            int minDistance = Integer.MAX_VALUE;
            int d;
            for (Path path : pathsArray) {
                if ((d = path.getDistance(curPoint)) < minDistance) {
                    minDistance = d;
                    nearestPath = path;
                }
            }
            nearestPath.checkReverse();
            curPoint = nearestPath.getLastPoint();
            newPaths.add(nearestPath);
            pathsArray.remove(nearestPath);
        }
        pathsArray = newPaths;
    }

}
