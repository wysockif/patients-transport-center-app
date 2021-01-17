package pl.group2.optimizer.impl.items.intersections;

import pl.group2.optimizer.impl.algorithms.intersectionFinder.IntersectionFinder;
import pl.group2.optimizer.impl.io.TextFileReader;
import pl.group2.optimizer.impl.items.paths.Path;
import pl.group2.optimizer.impl.items.pathspoints.PathPoint;
import pl.group2.optimizer.impl.items.pathspoints.PathsPoints;

import java.awt.Graphics;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static java.awt.Color.RED;
import static pl.group2.optimizer.gui.components.Plan.HEIGHT;
import static pl.group2.optimizer.gui.components.Plan.MARGIN;
import static pl.group2.optimizer.gui.components.Plan.PADDING;

public class Intersections {
    private List<Intersection> intersections;
    private IntersectionFinder intersectionFinder;

    public Intersections() {
        intersections = new LinkedList<>();
    }

    public Collection<Intersection> getCollection() {
        return intersections;
    }

    public void lookForIntersections(List<Path> paths, int maxHospitalIdForDijkstra) {
        intersectionFinder = new IntersectionFinder(paths);
        intersectionFinder.findIntersectionsNaive(); //od razu tworzy nowa liste dróg
        intersections = intersectionFinder.getIntersectionsList();
    }

    public void draw(Graphics g, double scalaX, double scalaY, int minX, int minY) {
        g.setColor(RED);
        for (Intersection intersection : intersections) {
            int xShift = 10;
            int yShift = 10;

            int x = (int) Math.round(PADDING + intersection.getXCoordinate() * scalaX + MARGIN - xShift - minX * scalaX);
            int y = (int) Math.round(PADDING + HEIGHT - (intersection.getYCoordinate() * scalaY) - MARGIN - yShift + minY * scalaY);
            g.fillOval(x, y, xShift * 2, yShift * 2);
        }
    }

    public List<Intersection> getList() {
        return intersections;
    }
}
