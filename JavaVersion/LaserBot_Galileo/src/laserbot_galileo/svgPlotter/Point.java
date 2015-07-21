/*
 *  Copyright Nguyen Duong Kim Hao @ 2014
 */
package laserbot_galileo.svgPlotter;

/**
 *
 * @author KimHao
 */
public final class Point {

    public int x, y;

    public Point() {
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public final int distance(Point p) {
        return (int) Math.sqrt((x - p.x) * (x - p.x) + (y - p.y) * (y - p.y));
    }

    @Override
    public final String toString() {
        return String.format("[%d, %d]", x, y);
    }

    public boolean equals(Point p) {
        return (x == p.x && y == p.y);
    }

    public final Point copy() {
        return new Point(x, y);
    }

}
