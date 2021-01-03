package pl.group2.optimizer.impl.algorithms.graham;

import pl.group2.optimizer.impl.items.area.Point;
import pl.group2.optimizer.impl.items.hospitals.Hospitals;
import pl.group2.optimizer.impl.items.specialobjects.SpecialObjects;
import pl.group2.optimizer.impl.structures.queues.QueueLIFO;

import java.util.LinkedList;
import java.util.List;

public class Graham {
    private final List<Point> points;
    private final QueueLIFO<Point> queueLIFO;
    private Point lowestPoint;

    public Graham() {
        points = new LinkedList<>();
        queueLIFO = new QueueLIFO<>();
    }

    public void loadPoints(Hospitals hospitals, SpecialObjects specialObjects) {
        checkIfArgumentsAreNotNull(hospitals, specialObjects);
        points.addAll(hospitals.getCollection());
        points.addAll(specialObjects.getCollection());
    }

    public List<Point> findConvexHull() {
        if (points.isEmpty()) {
            throw new UnsupportedOperationException("Cannot handle the empty list");
        }
        findTheLowestPoint();
        points.sort(this::comparePoints);
        print(points);
        if (points.size() < 2) {
            return new LinkedList<>();
        }
        runScanning();
        return rewriteStackToList(queueLIFO);
    }

    private void print(List<Point> points) {
        for(Point p : points){
            System.out.println(p);
        }
    }

    private void runScanning() {
        for (int i = 0; i < 3; i++) {
            queueLIFO.add(points.get(i));
        }
        for (int i = 3; i < points.size(); i++) {
            Point currentPoint = points.get(i);
            while (queueLIFO.size() > 1 && counterClockwise(queueLIFO.nextToTop(), queueLIFO.top(), currentPoint) <= 0) {
                queueLIFO.remove();
            }
            queueLIFO.add(currentPoint);
        }
    }

    private List<Point> rewriteStackToList(QueueLIFO<Point> stack) {
        LinkedList<Point> list = new LinkedList<>();
        while (!stack.isEmpty()) {
            list.addLast(stack.remove());
        }
        return list;
    }

    private int comparePoints(Point q1, Point q2) {
        double dx1 = q1.getXCoordinate() - lowestPoint.getXCoordinate();
        double dy1 = q1.getYCoordinate() - lowestPoint.getYCoordinate();
        double dx2 = q2.getXCoordinate() - lowestPoint.getXCoordinate();
        double dy2 = q2.getYCoordinate() - lowestPoint.getYCoordinate();
        if (isFirstPointAboveAndSecondBelow(dy1, dy2)) {
            return -1;
        } else if (isFirstPointBelowAndSecondAbove(dy1, dy2)) {
            return 1;
        } else if (arePointsCollinear(dy1, dy2)) {
            return checkVerticalPosition(dx1, dx2);
        } else return -counterClockwise(lowestPoint, q1, q2);
    }

    private int checkVerticalPosition(double dx1, double dx2) {
        if (isFirstPointToTheRightAndSecondToTheLeft(dx1, dx2)) {
            return -1;
        } else if (isFirstPointToTheLeftAndSecondToTheRight(dx1, dx2)) {
            return 1;
        } else {
            return 0;
        }
    }

    private boolean isFirstPointToTheLeftAndSecondToTheRight(double dx1, double dx2) {
        return dx2 >= 0 && dx1 < 0;
    }

    private boolean isFirstPointToTheRightAndSecondToTheLeft(double dx1, double dx2) {
        return dx1 >= 0 && dx2 < 0;
    }

    private boolean arePointsCollinear(double dy1, double dy2) {
        return dy1 == 0 && dy2 == 0;
    }

    private boolean isFirstPointBelowAndSecondAbove(double dy1, double dy2) {
        return dy2 >= 0 && dy1 < 0;
    }

    private boolean isFirstPointAboveAndSecondBelow(double dy1, double dy2) {
        return dy1 >= 0 && dy2 < 0;
    }

    public static int counterClockwise(Point a, Point b, Point c) {
        double area = (b.getXCoordinate() - a.getXCoordinate()) *
                (c.getYCoordinate() - a.getYCoordinate()) - (b.getYCoordinate() - a.getYCoordinate()) *
                (c.getXCoordinate() - a.getXCoordinate());
        if (isClockwise(area)) {
            return -1;
        } else if (isCounterClockwise(area)) {
            return 1;
        } else {
            return 0;
        }
    }

    private static boolean isCounterClockwise(double area) {
        return area > 0;
    }

    private static boolean isClockwise(double area) {
        return area < 0;
    }

    private void findTheLowestPoint() {
        lowestPoint = points.get(0);
        for (Point point : points) {
            if (point.getYCoordinate() < lowestPoint.getYCoordinate()) {
                lowestPoint = point;
            } else if (point.getYCoordinate() == lowestPoint.getYCoordinate()) {
                lowestPoint = findLeftmostPoint(point, lowestPoint);
            }
        }
    }

    private Point findLeftmostPoint(Point currentPoint, Point lowestPoint) {
        if (currentPoint.getXCoordinate() < lowestPoint.getXCoordinate()) {
            return currentPoint;
        }
        return lowestPoint;
    }

    private void checkIfArgumentsAreNotNull(Object... arguments) {
        for (Object argument : arguments) {
            if (argument == null) {
                throw new IllegalArgumentException("The argument is null");
            }
        }
    }
}
