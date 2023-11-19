package model;


import java.util.List;

public class Clipper {



    public Clipper() {
    }

    public Polygon clip(Polygon oldPolygon, Polygon clipperPolygon) {
        List<Line> clipperEdges = clipperPolygon.getLines();



        // for every edge of the clipping polygon find out if the oldPolygonPoints ar inside or outside
        for (Line clipperEdge : clipperEdges) {
            Polygon currentPolygon = new Polygon();
            Point2D v1 = oldPolygon.getPoints().get(oldPolygon.getPoints().size() - 1);

            for (int i = 0; i < oldPolygon.getPoints().size(); i++) {
                Point2D v2 = oldPolygon.getPoints().get(i);

                if (clipperEdge.isInside(v2)) {
                    if (!clipperEdge.isInside(v1)) {
                        currentPolygon.getPoints().add(getIntercept(v1, v2, clipperEdge));
                    }
                    currentPolygon.getPoints().add(v2);
                } else {
                    if (clipperEdge.isInside(v1)) {
                        currentPolygon.getPoints().add(getIntercept(v1, v2, clipperEdge));
                    }
                }
                v1 = v2;
            }
            oldPolygon = currentPolygon;
        }

        return oldPolygon;
    }

    private Point2D getIntercept(Point2D firstStart, Point2D firstEnd, Line second) {
        int firstStartX = firstStart.getX();
        int firstEndX = firstEnd.getX();
        int firstStartY = firstStart.getY();
        int firstEndY = firstEnd.getY();
        int secondStartX = second.getX1();
        int secondEndX = second.getX2();
        int secondStartY = second.getY1();
        int secondEndY = second.getY2();
        double x = (double) ((firstStartX * firstEndY - firstEndX * firstStartY) * (secondStartX - secondEndX) - (secondStartX * secondEndY - secondEndX * secondStartY) * (firstStartX - firstEndX)) / ((firstStartX - firstEndX) * (secondStartY - secondEndY) - (firstStartY - firstEndY) * (secondStartX - secondEndX));
        double y = (double) ((firstStartX * firstEndY - firstEndX * firstStartY) * (secondStartY - secondEndY) - (secondStartX * secondEndY - secondEndX * secondStartY) * (firstStartY - firstEndY)) / ((firstStartX - firstEndX) * (secondStartY - secondEndY) - (firstStartY - firstEndY) * (secondStartX - secondEndX));
        Point2D intercept = new Point2D((int) x, (int) y);
        return intercept;
    }

}
