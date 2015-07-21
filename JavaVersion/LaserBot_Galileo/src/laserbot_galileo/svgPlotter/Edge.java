/*
 *  Copyright Nguyen Duong Kim Hao @ 2014
 */
package laserbot_galileo.svgPlotter;

/**
 *
 * @author KimHao
 */
public final class Edge {

    public final Point p1;        // first vertice
    public final Point p2;        // second vertice
    private final double m;                // slope
    public double curX;             // x-coord of intersection with scanline

    public Edge(Point a, Point b) {
        p1 = a.copy();
        p2 = b.copy();
        // m = 1 / dy / dx
        m = (p1.x - p2.x) / (p1.y - p2.y);
        System.out.println(m);
    }

    /*
     * Called when scanline intersects the first vertice of this edge.
     * That simply means that the intersection point is this vertice.
     */
    public void activate() {
        curX = p1.x;
    }

    /*
     * Update the intersection point from the scanline and this edge.
     * Instead of explicitly calculate it we just increment with 1/m every time
     * it is intersected by the scanline.
     */
    public void update() {
        curX += m;
    }

    /*
     * Called when scanline intersects the second vertice, 
     * so the intersection point is exactly this vertice and from now on 
     * we are done with this edge
     */
    public void deactivate() {
        curX = p2.x;
    }

    @Override
    public final String toString() {
        return String.format("{%s -> %s}", p1, p2);
    }

}
