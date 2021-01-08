package pl.group2.optimizer.impl.items.pathspoints;

import pl.group2.optimizer.impl.items.Vertex;
import pl.group2.optimizer.impl.items.area.Point;

public class PathPoint extends Vertex implements Point {

    private final int leftOrRight;

    public PathPoint(int id, int xCoordinate, int yCoordinate, int leftOrRight) {
        super(id, xCoordinate, yCoordinate);
        this.leftOrRight = leftOrRight;
    }

    public boolean isLeft() {
        return leftOrRight == 0;
    }

    public int getPathId() {
        return getId();
    }

    @Override
    public String toString() {
        return "PathPoint{" +
                "id=" + this.getId() +
                ", x=" + this.getXCoordinate() +
                ", y=" + this.getYCoordinate() +
                "left?=" + this.isLeft() +
                "path id=" + this.getPathId() +
                '}';
    }
}
