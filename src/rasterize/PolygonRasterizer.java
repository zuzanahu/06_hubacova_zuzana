package rasterize;

import model.Line;
import model.Point2D;

import java.util.List;

public class PolygonRasterizer {

    private final LineRasterizerGraphics rasterizer;
    private final Raster raster;

    public PolygonRasterizer( LineRasterizerGraphics rasterizer, Raster raster) {
        this.rasterizer = rasterizer;
        this.raster = raster;
    }

    /**
     * This function draws a polygon
     * Visually connects polygon's nodes using line rasterization and also connects the first and last node to ensure that the polygon border is enclosed
     * @param nodes nodes of the polygon
     * @param color border color
     */

    public void drawPolygon(List<Point2D> nodes , int color) {
        if (nodes.size() > 1) {
            for (int i = 0; i < nodes.size() - 1; i++) {
                Point2D a = nodes.get(i);
                Point2D b = nodes.get(i + 1);
                rasterizer.rasterize(new Line(a, b, color) );
            }
            // Connect the last and first points to close the polygon
            Point2D firstPoint = nodes.get(0);
            Point2D lastPoint = nodes.get(nodes.size() - 1);
            rasterizer.rasterize(new Line(lastPoint, firstPoint, color));
        }
    }
}