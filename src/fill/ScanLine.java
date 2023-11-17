package fill;

import model.Line;
import model.Polygon;
import rasterize.LineRasterizerGraphics;
import rasterize.PolygonRasterizer;
import rasterize.Raster;

import java.util.ArrayList;
import java.util.Comparator;
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
        for (int i = 0; i < lines.size(); i++) {
            int y1 = lines.get(i).getY1();
            int y2 = lines.get(i).getY2();
            if (y1 == y2) {
                lines.remove(i);
            }
        }

        // 3. Orient the lines


        // 4. Calculate yMin and yMax
        int yMin = Integer.MAX_VALUE;
        int yMax = Integer.MIN_VALUE;


        for (Line line : lines) {
            yMin = Math.min(yMin, Math.min(line.getY1(), line.getY2()));
            yMax = Math.max(yMax, Math.max(line.getY1(), line.getY2()));
        }

        // 5. for y in [yMin, yMax]: scan line is vertical (goes from yMin to yMax from top to bottom)
        for (int y = yMin; y <= yMax; y++) {
            List<Double> intercepts = new ArrayList<>();

            // create a list of intercepts
            for (Line line : lines) {
                double intercept = computeIntercept(line, y);
                if (intercept != Double.MAX_VALUE) { // TODO dat tam boolean mitso max_value
                    intercepts.add(intercept);
                }
            }
            // sort the list of intercepts in ascending order (so that the scan line can go from top to bottom)
            intercepts.sort(Comparator.naturalOrder());

            // fill the area between odd and even intercepts using the `fillColor`
            for (int i = 0; i < intercepts.size(); i += 2) {
                int xStart = (int) Math.round(intercepts.get(i));
                // Check if i + 1 is within bounds
                    int xEnd = (int) Math.round(intercepts.get(i + 1));
                    liner.drawLine(xStart, y, xEnd, y, fillColor);
            }
        }
        //      - for line in lines:
        //          - compute the intercept if it exists and add it to the list of intercepts
        // 6. Rasterize the polygon using the `polygonColor` TODO
        polygoner.drawPolygon(polygon.getPoints(), polygonColor);

    }

    /**
     *
     * @param line Line object
     * @param y coordinate of the horizontal scanline
     * @return
     */
    private double computeIntercept(Line line, int y) {
        int x1 = line.getX1();
        int y1 = line.getY1();
        int x2 = line.getX2();
        int y2 = line.getY2();

        // Handle special case: vertical line
        if (x1 == x2) {
            // check if the lines
            return (y == y1 && y == y2) ? x1 : Double.MAX_VALUE;
        }

        // Handle special case: horizontal line
        if (y1 == y2) {
            return Double.MAX_VALUE; // The line is parallel to the scanline.
        }

        // Compute x-coordinate using the line equation
        double x = x1 + (double) (y - y1) * (x2 - x1) / (y2 - y1);

        // Ensure that the intersection point is within the bounds of the line segment
        if (x < Math.min(x1, x2) || x > Math.max(x1, x2)) {
            return Double.MAX_VALUE;
        }

        return x;
    }
}
