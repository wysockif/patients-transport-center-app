package pl.group2.optimizer.impl.structures.graph;

public interface Graph {

    int getWeightOfEdge(int from, int to);

    void addEdge(int from, int to, int weight);

    boolean containsEdge(int from, int to);

    int getNumberOfVertices();

    void setWeightOfEdge(int from, int to, int weight);
}
