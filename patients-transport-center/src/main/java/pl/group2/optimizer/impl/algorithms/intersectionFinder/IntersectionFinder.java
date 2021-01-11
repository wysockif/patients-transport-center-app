package pl.group2.optimizer.impl.algorithms.intersectionFinder;

import pl.group2.optimizer.impl.items.Vertex;
import pl.group2.optimizer.impl.items.intersections.Intersection;
import pl.group2.optimizer.impl.items.paths.Path;
import pl.group2.optimizer.impl.items.pathspoints.PathPoint;
import pl.group2.optimizer.impl.items.pathspoints.PathsPoints;

import java.util.*;

public class IntersectionFinder {

    private final List<Path> paths;
    private List<Path> pathsToAdd;
    private List<Path> pathsToDelete;
    private int newPathId;
    private List<PathPoint> pathsPoints;
    private final List<Intersection> intersections;
    private int intersectionId;
    private SweepingLineStateStructure sweepingLine;
    private Path pathAbove;
    private Path pathBelow;


    public IntersectionFinder(List<Path> paths) {
        this.paths = paths;
        this.pathsToAdd = new LinkedList<>();
        this.pathsToDelete = new LinkedList<>();
        this.sweepingLine = new SweepingLineStateStructure();
        intersections = new LinkedList<>();
        intersectionId = 0;
        newPathId = -1;
    }

    public void findIntersections() {
        getPathsPoints();
        sortPathsPoints();

        for(PathPoint point: pathsPoints) {
            if(point.isLeft()==0) {
                sweepingLine.insertPath(point.getPath());
                sweepingLine.sortInDescendingOrder(point);

                pathAbove = sweepingLine.getAbove(point.getPath());
                pathBelow = sweepingLine.getBelow(point.getPath());

                if(pathAbove != null && doIntersect(pathAbove, point.getPath())) {
                     IntersectionPoint insectPoint = getIntersectionPointCoordinates(pathAbove,point.getPath());
                     if(insectPoint != null) {
                         Intersection intersection = new Intersection(intersectionId++, insectPoint.getXCoordinate(), insectPoint.getYCoordinate());
                         intersections.add(intersection);
                         getPathsToBeAddedAndDeleted(intersection,pathAbove,point.getPath());
                     }
                }
                if(pathBelow != null && doIntersect(pathBelow, point.getPath())) {
                    IntersectionPoint insectPoint = getIntersectionPointCoordinates(pathBelow, point.getPath());
                    if(insectPoint != null) {
                        Intersection intersection = new Intersection(intersectionId++, insectPoint.getXCoordinate(), insectPoint.getYCoordinate());
                        intersections.add(intersection);
                        getPathsToBeAddedAndDeleted(intersection,pathBelow,point.getPath());
                    }
                }
            }
            if(point.isLeft()==1) {
                pathAbove = sweepingLine.getAbove(point.getPath());
                pathBelow = sweepingLine.getBelow(point.getPath());

                sweepingLine.deletePath(point.getPath());
                sweepingLine.sortInDescendingOrder(point);

                if((pathAbove != null && pathBelow != null) && doIntersect(pathAbove,pathBelow)) {
                    IntersectionPoint insectPoint = getIntersectionPointCoordinates(pathAbove,pathBelow);
                    if(insectPoint != null) {
                        Intersection intersection = new Intersection(intersectionId++, insectPoint.getXCoordinate(), insectPoint.getYCoordinate());
                        intersections.add(intersection);
                        getPathsToBeAddedAndDeleted(intersection,pathBelow,pathAbove);
                    }
                }
            }
        }
        updatePathsList();
    }

