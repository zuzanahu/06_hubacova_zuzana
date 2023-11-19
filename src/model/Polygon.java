package model;

import rasterize.Raster;

import java.util.ArrayList;
import java.util.List;

public class Polygon {
    public ArrayList<Point2D> getPoints() {
        return points;
    }

    ArrayList<Point2D> points;

    public Polygon() {
        this.points = new ArrayList<>();
    }
    public Polygon(ArrayList<Point2D> points) {
        this.points = points;

        System.out.println("New polygon created:");
        for (Point2D point : points) {
            System.out.println(point);
        }
    }

    public List<Line> getLines() {
        List<Line> lines= new ArrayList<>();
        for (int i = 0; i < points.size()-1; i++) {
            lines.add(new Line(points.get(i),points.get(i+1)));
        }
        lines.add(new Line(points.get(points.size()-1),points.get(0)));
        return lines;

    }


    public boolean isPointInside( int c, int r) {
        int n = points.size();
        int intersections = 0;

        // Iterate through each edge of the polygon
        for (int i = 0; i < n; i++) {
            Point2D p1 = points.get(i);
            Point2D p2 = points.get((i + 1) % n);

            // Check if the ray from (c, r) intersects with the edge (p1, p2)
            if ((p1.getY() > r) != (p2.getY() > r) &&
                    c < (p2.getX() - p1.getX()) * (r - p1.getY()) / (p2.getY() - p1.getY()) + p1.getX()) {
                intersections++;
            }
        }

        // If the number of intersections is odd, the point is inside the polygon
        return intersections % 2 == 1;
    }


}
