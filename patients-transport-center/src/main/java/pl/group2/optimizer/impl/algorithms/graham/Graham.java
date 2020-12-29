package pl.group2.optimizer.impl.algorithms.graham;

import pl.group2.optimizer.impl.items.Point;
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
        if (points.size() < 2) {
            return new LinkedList<>();
        }
        printList(points);
        System.out.println("---------------------");

        for (int i = 0; i < 3; i++) {
            queueLIFO.add(points.get(i));
            System.out.println("Dodanie: " +  points.get(i));
        }

        for (int i = 3; i < points.size(); i++) {
            Point currentPoint = points.get(i);

            while (queueLIFO.size() > 1 && counterClockwiseChecker(queueLIFO.nextToTop(), queueLIFO.top(), currentPoint) <= 0) {
                Point p = queueLIFO.remove();
                System.out.println("Skasowanie: " + p);
            }
            queueLIFO.add(currentPoint);
            System.out.println("Dodanie: " +  currentPoint);
        }

        List<Point> pointsList = rewriteStackToList(queueLIFO);
//        printList(pointsList);
        return pointsList;
    }

    private List<Point> rewriteStackToList(QueueLIFO<Point> stack) {
        LinkedList<Point> list = new LinkedList<>();
        while (!stack.isEmpty()) {
            list.addLast(stack.remove());
        }
        return list;
    }

    private void printList(List<Point> list) {
        for (Point p : list) {
            System.out.println("x: " + p.getXCoordinate() + " | y: " + p.getYCoordinate());
        }
    }

    private int comparePoints(Point q1, Point q2) {
        double dx1 = q1.getXCoordinate() - lowestPoint.getXCoordinate();
        double dy1 = q1.getYCoordinate() - lowestPoint.getYCoordinate();
        double dx2 = q2.getXCoordinate() - lowestPoint.getXCoordinate();
        double dy2 = q2.getYCoordinate() - lowestPoint.getYCoordinate();
        if (dy1 >= 0 && dy2 < 0) return -1;    // q1 above; q2 below
        else if (dy2 >= 0 && dy1 < 0) return +1;    // q1 below; q2 above
        else if (dy1 == 0 && dy2 == 0) {            // 3-collinear and horizontal
            if (dx1 >= 0 && dx2 < 0) return -1;
            else if (dx2 >= 0 && dx1 < 0) return +1;
            else return 0;
        } else return -counterClockwiseChecker(lowestPoint, q1, q2);     // both above or below
    }

    // -1 if clockwise, +1 if counter-clockwise, 0 if collinear
    public static int counterClockwiseChecker(Point a, Point b, Point c) {
        double area2 = (b.getXCoordinate() - a.getXCoordinate()) *
                (c.getYCoordinate() - a.getYCoordinate()) - (b.getYCoordinate() - a.getYCoordinate()) *
                (c.getXCoordinate() - a.getXCoordinate());
        if (area2 < 0) return -1;
        else if (area2 > 0) return +1;
        else return 0;
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
