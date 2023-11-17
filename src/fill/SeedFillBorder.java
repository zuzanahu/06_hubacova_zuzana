package fill;


import model.Point2D;
import rasterize.Raster;

public class SeedFillBorder {

    public void dfs(Raster raster, int c, int r, int newColor, int borderColor) {
        // check if the point is inside polygon TODO


        raster.getColor(c,r).ifPresent( color -> {
            if ((color == borderColor) || (c >= raster.getWidth()) || (r >= raster.getHeight()) || (c <= 0) || (r <= 0 || color == newColor) ) {
                return;
            } else {
                // current pixel / center
                raster.setColor(c, r, newColor);
                // down
                dfs(raster, c, r + 1, newColor, borderColor);
                // up
                dfs(raster, c, r - 1, newColor, borderColor);
                // right
                dfs(raster, c + 1, r, newColor, borderColor);
                // left
                dfs(raster, c - 1, r, newColor, borderColor);
            }



        } );
    }


    public void seedFill(Raster raster, Point2D seed, int newColor, int borderColor) {
        //the seed has on default the color of the raster background
        int c = seed.getX();
        int r = seed.getY();
        int oldColor = raster.getColor(c, r).orElseThrow(() -> new IllegalStateException("seed does not have any color in the raster"));
        if(oldColor == newColor) {
            return;
        }
        if (newColor == borderColor) {
            return;
        }
        dfs(raster, c, r, newColor, borderColor);
    }
}
