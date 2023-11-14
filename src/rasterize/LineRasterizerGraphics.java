package rasterize;


import model.Line;
import model.Point2D;
import rasterize.LineRasterizer;
import rasterize.Raster;

import java.awt.*;


public class LineRasterizerGraphics implements LineRasterizer {
    Raster raster;
    public LineRasterizerGraphics(Raster raster) {
        this.raster = raster;
    }

    /**
     * Draws a line segment, the algorithm in this method is called "Trivial line algorithm for all quadrants"
     *
     * @param x1     coordinate of the first point ranging from 0 to the width of the raster - 1
     * @param x2     coordinate of the second point ranging from 0 to the width of the raster - 1
     * @param y1     coordinate of the first point ranging from 0 to the height of the raster - 1
     * @param y2     coordinate of the second point ranging from 0 to the height of the raster - 1
     * @param color  color of the line
     */
    public void drawLine( int x1, int y1, int x2, int y2, int color) {

        //checks if k == infinity, or in other words if x1 and x2 are the same
        if (x1 == x2) {
            // Vertical line, handle separately
            int minY = Math.min(y1, y2);
            int maxY = Math.max(y1, y2);

            for (int r = minY; r <= maxY; r++) {
                raster.setColor(x1, r, color);
            }

        } else {

            final double k = (double) (y2 - y1) / (x2 - x1); // k can be equal to infinity, there is no check or solution to this bug implemented in the code
            final double q = y1 - k * x1;

            if (Math.abs(y2 - y1) < Math.abs(x2 - x1)) {
                //c == x axis, r == y axis
                if (x2 > x1) {
                    for (int c = x1; c < x2; c++) {
                        int r = (int) (k * c + q);
                        raster.setColor(c, r, color);
                    }
                } else {
                    for (int c = x1; x2 < c; c--) {
                        int r = (int) (k * c + q);
                        raster.setColor(c, r, color);
                    }
                }
            } else {
                //c == x axis, r == y axis

                if (y2 > y1) {
                    for (int r = y2; y1 < r; r--) {
                        int c = (int) ((r - q) / k);
                        raster.setColor(c, r, color);
                    }
                } else {
                    for (int r = y1; y2 < r; r--) {
                        int c = (int) ((r - q) / k);
                        raster.setColor(c, r, color);
                    }
                }
            }
        }
    }

    @Override
    public void rasterize(Line line) {
        drawLine(line.getX1(), line.getY1(), line.getX2(), line.getY2(), line.getColor());
    }
}







