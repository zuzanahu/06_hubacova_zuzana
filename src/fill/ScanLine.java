package fill;

import model.Line;
import model.Polygon;
import rasterize.LineRasterizerGraphics;
import rasterize.PolygonRasterizer;
import rasterize.Raster;

import java.util.List;

public class ScanLine {

    void fill(Polygon polygon, Raster raster, int fillColor, int polygonColor, PolygonRasterizer polygoner, LineRasterizerGraphics liner) {

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


        // 4. Calculate yMin and yMax TODO
        int yMin = Integer.MAX_VALUE;
        int yMax = Integer.MIN_VALUE;


        for (Line line : lines) {
            yMin = Math.min(yMin, Math.min(line.getY1(), line.getY2()));
            yMax = Math.max(yMax, Math.max(line.getY1(), line.getY2()));
        }

        // 5. for y in [yMin, yMax]:
        //      - create a list of intercepts
        //      - for line in lines:
        //          - compute the intercept if it exists and add it to the list of intercepts
        //      - sort the list of intercepts in ascending order
        //      - fill the area between odd and even intercepts using the `fillColor`
        // 6. Rasterize the polygon using the `polygonColor`
    }
}
