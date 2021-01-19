package pl.group2.optimizer.impl.structures.graph;

public class Graph {

    private final int numberOfVertices;
    private final int[][] weights;

    public Graph(int numberOfVertices) {
        this.numberOfVertices = numberOfVertices;
        weights = new int[numberOfVertices][numberOfVertices];
    }

    public int getWeightOfEdge(int from, int to) {
        checkCorrectnessOfOperation(from, to);
        return weights[from][to];
    }

    public void addEdge(int from, int to, int weight) {
        checkCorrectnessOfOperation(from, to);
        weights[from][to] = weight;
        weights[to][from] = weight;
    }

    public int getNumberOfVertices() {
        return numberOfVertices;
    }

    public void setWeightOfEdge(int from, int to, int weight) {
        checkCorrectnessOfOperation(from, to);
        weights[from][to] = weight;
        weights[to][from] = weight;
    }

    private boolean containsVertices(int from, int to) {
        return (from >= numberOfVertices || to >= numberOfVertices);
    }

    private void checkCorrectnessOfOperation(int from, int to) {
        if (containsVertices(from, to)) {
            throw new UnsupportedOperationException("Conajmniej jeden z podanych wierzchołków nie istnieje");
        }
    }
}
