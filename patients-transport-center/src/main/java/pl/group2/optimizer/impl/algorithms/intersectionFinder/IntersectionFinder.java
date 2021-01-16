package pl.group2.optimizer.impl.algorithms.intersectionFinder;

import pl.group2.optimizer.impl.items.Vertex;
import pl.group2.optimizer.impl.items.intersections.Intersection;
import pl.group2.optimizer.impl.items.paths.Path;
import pl.group2.optimizer.impl.items.pathspoints.PathPoint;
import pl.group2.optimizer.impl.items.pathspoints.PathsPoints;
import pl.group2.optimizer.impl.structures.queues.PreferencePointsQueue;

import java.util.LinkedList;
import java.util.List;

public class IntersectionFinder {

    private final List<Path> paths;
    private List<PathPoint> pathsPoints;
    private List<Path> pathsToDelete;
    private int newPathId;
    private final List<Intersection> intersections;
    private int intersectionId;
    private SLStateStructure sweepingLine;
    private PreferencePointsQueue pointsQueue;
    private Path pathAbove;
    private Path pathBelow;


    public IntersectionFinder(List<Path> paths) {
        this.paths = paths;
        this.pathsToDelete = new LinkedList<>();
        this.sweepingLine = new SLStateStructure();
        this.pointsQueue = new PreferencePointsQueue();
        intersections = new LinkedList<>();
        intersectionId = 0;
        newPathId = -1;
    }

    //przerobiony algorytm Bentleya-Ottmana, nie dziala jeszcze jednak w 100 procentach
    public void findIntersections() {
        getPathsPoints();
        showPathsPoints();

        while(!pointsQueue.isEmpty()) {
            PathPoint point = pointsQueue.remove();
            //System.out.println("------------------------------");
            //pointsQueue.showQueue();
            //System.out.println("------------------------------");
            System.out.println("(" + point.getXCoordinate() + "," + point.getYCoordinate() + ")");

            if (point.isLeft() == 0) {
                sweepingLine.insertPath(point.getPath());
                sweepingLine.sortInDescendingOrder(point);

                pathAbove = sweepingLine.getAbove(point.getPath());
                pathBelow = sweepingLine.getBelow(point.getPath());

                if((pathAbove != null && pathBelow != null) && doIntersect(pathBelow,pathAbove)) {
                    IntersectionPoint insectPoint = getIntersectionPointCoordinates(pathAbove, pathBelow);
                    if(insectPoint != null) {
                        //System.out.println("(" + insectPoint.getXCoordinate() + "," + insectPoint.getYCoordinate() + ")");
                        pointsQueue.delete(insectPoint);
                    }
                }

                if(pathAbove != null && doIntersect(point.getPath(),pathAbove)) {
                    IntersectionPoint insectPoint = getIntersectionPointCoordinates(pathAbove, point.getPath());
                    if(insectPoint != null) {
                        pointsQueue.add(insectPoint);
                    }
                }
                if(pathBelow != null && doIntersect(point.getPath(),pathBelow)) {
                    IntersectionPoint insectPoint = getIntersectionPointCoordinates(point.getPath(), pathBelow);
                    if(insectPoint != null) {
                        pointsQueue.add(insectPoint);
                    }
                }
            } else if (point.isLeft() == 1) {
                pathAbove = sweepingLine.getAbove(point.getPath());
                pathBelow = sweepingLine.getBelow(point.getPath());

                sweepingLine.deletePath(point.getPath());
                sweepingLine.sortInDescendingOrder(point);

                if ((pathAbove != null && pathBelow != null) && doIntersect(pathAbove, pathBelow)) {
                    IntersectionPoint insectPoint = getIntersectionPointCoordinates(pathAbove, pathBelow);
                    if (insectPoint != null) {
                        if(!pointsQueue.contains(insectPoint)) {
                            pointsQueue.add(insectPoint);
                        }
                    }
                }
            } else {
                intersections.add(new Intersection(intersectionId, point.getXCoordinate(),point.getYCoordinate()));
                IntersectionPoint pointOfIntersection = (IntersectionPoint) point;
                Path intersectionPathAbove = pointOfIntersection.getPathAboveIntersection();
                Path intersectionPathBelow = pointOfIntersection.getPathBelowIntersection();

                sweepingLine.swap(intersectionPathAbove,intersectionPathBelow);

                pathAbove = sweepingLine.getAbove(intersectionPathBelow);
                pathBelow = sweepingLine.getBelow(intersectionPathAbove);

                if (pathAbove != null  && doIntersect(pathAbove, intersectionPathAbove)) {
                    IntersectionPoint insectPoint = getIntersectionPointCoordinates(pathAbove, intersectionPathAbove);
                    if (insectPoint != null) {
                        if(!pointsQueue.contains(insectPoint)) {
                            pointsQueue.add(insectPoint);
                        }
                    }
                }


                if (pathBelow != null  && doIntersect(pathBelow, intersectionPathBelow)) {
                    IntersectionPoint insectPoint = getIntersectionPointCoordinates(intersectionPathBelow, pathBelow);
                    if (insectPoint != null) {
                        if(!pointsQueue.contains(insectPoint)) {
                            pointsQueue.add(insectPoint);
                        }
                    }

                }

                if (pathAbove != null  && doIntersect(pathAbove, intersectionPathBelow)) {
                    IntersectionPoint insectPoint = getIntersectionPointCoordinates(pathAbove, intersectionPathBelow);
                    if (insectPoint != null) {
                        if(!pointsQueue.contains(insectPoint)) {
                            pointsQueue.add(insectPoint);
                        }
                        //System.out.println("(" + insectPoint.getXCoordinate() + "," + insectPoint.getYCoordinate() + ")");
                        //System.out.println("PERLUCCI");
                    }

                }
                if (pathBelow != null  && doIntersect(pathBelow, intersectionPathAbove)) {
                    IntersectionPoint insectPoint = getIntersectionPointCoordinates(intersectionPathAbove, pathBelow);
                    if (insectPoint != null) {
                        if(!pointsQueue.contains(insectPoint)) {
                            pointsQueue.add(insectPoint);
                        }
                        //System.out.println("(" +insectPoint.getXCoordinate() +"," +insectPoint.getYCoordinate() + ")");
                        //System.out.println("SKIERWA");
                    }

                }
            }
        }
        updatePathsList();
    }


