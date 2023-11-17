package fill;

import model.Line;
import model.Polygon;
import rasterize.LineRasterizerGraphics;
import rasterize.PolygonRasterizer;

import java.util.ArrayList;
import java.util.List;

public class ScanLine {

    public ScanLine() {
    }

    /**
     * How it works: for every y from yMin to yMax (<yMin, yMax> = height of the polygon) check if there are some points in the polygon lines that have that y (the points that are returned are the intercepts and in beween them <odd, even> we are going to color the entire horizontal line) as the lines add up the whole polygon is going to be filled with color)
     * @param polygon
     * @param fillColor
     * @param polygoner can draw (rasterize) a polygon using polygonColor
     * @param liner lineRasterizer that can rasterize lines
     */
    public void fill(Polygon polygon, int fillColor, PolygonRasterizer polygoner, int polygonColor, LineRasterizerGraphics liner) {

        // 1. Create a list of lines
        List<Line> lines = polygon.getLines();

        // 2. Remove horizontal lines from the list
        List<Line> nonHorizontalLines = new ArrayList<>();
        for (Line line : lines) {
            int y1 = line.getY1();
            int y2 = line.getY2();
            if (y1 != y2) {
                nonHorizontalLines.add(line);
            }
        }
        lines = nonHorizontalLines;

        // 3. Orient the lines
        List<Line> orientedLines = new ArrayList<>();
        for (Line line: lines) {
            int x1 = line.getX1();
            int y1 = line.getY1();
            int x2 = line.getX2();
            int y2 = line.getY2();

            if(y1 > y2){
                y1 = line.getY2();
                y2 = line.getY1();

                x1 = line.getX2();
                x2 = line.getX1();
            }
            orientedLines.add(new Line(x1-1, y1-1, x2, y2, polygonColor)) ;

        }
        lines = orientedLines;

        // 4. Calculate yMin and yMax
        int yMin = Integer.MAX_VALUE;
        int yMax = Integer.MIN_VALUE;


        for (Line line : lines) {
            yMin = Math.min(yMin, Math.min(line.getY1(), line.getY2()));
            yMax = Math.max(yMax, Math.max(line.getY1(), line.getY2()));
        }

        // 5. for y in [yMin, yMax]: (goes from yMin to yMax from top to bottom)
        for (int y = yMin; y <= yMax; y++) {
            ArrayList<Double> intercepts = new ArrayList<>();

            // create a list of intercepts
            for (Line line : lines) {
                double intercept = computeIntercept(line, y);
                if (intercept != Double.MAX_VALUE) {
                    intercepts.add(intercept);
                }
            }
            // sort intercepts in an ascending way
            ArrayList<Double> sortedIntercepts = sortIntercepts(intercepts);

            // draw lines from even intercept to odd intercept
            for (int i = 0; i < sortedIntercepts.size(); i += 2) {
                int xStart = (int) Math.round(sortedIntercepts.get(i));
                int xEnd = (int) Math.round(sortedIntercepts.get(i + 1));

                // Ensure xStart and xEnd are different to avoid processing vertices twice
                if (xStart != xEnd) {
                    liner.drawLine(xStart, y, xEnd, y, fillColor);
                }
            }
        }
        // 6. Rasterize the polygon using the `polygonColor`
        polygoner.drawPolygon(polygon.getPoints(), polygonColor);
    }

    /**
     *
     * @param line Line object
     * @param y coordinate of the horizontal scanline
     * @return intersect
     */
    private double computeIntercept(Line line, int y) {
        int x1 = line.getX1();
        int y1 = line.getY1();
        int x2 = line.getX2();
        int y2 = line.getY2();


        // Compute x-coordinate using the line equation
        double x = x1 + (double) (y - y1) * (x2 - x1) / (y2 - y1);

        // Ensure that the intersection point is within the bounds of the line segment
        if (x < Math.min(x1, x2) || x > Math.max(x1, x2)) {
            return Double.MAX_VALUE;
        }

        return x;
    }

    private ArrayList<Double> sortIntercepts(ArrayList<Double> list) {
        {
            int n = list.size();

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n - i - 1; j++) {
                    if (list.get(j) > list.get(j + 1)) {
                        // swap the elements
                        Double temp = list.get(j);
                        list.set(j, list.get(j + 1));
                        list.set(j + 1, temp);
                    }
                }
            }
        }
        return list;
    }

}