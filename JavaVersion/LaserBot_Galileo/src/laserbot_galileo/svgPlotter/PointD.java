/*
 *  Copyright Nguyen Duong Kim Hao @ 2014
 */
package laserbot_galileo.svgPlotter;

/**
 *
 * @author KimHao
 */
public final class PointD {

    public double x, y;

    public PointD() {
    }

    public PointD(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public final double distance(PointD p) {
        return Math.sqrt((x - p.x) * (x - p.x) + (y - p.y) * (y - p.y));
    }
    
    public final void newValue(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public final String toString() {
        return String.format("[%f, %f]", x, y);
    }

    public boolean equals(PointD p) {
        return (x == p.x && y == p.y);
    }

    public final PointD copy() {
        return new PointD(x, y);
    }

}
