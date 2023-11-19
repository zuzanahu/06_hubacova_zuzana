package model;

import fill.SeedFill;
import rasterize.PolygonRasterizer;
import rasterize.Raster;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Clipper {
    public Clipper() {
    }

    public ArrayList<Point2D> clip(Polygon oldPolygon, Polygon clipperPolygon) {
        List<Line> oldEdges = oldPolygon.getLines();
        List<Line>  clipperEdges = clipperPolygon.getLines();
        // saving the points of the newPolygon
        ArrayList<Point2D> newPoints = new ArrayList<>();

        for (Line clipperEdge: clipperEdges) {
            for (Line oldEdge: oldEdges) {
                //are start point and end point of oldPolygon line inside or outside o clipperEdge
                //on the left of the clipperEdge the points are outside
                //we get to know that by the dot product of a normal vector (of a tangent vector of the clipperEdge line) and a vector from clipperEdge x1 and X2 to both the start point end endpoint of oldEdge
                int clipperX1 = clipperEdge.getX1();
                int clipperX2 = clipperEdge.getX2();
                int clipperY1 = clipperEdge.getY1();
                int clipperY2 = clipperEdge.getY2();

                int[] tangentClipper = {clipperX2 - clipperX1, clipperY2 - clipperY1};
                int[] normalClipper = {tangentClipper[1], -tangentClipper[0]};

                int oldX1 = oldEdge.getX1();
                int oldX2 = oldEdge.getX2();
                int oldY1 = oldEdge.getY1();
                int oldY2 = oldEdge.getY2();
                Point2D oldEnd = new Point2D(oldX2, oldY2);

                int[] toStart = {oldX1 - clipperX1, oldY1 - clipperY1};
                int[] toEnd = {oldX2 - clipperX2, oldY2 - clipperY2};

                int dotProductStart = toStart[0] * normalClipper[0] + toStart[1] * normalClipper[1];
                int dotProductEnd = toEnd[0] * normalClipper[0] + toEnd[1] * normalClipper[1];

                // both are inside => save the old end point only
                if (dotProductStart > 0 && dotProductEnd > 0) {
                    newPoints.add(oldEnd);
                }
                //when both are outside (alpha < 0) then we do nothing
                //save only the end and the intercept
                if (dotProductEnd > 0 && dotProductStart < 0) {
                    newPoints.add(oldEnd);
                    Point2D intercept;
                    double x = (double) ((clipperX1 * clipperY2 - clipperX2 * clipperY1) * (oldX1 - oldX2) - (oldX1 * oldY2 - oldX2 * oldY1) * (clipperX1 - clipperX2)) / ((clipperX1 - clipperX2) * (oldY1 - oldY2) - (clipperY1 - clipperY2) * (oldX1 - oldX2));
                    double y = (double) ((clipperX1 * clipperY2 - clipperX2 * clipperY1) * (oldY1 - oldY2) - (oldX1 * oldY2 - oldX2 * oldY1) * (clipperY1 - clipperY2)) / ((clipperX1 - clipperX2) * (oldY1 - oldY2) - (clipperY1 - clipperY2) * (oldX1 - oldX2));
                    intercept = new Point2D((int) x, (int) y);
                    newPoints.add(intercept);
                }
                //when start is inside and end outside, then add the intercept
                if (dotProductEnd < 0 && dotProductStart > 0) {
                    Point2D intercept;
                    double x = (double) ((clipperX1 * clipperY2 - clipperX2 * clipperY1) * (oldX1 - oldX2) - (oldX1 * oldY2 - oldX2 * oldY1) * (clipperX1 - clipperX2)) / ((clipperX1 - clipperX2) * (oldY1 - oldY2) - (clipperY1 - clipperY2) * (oldX1 - oldX2));
                    double y = (double) ((clipperX1 * clipperY2 - clipperX2 * clipperY1) * (oldY1 - oldY2) - (oldX1 * oldY2 - oldX2 * oldY1) * (clipperY1 - clipperY2)) / ((clipperX1 - clipperX2) * (oldY1 - oldY2) - (clipperY1 - clipperY2) * (oldX1 - oldX2));
                    intercept = new Point2D((int) x, (int) y);
                    newPoints.add(intercept);
                }


            }
        }
        return newPoints;
    }
}
