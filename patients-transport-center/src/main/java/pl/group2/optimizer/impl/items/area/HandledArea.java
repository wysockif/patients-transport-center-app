package pl.group2.optimizer.impl.items.area;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.List;

import static pl.group2.optimizer.gui.components.Plan.HEIGHT;
import static pl.group2.optimizer.gui.components.Plan.MARGIN;
import static pl.group2.optimizer.gui.components.Plan.PADDING;

public class HandledArea {
    private final Polygon area;
    private List<Point> points;
    private Polygon scaledArea;

    private int minX;
    private int minY;
    private double scaleX;
    private double scaleY;

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
        minX = points.get(0).getXCoordinate();
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
        minY = points.get(0).getYCoordinate();
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

    public void draw(Graphics g, double scalaX, double scalaY) {
        if (scaledArea == null) {
            scaledArea = scaleArea(scalaX, scalaY);
        }
        g.setColor(new Color(101, 171, 90));
        g.fillPolygon(scaledArea);
    }

    private Polygon scaleArea(double scalaX, double scalaY) {
        Polygon scaledArea = new Polygon();
        for (Point p : points) {
            int x = (int) Math.round(PADDING + p.getXCoordinate() * scalaX + MARGIN - minX * scalaX);
            int y = (int) Math.round(PADDING + HEIGHT - p.getYCoordinate() * scalaY - MARGIN + minY * scalaY);

            scaledArea.addPoint(x, y);
        }
        return scaledArea;
    }

    public int getMinX() {
        return minX;
    }

    public int getMinY() {
        return minY;
    }

    public void setScales(double scaleX, double scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    public double getScaleX() {
        return scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }
}
