package pl.group2.optimizer.impl.items.pathspoints;

import pl.group2.optimizer.impl.items.hospitals.Hospital;
import pl.group2.optimizer.impl.items.hospitals.Hospitals;
import pl.group2.optimizer.impl.items.paths.Path;

import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class PathsPoints {
    private final List<PathPoint> pathsPoints;
    private final List<Path> pathsList;
    private int leftX;
    private int leftY;
    private int rightX;
    private int rightY;
    private Hospital startingPoint;
    private Hospital destinationPoint;

    public PathsPoints(List<Path> pathsList) {
        pathsPoints = new LinkedList<>();
        this.pathsList = pathsList;
    }

    public void createPathsPoints() {
        // do refaktoryzacji
        for(Path selectedPath: pathsList) {
            checkIfArgumentIsNull(selectedPath);
            startingPoint = (Hospital) selectedPath.getFrom();
            destinationPoint = (Hospital) selectedPath.getTo();

            if(startingPoint.getXCoordinate() < destinationPoint.getXCoordinate()) {
                leftX = startingPoint.getXCoordinate();
                leftY = startingPoint.getXCoordinate();
                rightX = destinationPoint.getXCoordinate();
                rightY = destinationPoint.getYCoordinate();
            } else if(startingPoint.getXCoordinate() > destinationPoint.getXCoordinate()) {
                leftX = destinationPoint.getXCoordinate();
                leftY = destinationPoint.getXCoordinate();
                rightX = startingPoint.getXCoordinate();
                rightY = startingPoint.getYCoordinate();
            } else {
                if(startingPoint.getYCoordinate() < destinationPoint.getYCoordinate()) {
                    leftX = startingPoint.getXCoordinate();
                    leftY = startingPoint.getXCoordinate();
                    rightX = destinationPoint.getXCoordinate();
                    rightY = destinationPoint.getYCoordinate();
                } else if(startingPoint.getYCoordinate() > destinationPoint.getYCoordinate()) {
                    leftX = destinationPoint.getXCoordinate();
                    leftY = destinationPoint.getXCoordinate();
                    rightX = startingPoint.getXCoordinate();
                    rightY = startingPoint.getYCoordinate();
                } else {
                    throw new IllegalArgumentException("Nie ma takiej drogi, jest to punkt, w którym leży szpital");
                }
            }

            pathsPoints.add(new PathPoint(selectedPath.getId(), leftX, leftY, 0));
            pathsPoints.add(new PathPoint(selectedPath.getId(), rightX, rightY, 1));
        }

    }

    public boolean contains(PathPoint pathPoint) {
        checkIfArgumentIsNull(pathPoint);
        return pathsPoints.contains(pathPoint);
    }

    public int size() {
        return pathsPoints.size();
    }

    public PathPoint get(int index) {
        return pathsPoints.get(index);
    }

    private void checkIfArgumentIsNull(Object argument) {
        if (argument == null) {
            throw new IllegalArgumentException("Niezainicjowany argument");
        }
    }

    public List<PathPoint> getList() {
        return pathsPoints;
    }
}
