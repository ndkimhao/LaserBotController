package laserbot_galileo.svgPlotter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

/*
 *  Copyright Nguyen Duong Kim Hao @ 2014
 */
/**
 *
 * @author KimHao
 */
public final class Path {

    //public static int FILL_POLYGON_SCANLINE = 4;
    private static final Pattern splitPattern = Pattern.compile("[\\s,]");

    public ArrayList<Point> points = new ArrayList<>();
    public Point minPoint = null, maxPoint = null;
    private boolean needReverse = false;

    public Path() {
    }

    public Path(ArrayList<Point> points) {
        this.points = points;
        calcMinMaxPoint();
    }

    /*public static ArrayList<Path> parseSVG(String svg, boolean isFill, int scanLine) throws Exception {
     ArrayList<Path> outPaths = new ArrayList<>();
     String[] data = splitPattern.split(svg);
     String tmp;
     Path path = null;
     for (int i = 0; i < data.length;) {
     tmp = data[i++];
     switch (tmp) {
     case "M":
     if (i > 1) {
     path.calcMinMaxPoint();
     outPaths.add(path);
     }
     path = new Path();
     break;
     case "z":
     path.points.add(path.getFirstPoint().copy());
     break;
     case "m":
     throw new Exception("Program does not support parse relative path (m) !");
     case "L":
     case "l":
     case "H":
     case "h":
     case "V":
     case "v":
     case "C":
     case "c":
     case "S":
     case "s":
     case "A":
     case "a":
     case "Z":
     case "Q":
     case "q":
     case "T":
     case "t":
     throw new Exception("Unsupported operation (" + tmp + ") !");
     default:
     if (tmp.startsWith("L")) {
     throw new Exception("File is not a plain SVG !");
     }
     path.points.add(new Point(parseInt(tmp), parseInt(data[i++])));
     break;
     }
     }
     path.calcMinMaxPoint();
     outPaths.add(path);
     if (isFill) {
     return fillPolygon(createEdges(outPaths), outPaths, scanLine);
     } else {
     return outPaths;
     }
     }*/
    public static ArrayList<Path> parseSVG(String svg, Point translate, boolean isFill, int scanLine) throws Exception {
        ArrayList<Path> outPaths = new ArrayList<>();
        if (!(svg.startsWith("M") || svg.startsWith("m"))) {
            throw new Exception("Unsupported start operation (" + svg.substring(0, 1) + ") !");
        }

        String[] data = splitPattern.split(svg);
        int mode = -1; // 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15 = M,m,L,l,H,h,V,v,C,c,S,s,A,a,Z,z
        ArrayList<Point> pathpoints = null;

        PointD cntrlpt = new PointD(0, 0); // special point for s commands
        PointD relpt = new PointD(0, 0); // for relative commands
        PointD startpt = new PointD(0, 0); // for z commands

        for (int i = 0; i < data.length; i++) {
            if (mode == 0) {
                mode = 2;
            } else if (mode == 1) {
                mode = 3;
            }
            switch (data[i].charAt(0)) {
                case 'M':
                    mode = 0;
                    i++;
                    break;
                case 'L':
                    mode = 2;
                    i++;
                    break;
                case 'H':
                    mode = 4;
                    i++;
                    break;
                case 'V':
                    mode = 6;
                    i++;
                    break;
                case 'C':
                    mode = 8;
                    i++;
                    break;
                case 'S':
                    mode = 10;
                    i++;
                    break;
                case 'A':
                    mode = 12;
                    i++;
                    break;
                case 'z':
                case 'Z':
                    mode = 14;
                    break;
                case 'Q':
                case 'q':
                case 'T':
                case 't':
                case 'm':
                case 'l':
                case 'h':
                case 'v':
                case 'c':
                case 's':
                case 'a':
                    throw new Exception("Unsupported operation (" + data[i].charAt(0) + ") !");
            }
            switch (mode) {
                case 0: {
                    if (pathpoints != null && !pathpoints.isEmpty()) {
                        Path p = new Path(pathpoints);
                        if (translate != null) {
                            p.alignMargin(-translate.x, -translate.y);
                        }
                        outPaths.add(p);
                    }
                    pathpoints = new ArrayList<>();
                    double tmpx = Double.parseDouble(data[i]);
                    double tmpy = Double.parseDouble(data[i + 1]);
                    relpt.newValue(tmpx, tmpy);
                    startpt.newValue(tmpx, tmpy);
                    pathpoints.add(new Point((int) tmpx, (int) tmpy));
                    i++;
                    break;
                }
                case 2: {
                    double tmpx = Double.parseDouble(data[i]);
                    double tmpy = Double.parseDouble(data[i + 1]);
                    relpt.newValue(tmpx, tmpy);
                    pathpoints.add(new Point((int) tmpx, (int) tmpy));
                    i++;
                    break;
                }
                case 4: {
                    double tmpx = Double.parseDouble(data[i]);
                    pathpoints.add(new Point((int) tmpx, (int) relpt.y));
                    relpt.newValue(tmpx, relpt.y);
                    break;
                }
                case 6: {
                    double tmpy = Double.parseDouble(data[i]);
                    pathpoints.add(new Point((int) relpt.x, (int) tmpy));
                    relpt.newValue(relpt.x, tmpy);
                    break;
                }
                case 8: {
                    double x = relpt.x;
                    double y = relpt.y;
                    double xc1 = Double.parseDouble(data[i]);
                    double yc1 = Double.parseDouble(data[i + 1]);
                    double xc2 = Double.parseDouble(data[i + 2]);
                    double yc2 = Double.parseDouble(data[i + 3]);
                    double px = Double.parseDouble(data[i + 4]);
                    double py = Double.parseDouble(data[i + 5]);
                    cntrlpt.newValue(x + x - xc2, y + y - yc2);
                    pathpoints.addAll(interpolateCurve(relpt, new PointD(xc1, yc1), new PointD(xc2, yc2), new PointD(px, py)));
                    relpt.newValue(px, py);
                    i += 5;
                    break;
                }
                case 10: {
                    double x = relpt.x;
                    double y = relpt.y;
                    double xc2 = Double.parseDouble(data[i]);
                    double yc2 = Double.parseDouble(data[i + 1]);
                    double px = Double.parseDouble(data[i + 2]);
                    double py = Double.parseDouble(data[i + 3]);
                    pathpoints.addAll(interpolateCurve(relpt, cntrlpt, new PointD(xc2, yc2), new PointD(px, py)));
                    relpt.newValue(px, py);
                    i += 3;
                    break;
                }
                case 12: {
                    double rx = Double.parseDouble(data[i]);
                    double ry = Double.parseDouble(data[i + 1]);
                    double xrot = Double.parseDouble(data[i + 2]);
                    boolean bigarc = Integer.parseInt(data[i + 3]) > 0;
                    boolean sweep = Integer.parseInt(data[i + 4]) > 0;
                    double px = Double.parseDouble(data[i + 5]);
                    double py = Double.parseDouble(data[i + 6]);
                    pathpoints.addAll(interpolateArc(relpt, rx, ry, xrot, bigarc, sweep, new PointD(px, py)));
                    relpt.newValue(px, py);
                    i += 6;
                    break;
                }
                case 14: {
                    double tmpx = startpt.x;
                    double tmpy = startpt.y;
                    pathpoints.add(new Point((int) tmpx, (int) tmpy));
                    relpt.newValue(tmpx, tmpy);
                    break;
                }
            }
        }

        if (pathpoints != null && !pathpoints.isEmpty()) {
            //System.out.println(pathpoints.size());
            Path p = new Path(pathpoints);
            if (translate != null) {
                p.alignMargin(-translate.x, -translate.y);
            }
            outPaths.add(p);
        }
        //return outPaths;
        if (isFill) {
            return fillPolygon(createEdges(outPaths), outPaths, scanLine);
        } else {
            return outPaths;
        }
    }

