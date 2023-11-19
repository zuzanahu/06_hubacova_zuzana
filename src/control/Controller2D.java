package control;

import fill.ScanLine;
import fill.SeedFill;
import fill.SeedFillBorder;
import model.Clipper;
import model.Line;
import model.Point2D;
import model.Polygon;
import rasterize.*;
import view.Panel;

import java.awt.event.*;
import java.util.ArrayList;

public class Controller2D implements Controller {

    private LineRasterizerGraphics lineRasterizer;

    private PolygonRasterizer polygonRasterizer;
    private ArrayList<Point2D> polygonPoints;
    private ArrayList<Point2D> clipperPoints;
    private ArrayList<Point2D> toClipPoints;
    private Raster raster;


    public Controller2D(Panel panel) {
        initObjects(panel.getRaster());
        initListeners(panel);
    }

    public void initObjects(Raster raster) {
        this.lineRasterizer = new LineRasterizerGraphics(raster);
        this.polygonRasterizer = new PolygonRasterizer(lineRasterizer, raster);
        this.polygonPoints = new ArrayList<>();
        this.raster = raster;
        this.clipperPoints = new ArrayList<>();
        this.toClipPoints = new ArrayList<>();
     }

    @Override
    public void initListeners(Panel panel) {
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                //on C clear canvas
                if (e.getKeyCode() == KeyEvent.VK_C) {
                    polygonPoints.clear();
                    toClipPoints.clear();
                    clipperPoints.clear();
                    panel.clear();
                }
                //on L do scanline
                if (e.getKeyCode() == KeyEvent.VK_L) {
                    polygonRasterizer.drawPolygon( polygonPoints, 0x00ffffff);
                    ScanLine scanner = new ScanLine();
                    scanner.fill(new Polygon(polygonPoints), 0x000000ff, polygonRasterizer, 0x00ffffff, lineRasterizer);
                    panel.repaint();
                }
                //on H clip
                if (e.getKeyCode() == KeyEvent.VK_H) {

                    if (clipperPoints.size() > 2 && toClipPoints.size() > 2) {
                        panel.clear();

                        Clipper clipper = new Clipper();
                        ScanLine filler = new ScanLine();
                        int yellowColor = 0x00ffff00;
                        int greenColor = 0x0000ff00;
                        int redColor = 0x00ff0000;
                        int greyColor = 0x00f0f0f0;

                        System.out.println("yellow");
                        Polygon yellowPolygon = new Polygon(clipperPoints);
                        System.out.println("green");
                        Polygon greenPolygon = new Polygon(toClipPoints);

                        polygonRasterizer.drawPolygon(yellowPolygon.getPoints(), yellowColor);
                        polygonRasterizer.drawPolygon(greenPolygon.getPoints(), greenColor);
                        System.out.println("red");

                        Polygon redPolygon = clipper.clip(greenPolygon, yellowPolygon);

                        panel.clear();


                        polygonRasterizer.drawPolygon(redPolygon.getPoints(), redColor);

                        filler.fill(redPolygon, greyColor, polygonRasterizer, redColor, lineRasterizer);

                        polygonRasterizer.drawPolygon( clipperPoints, 0x00ffff00);
                        panel.repaint();
                    }
                }
            }
        });
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    // to clip points

                    if (e.isControlDown()) {
                        panel.clear();
                        toClipPoints.add(new Point2D(e.getX(), e.getY()));

                        if (toClipPoints.size() == 1) {
                            raster.setColor(e.getX(), e.getY(), 0x0000ff00);
                            panel.repaint();
                        } else if (toClipPoints.size() == 2) {
                            lineRasterizer.rasterize(new Line(toClipPoints.get(0), new Point2D(e.getX(), e.getY()), 0x0000ff00));
                            panel.repaint();
                        } else {
                            polygonRasterizer.drawPolygon(toClipPoints, 0x0000ff00);
                            panel.repaint();
                        }
                    }


                    // clipper points
                    if (e.isShiftDown()) {
                        clipperPoints.add(new Point2D(e.getX(), e.getY()));
                        if (clipperPoints.size() == 1) {
                            polygonRasterizer.drawPolygon(toClipPoints, 0x0000ff00);
                            raster.setColor(e.getX(), e.getY(), 0x00ffff00);
                            panel.repaint();
                        } else if (clipperPoints.size() == 2) {
                            polygonRasterizer.drawPolygon(toClipPoints, 0x0000ff00);
                            lineRasterizer.rasterize(new Line(clipperPoints.get(0), new Point2D(e.getX(), e.getY()), 0x00ffff00));
                            panel.repaint();
                        } else {
                            raster.clear(0x000000);

                            polygonRasterizer.drawPolygon(toClipPoints, 0x0000ff00);
                            polygonRasterizer.drawPolygon(clipperPoints, 0x00ffff00);
                            panel.repaint();
                        }

                    }
                }
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                // draw polygon
                if (e.getButton() == MouseEvent.BUTTON1) {
                    polygonPoints.add(new Point2D(e.getX(), e.getY()));
                    polygonRasterizer.drawPolygon( polygonPoints, 0x00ffffff);
                    panel.repaint();
                }
                // seed-fill
                if (e.getButton() == MouseEvent.BUTTON2) {
                    //seed-fill background -  does not work
                    /*polygonRasterizer.drawPolygon( polygonPoints, 0x00ffffff);
                    SeedFillBorder filler = new SeedFillBorder();
                    filler.seedFill(raster, new Point2D(e.getX(), e.getY()), 0x0000ff00, 0x0000ff, new Polygon(polygonPoints));
                    panel.repaint();*/
                    // seed-fill background
                    /*polygonRasterizer.drawPolygon( polygonPoints, 0x00ffffff);
                    SeedFill filler = new SeedFill();
                    filler.seedFill(raster, new Point2D(e.getX(), e.getY()), 0x0000ff00, new Polygon(polygonPoints));
                    panel.repaint();*/
                    //seed-fill background queue
                    polygonRasterizer.drawPolygon( polygonPoints, 0x00ffffff);
                    SeedFill filler = new SeedFill();
                    filler.seedFillBFS(raster, new Point2D(e.getX(), e.getY()), 0x0000ff00, new Polygon(polygonPoints));
                    panel.repaint();
                }

            }
        });
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            // draw polygon more interactively
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
    }



}
