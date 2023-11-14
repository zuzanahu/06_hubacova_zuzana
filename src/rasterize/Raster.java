package rasterize;

import java.awt.*;
import java.util.Optional;

/**
 * Represents a two-dimensional raster image with pixels of type int
 */
public interface Raster {
    /**
     * Returns the width of this raster image
     * @return int
     */
    int getWidth();
    /**
     * Returns the height of this raster image
     * @return int
     */
    int getHeight();

    /**
     * Sets the color of a pixel based on its address
     * @param color new color of the pixel
     * @param c column address of the pixel
     * @param r row address of the pixel
     * @return boolean whether it was a success or not
     */
    boolean setColor(int c, int r,int color);

    //Integer oproti int může být null
    /**
     * Returns the color of a pixel based on its address
     * @param c column address of the pixel
     * @param r row address of the pixel
     * @return  Optional of Integer representation of the color if the given address is valid
     * empty "Optional" otherwise
     */
    Optional<Integer> getColor(int c, int r);

    /**
     * Sets all the pixel sto hold the provided color
     * @param backgroundColor color
     */
    void clear(int backgroundColor);

    /**
     * To draw an image in a raster (the )
     * @param g type Graphics
     */
    public void present(Graphics g);

}
