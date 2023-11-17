package model;

import java.util.ArrayList;
import java.util.List;

public class Polygon {
    public Polygon(List<Point2D> points) {
        this.points = points;
    }

    public List<Point2D> getPoints() {
        return points;
    }

    public void setPoints(List<Point2D> points) {
        this.points = points;
    }

    List<Point2D> points;

    public List<Line> getLines() {
        List<Line> lines= new ArrayList<>();
        for (int i = 0; i < points.size()-1; i++) {
            lines.add(new Line(points.get(i),points.get(i+1)));
        }
        lines.add(new Line(points.get(points.size()-1),points.get(0)));
        return lines;
    }

}