    public static double PRECISION = 1;
    public static double SEGMENTS = 200;

    /*
     * Interpolate the cubic Bezier curves (commands C,c,S,s)
     * Copyright 2014 Eric Heisler
     */
    private static ArrayList<Point> interpolateCurve(PointD p1, PointD pc1, PointD pc2, PointD p2) {

        ArrayList<Point> pts = new ArrayList<>();

        pts.add(0, new Point((int) p1.x, (int) p1.y));
        pts.add(1, new Point((int) p2.x, (int) p2.y));
        double maxdist = Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
        double interval = 1.0;
        double win, iin;
        int segments = 1;
        double tmpx, tmpy;
        int curX = -1, curY = -1;
        int newX, newY;

        while (maxdist > PRECISION && segments < SEGMENTS) {
            interval = interval / 2.0;
            segments = segments * 2;

            for (int i = 1; i < segments; i += 2) {
                win = 1 - interval * i;
                iin = interval * i;
                tmpx = win * win * win * p1.x + 3 * win * win * iin * pc1.x + 3 * win * iin * iin * pc2.x + iin * iin * iin * p2.x;
                tmpy = win * win * win * p1.y + 3 * win * win * iin * pc1.y + 3 * win * iin * iin * pc2.y + iin * iin * iin * p2.y;
                newX = (int) tmpx;
                newY = (int) tmpy;
                if (newX != curX || newY != curY) {
                    pts.add(i, new Point(curX = newX, curY = newY));
                }
            }
            if (segments > 3) {
                maxdist = 0.0;
                for (int i = 0; i < pts.size() - 2; i++) {
                    // this is the deviation from a straight line between 3 points
                    tmpx = (pts.get(i).x - pts.get(i + 1).x) * (pts.get(i).x - pts.get(i + 1).x) + (pts.get(i).y - pts.get(i + 1).y) * (pts.get(i).y - pts.get(i + 1).y) - ((pts.get(i).x - pts.get(i + 2).x) * (pts.get(i).x - pts.get(i + 2).x) + (pts.get(i).y - pts.get(i + 2).y) * (pts.get(i).y - pts.get(i + 2).y)) / 4.0;
                    if (tmpx > maxdist) {
                        maxdist = tmpx;
                    }
                }
                maxdist = Math.sqrt(maxdist);
            }
        }

        return pts;
    }

