package pl.group2.optimizer.impl.items.pathspoints;

import pl.group2.optimizer.impl.items.Vertex;
import pl.group2.optimizer.impl.items.area.Point;
import pl.group2.optimizer.impl.items.paths.Path;

public class PathPoint extends Vertex implements Point {

    private final int leftOrRight;
    private final Path path;
    private final int isVerticalPath;

    public PathPoint(int id, int xCoordinate, int yCoordinate, int leftOrRight, Path path, int isVerticalPath) {
        super(id, xCoordinate, yCoordinate);
        this.leftOrRight = leftOrRight;
        this.path = path;
        this.isVerticalPath = isVerticalPath;
    }

    public int isLeft() {
        return leftOrRight;
    }

    public int isVerticalPath() {
        return isVerticalPath;
    }

    public Path getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "PathPoint{" +
                "id=" + this.getId() +
                ", x=" + this.getXCoordinate() +
                ", y=" + this.getYCoordinate() +
                ", left?=" + (this.isLeft() == 0) +
                ", path id=" + this.getPath().getId() +
                '}';
    }
}
