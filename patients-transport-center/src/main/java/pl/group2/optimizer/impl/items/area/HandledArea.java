package pl.group2.optimizer.impl.items.area;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.List;

import static java.awt.Color.GREEN;

public class HandledArea {
    private Polygon area;
    private List<Point> points;
    private Polygon scaledArea;

    public HandledArea() {
        area = new Polygon();
    }

    public void createArea(List<Point> points) {
        this.points = points;
        for (Point point : points) {
            area.addPoint(point.getXCoordinate(), point.getYCoordinate());
        }
    }

    public boolean contains(int x, int y) {
        return area.contains(x, y);
    }

    private int getMaxXCoordinate() {
        int maxX = points.get(0).getXCoordinate();
        for (Point point : points) {
            point.getXCoordinate();
            if (point.getXCoordinate() > maxX) {
                maxX = point.getXCoordinate();
            }
        }
        return maxX;
    }

    private int getMinXCoordinate() {
        int minX = points.get(0).getXCoordinate();
        for (Point point : points) {
            point.getXCoordinate();
            if (point.getXCoordinate() < minX) {
                minX = point.getXCoordinate();
            }
        }
        return minX;
    }

    private int getMaxYCoordinate() {
        int maxY = points.get(0).getYCoordinate();
        for (Point point : points) {
            point.getYCoordinate();
            if (point.getYCoordinate() > maxY) {
                maxY = point.getYCoordinate();
            }
        }
        return maxY;
    }

    private int getMinYCoordinate() {
        int minY = points.get(0).getYCoordinate();
        for (Point point : points) {
            point.getYCoordinate();
            if (point.getYCoordinate() < minY) {
                minY = point.getYCoordinate();
            }
        }
        return minY;
    }

    public int getMaxWidth() {
        return getMaxXCoordinate() - getMinXCoordinate();
    }

    public int getMaxHeight() {
        return getMaxYCoordinate() - getMinYCoordinate();
    }

    public void draw(Graphics g, int scalaX, int scalaY) {
        if(scaledArea == null) {
            scaledArea = scaleArea(scalaX, scalaY);
        }
        g.setColor(new Color(101, 171, 90));
        g.fillPolygon(scaledArea);
    }

    private Polygon scaleArea(int scalaX, int scalaY) {
        Polygon scaledArea = new Polygon();
        for(Point p : points){
            scaledArea.addPoint(p.getXCoordinate() * scalaX + 75, 600 - p.getYCoordinate() * scalaY - 75);
        }
        return scaledArea;
    }
}
