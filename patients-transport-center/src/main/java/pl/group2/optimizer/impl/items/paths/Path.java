package pl.group2.optimizer.impl.items.paths;

public class Path {
    private final int id;
    private final int from;
    private final int to;
    private final double distance;

    public Path(int id, int from, int to, double distance) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.distance = distance;
    }


}
