package pl.group2.optimizer.impl.items.specialobjects;

public class SpecialObject {
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

    public int getXCoordinate() {
        return xCoordinate;
    }

    public int getYCoordinate() {
        return yCoordinate;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getxCoordinate() {
        return xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }
}
