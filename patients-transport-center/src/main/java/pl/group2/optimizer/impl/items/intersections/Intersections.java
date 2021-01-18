package pl.group2.optimizer.impl.items.intersections;

import pl.group2.optimizer.impl.algorithms.intersectionFinder.IntersectionFinder;
import pl.group2.optimizer.impl.items.paths.Path;

import java.awt.Graphics;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static java.awt.Color.DARK_GRAY;
import static pl.group2.optimizer.gui.components.Plan.HEIGHT;
import static pl.group2.optimizer.gui.components.Plan.MARGIN;
import static pl.group2.optimizer.gui.components.Plan.PADDING;

public class Intersections {
    private List<Intersection> intersections;

    public Intersections() {
        intersections = new LinkedList<>();
    }

    public Collection<Intersection> getCollection() {
        return intersections;
    }

    public void lookForIntersections(List<Path> paths, int maxHospitalIdForDijkstra) {
        IntersectionFinder intersectionFinder = new IntersectionFinder(paths, maxHospitalIdForDijkstra);
        intersectionFinder.findIntersectionsNaive(); //od razu tworzy nowa liste dróg
        intersections = intersectionFinder.getIntersectionsList();
    }

    public void draw(Graphics g, double scalaX, double scalaY, int minX, int minY) {
        g.setColor(DARK_GRAY);
        for (Intersection intersection : intersections) {
            int xShift = 8;
            int yShift = 8;
            int x = (int) Math.round(PADDING + intersection.getXCoordinate() * scalaX + MARGIN - xShift - minX * scalaX);
            int y = (int) Math.round(PADDING + HEIGHT - (intersection.getYCoordinate() * scalaY) - MARGIN - yShift + minY * scalaY);
            g.fillOval(x, y, xShift * 2, yShift * 2);
        }
    }

    public List<Intersection> getList() {
        return intersections;
    }
}