    /*
     * Interpolate the elliptical arcs (commands A,a)
     * Copyright 2014 Eric Heisler
     */
    final static ArrayList<Point> interpolateArc(PointD p1, double rx, double ry, double xrot, boolean bigarc, boolean sweep, PointD p2) {

        ArrayList<Point> pts = new ArrayList<>();

        pts.add(0, new Point((int) p1.x, (int) p1.y));
        pts.add(1, new Point((int) p2.x, (int) p2.y));
        // if the ellipse is too small to draw
        if (Math.abs(rx) <= PRECISION || Math.abs(ry) <= PRECISION) {
            return pts;
        }

        // Now we begin the task of converting the stupid SVG arc format 
        // into something actually useful (method derived from SVG specification)
        // convert xrot to radians
        xrot = xrot * Math.PI / 180.0;

        // radius check
        double x1 = Math.cos(xrot) * (p1.x - p2.x) / 2.0 + Math.sin(xrot) * (p1.y - p2.y) / 2.0;
        double y1 = -Math.sin(xrot) * (p1.x - p2.x) / 2.0 + Math.cos(xrot) * (p1.y - p2.y) / 2.0;

        rx = Math.abs(rx);
        ry = Math.abs(ry);
        double rchk = x1 * x1 / rx / rx + y1 * y1 / ry / ry;
        if (rchk > 1.0) {
            rx = Math.sqrt(rchk) * rx;
            ry = Math.sqrt(rchk) * ry;
        }

        // find the center
        double sq = (rx * rx * ry * ry - rx * rx * y1 * y1 - ry * ry * x1 * x1) / (rx * rx * y1 * y1 + ry * ry * x1 * x1);
        if (sq < 0) {
            sq = 0;
        }
        sq = Math.sqrt(sq);
        double cx1, cy1;
        if (bigarc == sweep) {
            cx1 = -sq * rx * y1 / ry;
            cy1 = sq * ry * x1 / rx;
        } else {
            cx1 = sq * rx * y1 / ry;
            cy1 = -sq * ry * x1 / rx;
        }
        double cx = (p1.x + p2.x) / 2.0 + Math.cos(xrot) * cx1 - Math.sin(xrot) * cy1;
        double cy = (p1.y + p2.y) / 2.0 + Math.sin(xrot) * cx1 + Math.cos(xrot) * cy1;

        // find angle start and angle extent
        double theta, dtheta;
        double ux = (x1 - cx1) / rx;
        double uy = (y1 - cy1) / ry;
        double vx = (-x1 - cx1) / rx;
        double vy = (-y1 - cy1) / ry;
        double thing = Math.sqrt(ux * ux + uy * uy);
        double thing2 = thing * Math.sqrt(vx * vx + vy * vy);
        if (thing == 0) {
            thing = 1e-7;
        }
        if (thing2 == 0) {
            thing2 = 1e-7;
        }
        if (uy < 0) {
            theta = -Math.acos(ux / thing);
        } else {
            theta = Math.acos(ux / thing);
        }

        if (ux * vy - uy * vx < 0) {
            dtheta = -Math.acos((ux * vx + uy * vy) / thing2);
        } else {
            dtheta = Math.acos((ux * vx + uy * vy) / thing2);
        }
        dtheta = dtheta % (2 * Math.PI);
        if (sweep && dtheta < 0) {
            dtheta += 2 * Math.PI;
        }
        if (!sweep && dtheta > 0) {
            dtheta -= 2 * Math.PI;
        }

        // Now we have converted from stupid SVG arcs to something useful.
        double maxdist = 100;
        double interval = dtheta;
        int segments = 1;
        double tmpx, tmpy;

        while (maxdist > PRECISION && segments < SEGMENTS) {
            interval = interval / 2.0;
            segments = segments * 2;

            for (int i = 1; i < segments; i += 2) {
                tmpx = cx + rx * Math.cos(theta + interval * i) * Math.cos(xrot) - ry * Math.sin(theta + interval * i) * Math.sin(xrot);
                tmpy = cy + rx * Math.cos(theta + interval * i) * Math.sin(xrot) + ry * Math.sin(theta + interval * i) * Math.cos(xrot);
                pts.add(i, new Point((int) tmpx, (int) tmpy));
            }

            if (segments > 3) {
                maxdist = 0.0;
                for (int i = 0; i < pts.size() - 2; i++) {
                    // this is the deviation from a straight line between 3 points
                    tmpx = (pts.get(i).x - pts.get(i + 1).x) * (pts.get(i).x - pts.get(i + 1).x) + (pts.get(i).y - pts.get(i + 1).y) * (pts.get(i).y - pts.get(i + 1).y) - ((pts.get(i).x - pts.get(i + 2).x) * (pts.get(i).x - pts.get(i + 2).x) + (pts.get(i).y - pts.get(i + 2).y) * (pts.get(i).y - pts.get(i + 2).y)) / 4.0;
                    if (tmpx > maxdist) {
                        maxdist = tmpx;
                    }
                }
                maxdist = Math.sqrt(maxdist);
            }
        }

        return pts;
    }

