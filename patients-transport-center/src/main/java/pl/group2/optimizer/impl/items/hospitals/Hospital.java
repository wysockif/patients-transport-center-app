package pl.group2.optimizer.impl.items.hospitals;

import pl.group2.optimizer.impl.items.Vertex;

public class Hospital extends Vertex {
    private final String name;
    private final int numberOfBeds;
    private int numberOfAvailableBeds;

    public Hospital(int id,  String name, int xCoordinate, int yCoordinate, int numberOfBeds, int numberOfAvailableBeds) {
        super(id, xCoordinate, yCoordinate);
        this.name = name;
        this.numberOfBeds = numberOfBeds;
        this.numberOfAvailableBeds = numberOfAvailableBeds;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public int getNumberOfAvailableBeds() {
        return numberOfAvailableBeds;
    }

    public void setNumberOfAvailableBeds(int numberOfAvailableBeds) {
        this.numberOfAvailableBeds = numberOfAvailableBeds;
    }
}
