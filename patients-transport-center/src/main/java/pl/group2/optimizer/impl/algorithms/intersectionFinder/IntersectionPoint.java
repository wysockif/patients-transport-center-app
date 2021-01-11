package pl.group2.optimizer.impl.algorithms.intersectionFinder;

import pl.group2.optimizer.impl.items.area.Point;

public class IntersectionPoint implements Point {

    private final int x;
    private final int y;

    public IntersectionPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getXCoordinate() {
        return x;
    }

    @Override
    public int getYCoordinate() {
        return y;
    }
}
