package pl.group2.optimizer.impl.algorithms.dijkstra;

public class Predecessor {
    int id;
    int distance;

    public int getId() {
        return id;
    }

    public int getDistance() {
        return distance;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Predecessor(int id, int distance) {
        this.id = id;
        this.distance = distance;
    }
}
