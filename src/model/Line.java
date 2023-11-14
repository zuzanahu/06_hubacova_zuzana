package model;

public class Line {

    private final int  x1, x2, y1, y2;
    private final int color;


    public Line(int x1, int y1, int x2, int y2, int color) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.color = color;
    }

    public Line(Point2D p1, Point2D p2, int color) {
        this.x1 = p1.getX();
        this.x2 = p2.getX();
        this.y1 = p1.getY();
        this.y2 = p2.getY();
        this.color = color;
    }

    public int getX1() {
        return x1;
    }

    public int getX2() {
        return x2;
    }

    public int getY1() {
        return y1;
    }

    public int getY2() {
        return y2;
    }

    public int getColor() {
        return color;
    }
}
