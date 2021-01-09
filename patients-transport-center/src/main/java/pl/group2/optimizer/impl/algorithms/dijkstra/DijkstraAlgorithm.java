package pl.group2.optimizer.impl.algorithms.dijkstra;

import pl.group2.optimizer.impl.items.Vertex;
import pl.group2.optimizer.impl.items.hospitals.Hospitals;
import pl.group2.optimizer.impl.items.paths.Path;
import pl.group2.optimizer.impl.items.paths.Paths;
import pl.group2.optimizer.impl.items.patients.Patients;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class DijkstraAlgorithm {
    public static final int INFINITY = 1_000_000_000;
    public static final int UNDEFINED = -1;

    Hospitals hospitals;
    Patients patients;
    Paths paths;
    Graph graph;

    public DijkstraAlgorithm(Hospitals hospitals, Patients patients, Paths paths) {
        this.hospitals = hospitals;
        this.patients = patients;
        this.paths = paths;
        graph = makeGraph();
    }

    public List<Vertex> neighbours(int id) {
        List<Vertex> neighbours = new LinkedList<>();
        for (Path path : paths.getList()) {
            if (path.getFrom().getId() == id) {
                neighbours.add(path.getTo());
            } else if (path.getTo().getId() == id) {
                neighbours.add(path.getFrom());
            }
        }
        return neighbours;
    }

    private Graph makeGraph() {
        Graph graph = new Graph(hospitals.getMaxId() + 1);
        for (Path path : paths.getList()) {
            graph.addEdge(path.getFrom().getId(), path.getTo().getId(), (int) path.getDistance());
        }
        return graph;
    }

    int predecessors[];
    int distances[];
    PriorityQueue<Predecessor> q;

    public Vertex shortestPathFromSelectedVertexToHospital(Vertex start) {

        System.out.println("Sąsiedzi poszukwianego wierzchołka to " + neighbours(start.getId()));

        predecessors = new int[graph.getNumberOfVertices()];
        distances = new int[graph.getNumberOfVertices()];

        q = new PriorityQueue<>(new Comparator<Predecessor>() {
            @Override
            public int compare(Predecessor o1, Predecessor o2) {
                if (o1.distance - o2.distance < 0) {
                    return -1;
                } else if (o1.distance - o2.distance > 0) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        for (int i = 0; i < graph.getNumberOfVertices(); i++) {
            if (i == start.getId()) {
                distances[start.getId()] = 0;
                predecessors[start.getId()] = start.getId();
            } else {
                distances[i] = INFINITY;
                predecessors[i] = UNDEFINED;
            }

            q.add(new Predecessor(predecessors[i], distances[i]));
            System.out.println("Dodaje do kolejki id równe " + predecessors[i]);
            System.out.println("Dodaje do kolejki odległość równą " + distances[i]);
        }

        Set<Integer> w = new HashSet<>();

        System.out.println();

        while (!q.isEmpty()) {
            Predecessor u = q.poll();
            System.out.println(q.size());

            System.out.println("Wyjmuje z kolejki odległość równą " + u.getDistance());
            System.out.println("Wyjmuje z kolejki id równe " + u.getId());

            w.add(u.getId());
            System.out.println("Odwiedzony wierzchołek to: " + u.getId());
            for (Vertex v : neighbours(u.getId())) {
                System.out.println("Sąsiad to " + v.getId());
                relax(u.getId(), v.getId());
            }
        }

        System.out.println("Dla szpitala najkrótsza " + start + " odległość biegnie przez: ");
        System.out.println(Arrays.toString(predecessors));

        return null;
    }

    public void decreasePriority(int id, int distance) {

    }

    public void relax(int u, int v) {
        System.out.println("Dla sąsiada o id = " + v);
        System.out.println("Odległość u wynosi " + distances[u]);

        int alt = distances[u] + graph.getWeightOfEdge(u, v);
        System.out.println("Odległość v + graph wynosi " + alt);
        if (alt < distances[v]) {
            distances[v] = alt;
            predecessors[v] = u;
            decreasePriority(v, u);
        }
    }
}
