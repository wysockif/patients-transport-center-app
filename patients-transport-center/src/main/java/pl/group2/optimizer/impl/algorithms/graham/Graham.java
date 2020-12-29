package pl.group2.optimizer.impl.algorithms.graham;

import pl.group2.optimizer.impl.items.Point;
import pl.group2.optimizer.impl.items.hospitals.Hospitals;
import pl.group2.optimizer.impl.items.specialobjects.SpecialObjects;
import pl.group2.optimizer.impl.structures.queues.QueueLIFO;

import java.util.LinkedList;
import java.util.List;

public class Graham {
    private final List<Point> points;
    private final QueueLIFO<Point> stack;
    private Point lowestPoint;

    public Graham() {
        points = new LinkedList<>();
        stack = new QueueLIFO<>();
    }

    public void loadPoints(Hospitals hospitals, SpecialObjects specialObjects) {
        checkIfArgumentsAreNotNull(hospitals, specialObjects);
        points.addAll(hospitals.getCollection());
        points.addAll(specialObjects.getCollection());
    }

    public void findConvexHull() {
        findTheLowestPoint();
        points.sort(this::comparePoints);

        printList();
    }

    private void printList() {
        System.out.println("Lowest: x: " + lowestPoint.getXCoordinate() + " | y: " + lowestPoint.getYCoordinate());
        for (Point p : points) {
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
        }
        else return -ccw(lowestPoint, q1, q2);     // both above or below
        // Note: ccw() recomputes dx1, dy1, dx2, and dy2
    }

    // is a->b->c a counter-clockwise turn?
    // -1 if clockwise, +1 if counter-clockwise, 0 if collinear
    public static int ccw(Point a, Point b, Point c) {
        double area2 = (b.getXCoordinate() - a.getXCoordinate()) *
                (c.getYCoordinate() - a.getYCoordinate()) - (b.getYCoordinate() - a.getYCoordinate()) *
                (c.getXCoordinate() - a.getXCoordinate());
        if (area2 < 0) return -1;
        else if (area2 > 0) return +1;
        else return 0;
    }

    private void findTheLowestPoint() {
        // sprawdzenie czy jakikolwiek istnieje będzie przed wywołaniem tej funkcji dlatego pobieram "zerowy" bez sprawdzania
        lowestPoint = points.get(0);
        for (Point point : points) {
            if (point.getYCoordinate() < lowestPoint.getYCoordinate()) {
                lowestPoint = point;
            } else if (point.getYCoordinate() == lowestPoint.getYCoordinate()) {
                lowestPoint = findLeftmostPoint(point, lowestPoint);
            }
        }
        points.remove(lowestPoint);
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
