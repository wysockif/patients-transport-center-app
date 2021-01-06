package pl.group2.optimizer.impl.structures.graph;

public class WeightedGraph implements Graph {

    private final int numberOfVertices;
    private final int[][] weights;

    public WeightedGraph(int numberOfVertices) {
        this.numberOfVertices = numberOfVertices;
        weights = new int[numberOfVertices][numberOfVertices];
    }

    @Override
    public int getWeightOfEdge(int from, int to) {
        checkCorrectnessOfOperation(from, to);
        return weights[from][to];
    }

    @Override
    public void addEdge(int from, int to, int weight) {
        checkCorrectnessOfOperation(from, to);
        weights[from][to] = weight;
        weights[to][from] = weight;
    }

    @Override
    public boolean containsEdge(int from, int to) {
        return weights[from][to] > 0;
    }

    @Override
    public int getNumberOfVertices() {
        return numberOfVertices;
    }

    @Override
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
