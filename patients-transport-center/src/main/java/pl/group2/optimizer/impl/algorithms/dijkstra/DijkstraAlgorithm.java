package pl.group2.optimizer.impl.algorithms.dijkstra;

import pl.group2.optimizer.impl.items.Vertex;
import pl.group2.optimizer.impl.structures.graph.Graph;
import pl.group2.optimizer.impl.structures.graph.WeightedGraph;

import java.util.List;

public class DijkstraAlgorithm {
    public static final int INFINITY = 1_000_000_000;
    public static final int UNDEFINED = -1;
    List<Vertex> Q;
    Graph graph;

    public DijkstraAlgorithm(List<Vertex> q) {
        Q = q;
        graph = new WeightedGraph(Q.size());
    }


    public void Dijkstra() {
        for (Vertex vertex : Q) {
            
        }
    }

}
