package pl.group2.optimizer.impl.algorithms.dijkstra;

public class Predecessor {
    private int id;
    private int distance;

    public int getId() {
        return id;
    }

    public int getDistance() {
        return distance;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Predecessor(int id, int distance) {
        this.id = id;
        this.distance = distance;
    }
}
