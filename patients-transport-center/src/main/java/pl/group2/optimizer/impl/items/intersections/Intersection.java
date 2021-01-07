package pl.group2.optimizer.impl.items.intersections;

import pl.group2.optimizer.impl.items.Vertex;
import pl.group2.optimizer.impl.items.area.Point;

public class Intersection extends Vertex implements Point {

    public Intersection(int id, int xCoordinate, int yCoordinate) {
        super(id, xCoordinate, yCoordinate);
    }
}
