package fill;

import model.Point2D;
import model.Polygon;
import rasterize.Raster;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Predicate;

public class SeedFill {

    public SeedFill() {

    }


    public void dfs(Raster raster, int c, int r, int oldColor, int newColor) {
        // check if the point is inside polygon TODO

        raster.getColor(c,r).ifPresent( color -> {
            if ((color != oldColor) || (c >= raster.getWidth()) || (r >= raster.getHeight()) || (c <= 0) || (r <= 0) ) {
                return;
            } else {
                // current pixel / center
                raster.setColor(c, r, newColor);
                // down
                dfs(raster, c, r + 1, oldColor, newColor);
                // up
                dfs(raster, c, r - 1, oldColor, newColor);
                // right
                dfs(raster, c + 1, r, oldColor, newColor);
                // left
                dfs(raster, c - 1, r, oldColor, newColor);
            }
        } );
    }

    public void seedFill(Raster raster, Point2D seed, int newColor, Polygon polygon) {
        int c = seed.getX();
        int r = seed.getY();
        if (!polygon.isPointInside(raster, c, r)) {
            return;
        }
        int oldColor = raster.getColor(c, r).orElseThrow(() -> new IllegalStateException("seed does not have any color in the raster"));
        if(oldColor == newColor) {
            return;
        }
        dfs(raster, c, r, oldColor, newColor);
    }

    /**
     * BFS
     */
    public void seedFillBFS(Raster raster, Point2D seed, int newColor, Polygon polygon) {
        int c = seed.getX();
        int r = seed.getY();
        if (!polygon.isPointInside(raster, c, r)) {
            return;
        }
        int oldColor = raster.getColor(c, r).orElseThrow(() -> new IllegalStateException("seed does not have any color in the raster"));
        if (oldColor == newColor) {
            return;
        }
        Queue<Point2D> queue = new LinkedList<Point2D>();
        queue.add(seed);
        while (!queue.isEmpty()) {
            Point2D currentPoint = queue.poll();
            int x = currentPoint.getX();
            int y = currentPoint.getY();
            raster.getColor(x, y).ifPresent(color -> {
                if ((color == oldColor) && (c <= raster.getWidth()) && (r <= raster.getHeight()) && (c >= 0) && (r >= 0)) {
                    raster.setColor(x, y, newColor);
                    queue.add(new Point2D(x + 1, y));
                    queue.add(new Point2D(x - 1, y));
                    queue.add(new Point2D(x, y + 1));
                    queue.add(new Point2D(x, y - 1));
                }

            });
        }
    }

}
