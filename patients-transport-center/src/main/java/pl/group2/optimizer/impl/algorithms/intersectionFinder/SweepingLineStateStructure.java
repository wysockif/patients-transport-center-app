package pl.group2.optimizer.impl.algorithms.intersectionFinder;

import pl.group2.optimizer.impl.items.paths.Path;
import pl.group2.optimizer.impl.items.pathspoints.PathPoint;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class SweepingLineStateStructure {

    private final List<Path> sweepingLineList;
    private final List<PathPoint> supportPathsPointsList;

    public SweepingLineStateStructure() {
        sweepingLineList = new LinkedList<>();
        supportPathsPointsList = new LinkedList<>();
    }

    public void insertPath(Path path) {
        sweepingLineList.add(path);
    }

    public void deletePath(Path path) {
        sweepingLineList.remove(path);
    }

    public Path getAbove(Path path) {
        checkIfArgumentIsNotNull(path);
        int pathIndex = sweepingLineList.indexOf(path);

        if(sweepingLineList.size() < 2 || pathIndex == 0) {
            return null;
        }

        return sweepingLineList.get(pathIndex-1);
    }

    public Path getBelow(Path path) {
        checkIfArgumentIsNotNull(path);
        int pathIndex = sweepingLineList.indexOf(path);

        if(sweepingLineList.size() < 2 || pathIndex == (sweepingLineList.size()-1)) {
            return null;
        }

        return sweepingLineList.get(pathIndex + 1);
    }

    public void sortInDescendingOrder(PathPoint point) {

        if(sweepingLineList.size() > 1) {
            int sweepingLineX = point.getXCoordinate();

            for (Path segment : sweepingLineList) {
                PathPoint insectWithSweepingLinePoint = getPathSweepingLineIntersectionPoint(segment, sweepingLineX);

                if(insectWithSweepingLinePoint != null) {
                    supportPathsPointsList.add(insectWithSweepingLinePoint);
                }
            }
            sortSupportPathsPoints();
            sortPathsInSweepingStructure();
        }
    }

    private void sortSupportPathsPoints() {
        Collections.sort(supportPathsPointsList, Comparator.comparing(PathPoint::getYCoordinate).reversed());

    }

    private void sortPathsInSweepingStructure() {
        sweepingLineList.clear();

        for(PathPoint point: supportPathsPointsList) {
            sweepingLineList.add(point.getPath());
        }

        supportPathsPointsList.clear();
    }

    private void checkIfArgumentIsNotNull(Object argument) {
        if (argument == null) {
            throw new IllegalArgumentException("Niezainicjowany argument");
        }
    }

    private PathPoint getPathSweepingLineIntersectionPoint(Path path, int intersectionPointX) {
        double x1 = path.getFrom().getXCoordinate();
        double y1 = path.getFrom().getYCoordinate();
        double x2 = path.getTo().getXCoordinate();
        double y2 = path.getTo().getYCoordinate();

        if(x1 == x2) {
            if(y1 > y2) {
                return new PathPoint(-1, (int) x1, (int) y2, -1, path,-1);
            } else {
                return new PathPoint(-1, (int) x1, (int) y1, -1, path,-1);
            }
        }

        double a = (y1 - y2) / (x1 - x2);
        double b = y1 - (x1 * a);

        int intersectionPointY = (int) Math.round((a * intersectionPointX) + b);

        return new PathPoint(-1,intersectionPointX,intersectionPointY,-1,path,-1);
    }
}
