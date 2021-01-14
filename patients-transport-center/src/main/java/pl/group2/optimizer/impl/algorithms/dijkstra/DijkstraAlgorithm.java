package pl.group2.optimizer.impl.algorithms.dijkstra;

import pl.group2.optimizer.impl.items.Vertex;
import pl.group2.optimizer.impl.items.hospitals.Hospital;
import pl.group2.optimizer.impl.items.hospitals.Hospitals;
import pl.group2.optimizer.impl.items.paths.Path;
import pl.group2.optimizer.impl.items.paths.Paths;
import pl.group2.optimizer.impl.items.patients.Patients;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class DijkstraAlgorithm {
    public static final int INFINITY = 1_000_000_000;
    public static final int UNDEFINED = -1;

    private Hospitals hospitals;
    private Patients patients;
    private Paths paths;
    private Graph graph;

    private Hospital newHospital;

    private int predecessors[];
    private int distances[];
    private MyPQ q;

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
            graph.addEdge(path.getFrom().getId(), path.getTo().getId(), path.getDistance());
        }
        return graph;
    }

    private void initiatePredecessorsDistancesAndPriorityQueue(Vertex start) {
        predecessors = new int[graph.getNumberOfVertices()];
        distances = new int[graph.getNumberOfVertices()];
        q = new MyPQ();

        for (int i = 0; i < graph.getNumberOfVertices(); i++) {
            if (i == start.getId()) {
                distances[start.getId()] = 0;
                predecessors[start.getId()] = start.getId();
            } else {
                distances[i] = INFINITY;
                predecessors[i] = UNDEFINED;
            }

            q.add(new Predecessor(predecessors[i], distances[i]));
        }
    }

    private int findMinimumDistance() {
        int min = INFINITY;
        int index = 0;
        for (int i = 0; i < distances.length; i++) {
            if (distances[i] < min && distances[i] != 0 && hospitals.getHospitalById(i).getNumberOfAvailableBeds() != 0) {
                min = distances[i];
                index = i;
            }
        }
        return index;
    }

    private List<Vertex> findSeriesOfVerticesToVisit(int index, Vertex start) {
        List<Vertex> pointsToVisit = new ArrayList<>();
        List<Vertex> pointsToVisitCopy = new ArrayList<>();

        int predecessorIndex = index;

        while (predecessorIndex != start.getId()) {
            for (int i = 0; i < predecessors.length; i++) {
                if (predecessorIndex == i) {
                    System.out.println("Przez wierzchołek o id = " + predecessors[i]);
                    pointsToVisitCopy.add(hospitals.getHospitalById(predecessorIndex));
                    predecessorIndex = predecessors[i];
                    break;
                }
            }
        }

        for (int i = pointsToVisitCopy.size() - 1; i >= 0; i--) {
            pointsToVisit.add(pointsToVisitCopy.get(i));
        }

        for (Vertex vertex : pointsToVisit) {
            System.out.println(vertex.getXCoordinate() + " " + vertex.getYCoordinate());
        }

        return pointsToVisit;
    }

    public Hospital getNewHospital() {
        return newHospital;
    }

    public void decreasePriority(int id, int distance) {
        q.setNewPriority(id, distance);
    }

    public List<Vertex> shortestPathFromSelectedVertexToHospital(Vertex start) {
        initiatePredecessorsDistancesAndPriorityQueue(start);

        Set<Integer> w = new HashSet<>();
        while (!q.isEmpty()) {
            Predecessor u = q.remove();
            w.add(u.getId());
            for (Vertex v : neighbours(u.getId())) {
                if (!w.contains(v.getId())) {
                    relax(u.getId(), v.getId());
                }
            }
        }

        int index = findMinimumDistance();

        System.out.println("Najkrótsza droga jest do szpitala o id " + index);
        System.out.println(Arrays.toString(predecessors));
        System.out.println(Arrays.toString(distances));

        newHospital = hospitals.getHospitalById(index);

        return findSeriesOfVerticesToVisit(index, start);
    }
    
    public void relax(int u, int v) {
        int alt = distances[u] + graph.getWeightOfEdge(u, v);
        if (alt < distances[v]) {
            distances[v] = alt;
            predecessors[v] = u;
            decreasePriority(v, alt);
        }
    }
}
