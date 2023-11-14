package model;
/**
 * represents a point in a two-dimensional space, immutable
 */
public class Point2D {
    private int x;
    private int y;

    /**
     * constructor
     * @param x coordinate of a point
     * @param y coordinate of a point
     * when using points in rasters be careful to choose coordinates, that are relevant to the raster coordinate system (some rasters do not have negative coordinates etc.)
     */
    public Point2D (int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
