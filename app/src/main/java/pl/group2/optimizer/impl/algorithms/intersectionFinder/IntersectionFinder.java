package pl.group2.optimizer.impl.algorithms.intersectionFinder;

import pl.group2.optimizer.impl.items.Vertex;
import pl.group2.optimizer.impl.items.intersections.Intersection;
import pl.group2.optimizer.impl.items.paths.Path;
import pl.group2.optimizer.impl.items.pathspoints.PathPoint;

import java.util.LinkedList;
import java.util.List;

public class IntersectionFinder {

    private final List<Path> paths;
    private final List<Intersection> intersections;
    private int intersectionId;
    private int newPathId;

    public IntersectionFinder(List<Path> paths, int maxHospitalIdForDijkstra) {
        checkIfArgumentsAreNotNull(paths, maxHospitalIdForDijkstra);
        this.paths = paths;
        intersections = new LinkedList<>();
        intersectionId = maxHospitalIdForDijkstra + 1;
        newPathId = -1;
    }

    public void findIntersectionsNaive() {
        Path firstPath;
        Path secondPath;

        boolean areAllIntersectionsFound = false;

        while(!areAllIntersectionsFound) {
            areAllIntersectionsFound =true;

            for (int i = 0; i < paths.size(); i++) {
                firstPath = paths.get(i);
                for (int j = paths.size() - 1; j > i; j--) {
                    secondPath = paths.get(j);
                    if (doIntersect(firstPath, secondPath)) {
                        PathPoint insectPoint = getIntersectionPointCoordinates(firstPath, secondPath);
                        if (insectPoint != null) {
                            Intersection intersection = new Intersection(intersectionId++, insectPoint.getXCoordinate(), insectPoint.getYCoordinate());
                            intersections.add(intersection);
                            Path[] newPaths = getPathsToBeAddedAndDeleted(intersection,firstPath,secondPath);
                            paths.add(newPaths[0]);
                            paths.add(newPaths[1]);
                            paths.add(newPaths[2]);
                            paths.add(newPaths[3]);
                            paths.remove(firstPath);
                            paths.remove(secondPath);
                            areAllIntersectionsFound = false;
                            break;
                        }
                    }
                }
            }
        }
    }

    private Path[] getPathsToBeAddedAndDeleted(Intersection intersection, Path firstPath, Path secondPath) {
        double firstPathRealDistance = checkRealDistance(firstPath.getFrom(), firstPath.getTo());
        double secondPathRealDistance = checkRealDistance(secondPath.getFrom(), secondPath.getTo());
        double firstPathFromToIntersectionRealDistance = checkRealDistance(intersection, firstPath.getFrom());
        double secondPathFromToIntersectionRealDistance = checkRealDistance(intersection, secondPath.getFrom());

        int firstPathFromToIntersectionPathDistance = (int) Math.round((firstPathFromToIntersectionRealDistance *
                firstPath.getDistance()) /
                firstPathRealDistance);
        int secondPathFromToIntersectionPathDistance = (int) Math.round((secondPathFromToIntersectionRealDistance *
                secondPath.getDistance()) /
                secondPathRealDistance);

        Path[] newPaths = new Path[4];
        newPaths[0] = new Path(newPathId--, firstPath.getFrom(), intersection, firstPathFromToIntersectionPathDistance);
        newPaths[1] = new Path(newPathId--, intersection, firstPath.getTo(), firstPath.getDistance() - firstPathFromToIntersectionPathDistance);
        newPaths[2] = new Path(newPathId--, secondPath.getFrom(), intersection, secondPathFromToIntersectionPathDistance);
        newPaths[3] = new Path(newPathId--, intersection, secondPath.getTo(), secondPath.getDistance() - secondPathFromToIntersectionPathDistance);

      return newPaths;
    }

    private double checkRealDistance(Vertex from, Vertex to) {
        double x = to.getXCoordinate() - from.getXCoordinate();
        double y = to.getYCoordinate() - from.getYCoordinate();
        return Math.sqrt((x * x) + (y * y));
    }

    private PathPoint getIntersectionPointCoordinates(Path firstPath, Path secondPath) {
        double x1 = firstPath.getFrom().getXCoordinate();
        double y1 = firstPath.getFrom().getYCoordinate();
        double x2 = firstPath.getTo().getXCoordinate();
        double y2 = firstPath.getTo().getYCoordinate();
        double u1 = secondPath.getFrom().getXCoordinate();
        double v1 = secondPath.getFrom().getYCoordinate();
        double u2 = secondPath.getTo().getXCoordinate();
        double v2 = secondPath.getTo().getYCoordinate();

        double x = -1 * ((x1 - x2) * (u1 * v2 - u2 * v1) - (u2 - u1) * (x2 * y1 - x1 * y2)) /
                ((v1 - v2) * (x1 - x2) - (u2 - u1) * (y2 - y1));

        double y = -1 * (u1 * v2 * y1 - u1 * v2 * y2 - u2 * v1 * y1 + u2 * v1 * y2 - v1 * x1 * y2 + v1 * x2 * y1 + v2 * x1 * y2 - v2 * x2 * y1) /
                (-1 * u1 * y1 + u1 * y2 + u2 * y1 - u2 * y2 + v1 * x1 - v1 * x2 - v2 * x1 + v2 * x2);


        if ((x == x1 && y == y1) || (x == x2 && y == y2) || (x == u1 && y == v1) || (x == u2 && y == v2)) {
            return null;
        }

        return new PathPoint(-1, (int) Math.round(x), (int) Math.round(y), -1, null, 1);
    }

    private int orientation(PathPoint p, PathPoint q, PathPoint r) {
        int val = (q.getYCoordinate() - p.getYCoordinate()) * (r.getXCoordinate() - q.getXCoordinate()) -
                (q.getXCoordinate() - p.getXCoordinate()) * (r.getYCoordinate() - q.getYCoordinate());

        if (val == 0) return 0;

        return (val > 0) ? 1 : 2;
    }

    private boolean doIntersect(Path firstPath, Path secondPath) {
        PathPoint p1 = new PathPoint(-1, firstPath.getFrom().getXCoordinate(), firstPath.getFrom().getYCoordinate(), -1, null, 1);
        PathPoint q1 = new PathPoint(-1, firstPath.getTo().getXCoordinate(), firstPath.getTo().getYCoordinate(), -1, null, 1);
        PathPoint p2 = new PathPoint(-1, secondPath.getFrom().getXCoordinate(), secondPath.getFrom().getYCoordinate(), -1, null, 1);
        PathPoint q2 = new PathPoint(-1, secondPath.getTo().getXCoordinate(), secondPath.getTo().getYCoordinate(), -1, null, 1);

        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        if (o1 != o2 && o3 != o4) {
            return true;
        }

        return false;
    }

    private void checkIfArgumentsAreNotNull(Object... arguments) {
        for (Object argument : arguments) {
            if (argument == null) {
                throw new IllegalArgumentException("The argument is null");
            }
        }
    }

    public List<Intersection> getIntersectionsList() {
        return intersections;
    }
}
