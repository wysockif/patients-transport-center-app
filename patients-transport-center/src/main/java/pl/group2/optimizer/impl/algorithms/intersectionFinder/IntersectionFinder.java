package pl.group2.optimizer.impl.algorithms.intersectionFinder;

import pl.group2.optimizer.impl.items.paths.Path;
import pl.group2.optimizer.impl.items.pathspoints.PathPoint;
import pl.group2.optimizer.impl.items.pathspoints.PathsPoints;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class IntersectionFinder {

    private final List<Path> paths;
    private List<PathPoint> pathsPoints;

    public IntersectionFinder(List<Path> paths) {
        this.paths = paths;
    }

    public void findIntersections() {
        getPathsPoints();
        sortPathsPoints();
        for (PathPoint point: pathsPoints) {
            System.out.println(point.toString());
        }
    }

    private void getPathsPoints() {
        PathsPoints pathsPoints = new PathsPoints(paths);
        pathsPoints.createPathsPoints();
        this.pathsPoints = pathsPoints.getList();
    }

    private void sortPathsPoints() {
        Collections.sort(pathsPoints,Comparator.comparing(PathPoint::getXCoordinate)
                .thenComparing(PathPoint::isLeft)
                .thenComparing(PathPoint::getYCoordinate));
    }
}
