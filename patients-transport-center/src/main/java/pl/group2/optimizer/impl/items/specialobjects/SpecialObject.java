package pl.group2.optimizer.impl.items.specialobjects;

import pl.group2.optimizer.impl.items.Point;

public class SpecialObject implements Point {
    private int id;
    private String name;
    private final int xCoordinate;
    private final int yCoordinate;

    public SpecialObject(int id, String name, int xCoordinate, int yCoordinate) {
        this.id = id;
        this.name = name;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public int getXCoordinate() {
        return xCoordinate;
    }

    @Override
    public int getYCoordinate() {
        return yCoordinate;
    }
}
