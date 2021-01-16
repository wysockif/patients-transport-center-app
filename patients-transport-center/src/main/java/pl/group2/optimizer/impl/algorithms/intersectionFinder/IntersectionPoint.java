package pl.group2.optimizer.impl.algorithms.intersectionFinder;

import pl.group2.optimizer.impl.items.paths.Path;
import pl.group2.optimizer.impl.items.pathspoints.PathPoint;

public class IntersectionPoint extends PathPoint {

    private final Path pathAboveIntersection;
    private final Path pathBelowIntersection;

    public IntersectionPoint(int id, int xCoordinate, int yCoordinate, int leftOrRight, Path path, int isVerticalPath, Path pathAboveIntersection, Path pathBelowIntersection) {
        super(id, xCoordinate, yCoordinate, leftOrRight, path, isVerticalPath);
        this.pathAboveIntersection = pathAboveIntersection;
        this.pathBelowIntersection = pathBelowIntersection;
    }

    @Override
    public String toString() {
        return "IntersectionPoint{" +
                "pathAboveIntersection=" + pathAboveIntersection +
                ", pathBelowIntersection=" + pathBelowIntersection +
                '}';
    }

    public Path getPathAboveIntersection() {
        return pathAboveIntersection;
    }

    public Path getPathBelowIntersection() {
        return pathBelowIntersection;
    }
}