    //naiwna wersja algorytmu, działa jak trzeba ale jest wolniejsza
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
                        IntersectionPoint insectPoint = getIntersectionPointCoordinates(firstPath, secondPath);
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

    private void updatePathsList() {
        for(Path pathy: pathsToDelete) {
            if(paths.contains(pathy)) {
                paths.remove(pathy);
            }
        }
    }

    private void showPathsPoints() {
        pointsQueue.showQueue();
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
        pointsQueue.addAll(pathsPoints.getList());
    }

    private IntersectionPoint getIntersectionPointCoordinates(Path pathAbove, Path pathBelow) {
        double x1 = pathAbove.getFrom().getXCoordinate();
        double y1 = pathAbove.getFrom().getYCoordinate();
        double x2 = pathAbove.getTo().getXCoordinate();
        double y2 = pathAbove.getTo().getYCoordinate();
        double u1 = pathBelow.getFrom().getXCoordinate();
        double v1 = pathBelow.getFrom().getYCoordinate();
        double u2 = pathBelow.getTo().getXCoordinate();
        double v2 = pathBelow.getTo().getYCoordinate();

        double x = -1 * ((x1 - x2) * (u1 * v2 - u2 * v1) - (u2 - u1) * (x2 * y1 - x1 * y2)) /
                ((v1 - v2) * (x1 - x2) - (u2 - u1) * (y2 - y1));

        double y = -1 * (u1 * v2 * y1 - u1 * v2 * y2 - u2 * v1 * y1 + u2 * v1 * y2 - v1 * x1 * y2 + v1 * x2 * y1 + v2 * x1 * y2 - v2 * x2 * y1) /
                (-1 * u1 * y1 + u1 * y2 + u2 * y1 - u2 * y2 + v1 * x1 - v1 * x2 - v2 * x1 + v2 * x2);
/*
        String first = String.format("From points: (%f,%f) <-> (%f,%f)",x1,y1,x2,y2);
        String second = String.format("To points: (%f,%f) <-> (%f,%f)",u1,v1,u2,v2);
        System.out.println(first);
        System.out.println(second);

 */

        if ((x == x1 && y == y1) || (x == x2 && y == y2) || (x == u1 && y == v1) || (x == u2 && y == v2)) {
            return null;
        }


        return new IntersectionPoint(-1, (int) Math.round(x), (int) Math.round(y), -1, null, 1, pathAbove, pathBelow);
    }

    private int orientation(IntersectionPoint p, IntersectionPoint q, IntersectionPoint r) {
        int val = (q.getYCoordinate() - p.getYCoordinate()) * (r.getXCoordinate() - q.getXCoordinate()) -
                (q.getXCoordinate() - p.getXCoordinate()) * (r.getYCoordinate() - q.getYCoordinate());

        if (val == 0) return 0;

        return (val > 0) ? 1 : 2;
    }

    private boolean doIntersect(Path firstPath, Path secondPath) {
        IntersectionPoint p1 = new IntersectionPoint(-1, firstPath.getFrom().getXCoordinate(), firstPath.getFrom().getYCoordinate(), -1, null, 1, null, null);
        IntersectionPoint q1 = new IntersectionPoint(-1, firstPath.getTo().getXCoordinate(), firstPath.getTo().getYCoordinate(), -1, null, 1, null, null);
        IntersectionPoint p2 = new IntersectionPoint(-1, secondPath.getFrom().getXCoordinate(), secondPath.getFrom().getYCoordinate(), -1, null, 1, null, null);
        IntersectionPoint q2 = new IntersectionPoint(-1, secondPath.getTo().getXCoordinate(), secondPath.getTo().getYCoordinate(), -1, null, 1, null, null);

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
}
