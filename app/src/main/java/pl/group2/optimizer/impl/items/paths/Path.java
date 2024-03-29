package pl.group2.optimizer.impl.items.paths;

import pl.group2.optimizer.impl.items.Vertex;

public class Path {
    private final int id;
    private final Vertex from;
    private final Vertex to;
    private final int distance;

    public Path(int id, Vertex from, Vertex to, int distance) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    public int getId() {return id; }

    public int getDistance() {
        return distance;
    }

    public Vertex getFrom() {
        return from;
    }

    public Vertex getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "Path{" +
                "id=" + id +
                ", from=" + from.toString() +
                ", to=" + to.toString() +
                ", distance=" + distance +
                '}';
    }
}
