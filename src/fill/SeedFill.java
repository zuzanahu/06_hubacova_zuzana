package fill;

import model.Point2D;
import rasterize.Raster;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Predicate;

public class SeedFill implements Filler {

    public SeedFill() {

    }

    @Override
    public void fill() {

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

    public void seedFill(Raster raster, Point2D seed, int newColor) {
        int c = seed.getX();
        int r = seed.getY();
        int oldColor = raster.getColor(c, r).orElseThrow(() -> new IllegalStateException("seed does not have any color in the raster"));
        if(oldColor == newColor) {
            return;
        }
        dfs(raster, c, r, oldColor, newColor);
    }

    /**
     * BFS
     */
    /*public void seedFillBFS(Raster raster, Point2D seed, int newColor) {
        int c = seed.getX();
        int r = seed.getY();
        int oldColor = raster.getColor(c, r).orElseThrow(() -> new IllegalStateException("seed does not have any color in the raster"));
        if(oldColor == newColor) {
            return;
        }
        ArrayList<Point2D> queue = new ArrayList<Point2D>();
        Point2D point;
        queue.add(seed);
        for (int i = 0; i < queue.size(); i++) {
            point = queue.get(i);
            Point2D finalPoint = point;
            raster.getColor(c,r).ifPresent(color -> {
                if ((color != oldColor) || (c >= raster.getWidth()) || (r >= raster.getHeight()) || (c <= 0) || (r <= 0) ) {
                    continue;
                } else {
                    // current pixel / center
                    raster.setColor(c, r, newColor);
                    queue.add(new Point2D(finalPoint.getX() + 1, finalPoint.getY()));
                    queue.add(new Point2D(finalPoint.getX() - 1, finalPoint.getY()));
                    queue.add(new Point2D(finalPoint.getX(), finalPoint.getY() + 1 ));
                    queue.add(new Point2D(finalPoint.getX(), finalPoint.getY() - 1));
                }
            } );
        }
    }*/

}
