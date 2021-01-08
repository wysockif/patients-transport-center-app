package pl.group2.optimizer.impl.items.paths;

import pl.group2.optimizer.impl.items.Vertex;

public class Path {
    private final int id;
    private final Vertex from;
    private final Vertex to;
    private final double distance;

    public Path(int id, Vertex from, Vertex to, double distance) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    public int getId() {return id; }

    public double getDistance() {
        return distance;
    }

    public Vertex getFrom() {
        return from;
    }

    public Vertex getTo() {
        return to;
    }
}