    private void getPathsToBeAddedAndDeleted(Intersection intersection, Path firstPath, Path secondPath) {
        double firstPathRealDistance = checkRealDistance(firstPath.getFrom(),firstPath.getTo());
        double secondPathRealDistance = checkRealDistance(secondPath.getFrom(),secondPath.getTo());
        double firstPathFromToIntersectionRealDistance = checkRealDistance(intersection,firstPath.getFrom());
        double secondPathFromToIntersectionRealDistance = checkRealDistance(intersection,secondPath.getFrom());

        int firstPathFromToIntersectionPathDistance = (int) Math.round((firstPathFromToIntersectionRealDistance *
                                                                            firstPath.getDistance()) /
                                                                            firstPathRealDistance);
        int secondPathFromToIntersectionPathDistance = (int) Math.round((secondPathFromToIntersectionRealDistance *
                                                                            secondPath.getDistance()) /
                                                                            secondPathRealDistance);

        pathsToAdd.add(new Path(newPathId--,firstPath.getFrom(),intersection,firstPathFromToIntersectionPathDistance));
        pathsToAdd.add(new Path(newPathId--,firstPath.getTo(),intersection,firstPath.getDistance() - firstPathFromToIntersectionPathDistance));
        pathsToAdd.add(new Path(newPathId--,secondPath.getFrom(),intersection,secondPathFromToIntersectionPathDistance));
        pathsToAdd.add(new Path(newPathId--,secondPath.getTo(),intersection,secondPath.getDistance()-secondPathFromToIntersectionPathDistance));

        if(!pathsToDelete.contains(firstPath)) {
            pathsToDelete.add(firstPath);
        }

        if(!pathsToDelete.contains(secondPath)) {
            pathsToDelete.add(secondPath);
        }
    }

    private void updatePathsList() {
        for(Path deletedPath: pathsToDelete) {
            if(paths.contains(deletedPath)) {
                paths.remove(deletedPath);
            }
        }
        for(Path addedPath: pathsToAdd) {
            paths.add(addedPath);
        }
    }

    private double checkRealDistance(Vertex from, Vertex to) {
        double x = to.getXCoordinate() - from.getXCoordinate();
        double y = to.getYCoordinate() - from.getYCoordinate();
        return Math.sqrt((x * x) + (y * y));
    }


    private void getPathsPoints() {
        PathsPoints pathsPoints = new PathsPoints(paths);
        pathsPoints.createPathsPoints();
        this.pathsPoints = pathsPoints.getList();
    }

    private void sortPathsPoints() {
        Collections.sort(pathsPoints,Comparator.comparing(PathPoint::getXCoordinate)
                .thenComparing(PathPoint::isLeft)
                .thenComparing(PathPoint::getYCoordinate)
                .thenComparing(PathPoint::isVerticalPath));
    }

    private IntersectionPoint getIntersectionPointCoordinates(Path firstPath, Path secondPath) {
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

        if((x == x1 && y == y1) || (x == x2 && y == y2) || (x == u1 && y == v1) || (x == u2 && y == v2)) {
            return null;
        }

        return new IntersectionPoint((int)Math.round(x),(int) Math.round(y));
    }

    private int orientation(IntersectionPoint p, IntersectionPoint q, IntersectionPoint r) {
        int val = (q.getYCoordinate() - p.getYCoordinate()) * (r.getXCoordinate() - q.getXCoordinate()) -
                (q.getXCoordinate() - p.getXCoordinate()) * (r.getYCoordinate() - q.getYCoordinate());

        if (val == 0) return 0;

        return (val > 0)? 1: 2;
    }

    private boolean doIntersect(Path firstPath, Path secondPath) {
        IntersectionPoint p1 = new IntersectionPoint(firstPath.getFrom().getXCoordinate(),firstPath.getFrom().getYCoordinate());
        IntersectionPoint q1 = new IntersectionPoint(firstPath.getTo().getXCoordinate(),firstPath.getTo().getYCoordinate());
        IntersectionPoint p2 = new IntersectionPoint(secondPath.getFrom().getXCoordinate(),secondPath.getFrom().getYCoordinate());
        IntersectionPoint q2 = new IntersectionPoint(secondPath.getTo().getXCoordinate(),secondPath.getTo().getYCoordinate());

        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        if (o1 != o2 && o3 != o4) {
            return true;
        }

        return false;
    }

    public List<Intersection> getIntersectionsList() {
        return intersections;
    }

    public List<Path> getNewPaths() {
        return paths;
    }
}
