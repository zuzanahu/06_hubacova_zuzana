package control;

import fill.SeedFill;
import fill.SeedFillBorder;
import model.Line;
import model.Point2D;
import model.Polygon;
import rasterize.*;
import view.Panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Controller2D implements Controller {

    private final Panel panel;

    private int x,y;
    private LineRasterizerGraphics lineRasterizer;

    private PolygonRasterizer polygonRasterizer;
    private ArrayList<Point2D> polygonPoints;

    private Raster raster;


    public Controller2D(Panel panel) {
        this.panel = panel;
        initObjects(panel.getRaster());
        initListeners(panel);
    }

    public void initObjects(Raster raster) {
        this.lineRasterizer = new LineRasterizerGraphics(raster);
        this.polygonRasterizer = new PolygonRasterizer(lineRasterizer, raster);
        this.polygonPoints = new ArrayList<>();
        this.raster = raster;
     }

    @Override
    public void initListeners(Panel panel) {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    polygonPoints.add(new Point2D(e.getX(), e.getY()));
                    polygonRasterizer.drawPolygon( polygonPoints, 0x00ffffff);
                    panel.repaint();
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    polygonRasterizer.drawPolygon( polygonPoints, 0x00ffffff);
                    SeedFill filler = new SeedFill();
                    filler.seedFill(raster, new Point2D(e.getX(), e.getY()), 0x0000ff00, new Polygon(polygonPoints));
                    panel.repaint();
                }
            }
        });
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // when you want to draw the first line of the polygon by dragging the mouse
                if (polygonPoints.isEmpty()) {
                    polygonPoints.add(new Point2D(e.getX(), e.getY()));
                    Point2D firstPoint = polygonPoints.get(0);
                    Point2D lastPoint = new Point2D(e.getX(), e.getY());
                    lineRasterizer.rasterize(new Line(firstPoint, lastPoint, 0x00ff0000));
                    panel.repaint();
                }
                // when you want to draw other lines than the first line of the polygon by dragging the mouse
                else {
                    panel.clear();
                    polygonRasterizer.drawPolygon(polygonPoints, 0x00ffffff);
                    // We have to connect the first and last point of the polygon with the point of the dragging event (current point) to make the polygon appear enclosed
                    Point2D firstPoint = polygonPoints.get(0);
                    Point2D currentPoint = new Point2D(e.getX(), e.getY());
                    lineRasterizer.rasterize(new Line(currentPoint, firstPoint, 0x00ff0000));
                    Point2D lastPoint = polygonPoints.get(polygonPoints.size() - 1);
                    lineRasterizer.rasterize(new Line(lastPoint, currentPoint, 0x00ff0000));
                    panel.repaint();
                }
            }
        });
        /*panel.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isControlDown()) return;

                if (e.isShiftDown()) {
                    //TODO
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    lineRasterizer.rasterize(new Line(x,y,e.getX(),e.getY(), 0xffffffff));
                    x = e.getX();
                    y = e.getY();
                } else if (SwingUtilities.isMiddleMouseButton(e)) {
                    //TODO
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    //TODO
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.isControlDown()) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        //TODO
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        //TODO
                    }
                }
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (e.isControlDown()) return;

                if (e.isShiftDown()) {
                    //TODO
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    //TODO
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    //TODO
                } else if (SwingUtilities.isMiddleMouseButton(e)) {
                    //TODO
                }
                update();
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // na klávesu C vymazat plátno
                if (e.getKeyCode() == KeyEvent.VK_C) {
                    //TODO
                }
            }
        });

        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                //panel.resize();
                initObjects(panel.getRaster());
            }
        });*/
    }

    private void update() {
//        panel.clear();
        //TODO

    }

    private void hardClear() {
        panel.clear();
    }

}