    private static Edge[] createEdges(ArrayList<Path> paths) {
        ArrayList<Edge> sortedEdges = new ArrayList<>();
        paths.stream().map((path) -> path.points).forEach((points) -> {
            for (int i = 0; i < points.size() - 1; i++) {
                if (points.get(i).y < points.get(i + 1).y) {
                    sortedEdges.add(new Edge(points.get(i), points.get(i + 1)));
                } else {
                    sortedEdges.add(new Edge(points.get(i + 1), points.get(i)));
                }
            }
        });
        return sortedEdges.toArray(new Edge[sortedEdges.size()]);
    }

    private static ArrayList<Path> fillPolygon(Edge[] sortedEdges, ArrayList<Path> paths, int scanLineStep) {
        // sort all edges by y coordinate, smallest one first, lousy bubblesort
        Edge tmp;

        for (int i = 0; i < sortedEdges.length - 1; i++) {
            for (int j = 0; j < sortedEdges.length - 1; j++) {
                if (sortedEdges[j].p1.y > sortedEdges[j + 1].p1.y) {
                    // swap both edges
                    tmp = sortedEdges[j];
                    sortedEdges[j] = sortedEdges[j + 1];
                    sortedEdges[j + 1] = tmp;
                }
            }
        }

        // find biggest y-coord of all vertices
        int scanlineEnd = 0;
        for (Edge sortedEdge : sortedEdges) {
            if (scanlineEnd < sortedEdge.p2.y) {
                scanlineEnd = sortedEdge.p2.y;
            }
        }

        // scanline starts at smallest y coordinate
        int scanline;

        // this list holds all cutpoints from current scanline with the polygon
        ArrayList<Integer> list = new ArrayList<>();

        // move scanline step by step down to biggest one
        for (scanline = sortedEdges[0].p1.y; scanline <= scanlineEnd; scanline++) {
            //System.out.println("ScanLine: " + scanline); // DEBUG

            if (scanLineStep > 1 && scanline % scanLineStep != 0) {
                for (Edge sortedEdge : sortedEdges) {
                    if (scanline == sortedEdge.p1.y) {
                        if (scanline == sortedEdge.p2.y) {
                            sortedEdge.deactivate();
                        } else {
                            sortedEdge.activate();
                        }
                    }
                    if (scanline == sortedEdge.p2.y) {
                        sortedEdge.deactivate();
                    }
                    if (scanline > sortedEdge.p1.y && scanline < sortedEdge.p2.y) {
                        sortedEdge.update();
                    }
                }
                continue;
            }

            list.clear();
            // loop all edges to see which are cut by the scanline
            for (Edge sortedEdge : sortedEdges) {
                // here the scanline intersects the smaller vertice
                if (scanline == sortedEdge.p1.y) {
                    if (scanline == sortedEdge.p2.y) {
                        // the current edge is horizontal, so we add both vertices
                        sortedEdge.deactivate();
                        list.add((int) sortedEdge.curX);
                    } else {
                        sortedEdge.activate();
                        // we don't insert it in the list cause this vertice is also
                        // the (bigger) vertice of another edge and already handled
                    }
                }
                // here the scanline intersects the bigger vertice
                if (scanline == sortedEdge.p2.y) {
                    sortedEdge.deactivate();
                    list.add((int) sortedEdge.curX);
                }
                // here the scanline intersects the edge, so calc intersection point
                if (scanline > sortedEdge.p1.y && scanline < sortedEdge.p2.y) {
                    sortedEdge.update();
                    list.add((int) sortedEdge.curX);
                }
            }
            /*if (scanLineStep > 1 && scanline % scanLineStep != 0) {
             continue;
             }*/
            // now we have to sort our list with our x-coordinates, ascendend
            int swaptmp;
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < list.size() - 1; j++) {
                    Integer list_j = list.get(j);
                    Integer list_jp1 = list.get(j + 1);
                    if (list_j > list_jp1) {
                        swaptmp = list_j;
                        list.set(j, list_jp1);
                        list.set(j + 1, swaptmp);
                    }

                }
            }
            if (list.size() < 2 || list.size() % 2 != 0) {
                continue;
            }
            // so draw all line segments on current scanline
            for (int i = 0; i < list.size(); i += 2) {
                Path path = new Path();
                path.points.add(new Point(list.get(i), scanline));
                path.points.add(new Point(list.get(i + 1), scanline));
                paths.add(path);
            }
        }
        return paths;
    }

    public void calcMinMaxPoint() {
        int min_x = Integer.MAX_VALUE;
        int min_y = Integer.MAX_VALUE;
        for (Point p : points) {
            if (p.x < min_x) {
                min_x = p.x;
            }
            if (p.y < min_y) {
                min_y = p.y;
            }
        }
        minPoint = new Point(min_x, min_y);

        int max_x = Integer.MIN_VALUE;
        int max_y = Integer.MIN_VALUE;
        for (Point p : points) {
            if (p.x > max_x) {
                max_x = p.x;
            }
            if (p.y > max_y) {
                max_y = p.y;
            }
        }
        maxPoint = new Point(max_x, max_y);
    }

    public final void checkReverse() {
        if (needReverse) {
            Collections.reverse(points);
        }
    }

    public final int getDistance(Point curPoint) {
        int firstPointDistance = getFirstPoint().distance(curPoint);
        int lastPointDistance = getLastPoint().distance(curPoint);
        if (lastPointDistance < firstPointDistance) {
            needReverse = true;
            return lastPointDistance;
        } else {
            needReverse = false;
            return firstPointDistance;
        }
    }

    public final Point getLastPoint() {
        return points.get(points.size() - 1);
    }

    public final Point getFirstPoint() {
        return points.get(0);
    }

    public final void alignMargin(int min_x, int min_y) {
        points.stream().forEach((p) -> {
            p.x -= min_x;
            p.y -= min_y;
        });
    }

    @Override
    public final String toString() {
        return points.toString();
    }

    public static int parseInt(String s)
            throws NumberFormatException {
        int result = 0;
        boolean negative = false;
        boolean afterDot = false;
        int i = 0;
        int len = s.length();
        if (s.charAt(0) == '-') {
            negative = true;
            i++;
        }
        while (i < len) {
            char c = s.charAt(i++);
            if (afterDot) {
                if (c - '0' >= 5) {
                    result++;
                }
                break;
            } else if (c == '.') {
                afterDot = true;
            } else {
                result *= 10;
                result += c - '0';
            }
        }
        return negative ? -result : result;
    }

}
