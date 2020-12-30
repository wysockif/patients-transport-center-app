package pl.group2.optimizer.impl.items.area;

import java.awt.Polygon;
import java.util.List;

public class HandledArea {
    private Polygon area;
    private List<Point> points;

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
}
