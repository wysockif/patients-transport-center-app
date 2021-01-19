package pl.group2.optimizer.impl.algorithms.dijkstra;

import pl.group2.optimizer.impl.io.ErrorHandler;
import pl.group2.optimizer.impl.io.MyException;
import pl.group2.optimizer.impl.items.Vertex;
import pl.group2.optimizer.impl.items.hospitals.Hospital;
import pl.group2.optimizer.impl.items.hospitals.Hospitals;
import pl.group2.optimizer.impl.items.intersections.Intersections;
import pl.group2.optimizer.impl.items.paths.Path;
import pl.group2.optimizer.impl.items.paths.Paths;
import pl.group2.optimizer.impl.structures.graph.DijkstraPriorityQueue;
import pl.group2.optimizer.impl.structures.graph.Graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class DijkstraAlgorithm {
    public static final int INFINITY = 1_000_000_000;
    public static final int UNDEFINED = -1;

    private Hospitals hospitals;
    private Paths paths;
    private Intersections intersections;
    private Graph graph;

    private Hospital newHospital;

    private int predecessors[];
    private int distances[];
    private DijkstraPriorityQueue q;

    public DijkstraAlgorithm(Hospitals hospitals, Paths paths, Intersections intersections) {
        this.hospitals = hospitals;
        this.paths = paths;
        this.intersections = intersections;
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
        Graph graph = new Graph(hospitals.getMaxId() + 1 + intersections.getList().size());
        for (Path path : paths.getList()) {
            graph.addEdge(path.getFrom().getId(), path.getTo().getId(), path.getDistance());
        }
        return graph;
    }

    private void initiatePredecessorsDistancesAndPriorityQueue(Vertex start) {
        predecessors = new int[graph.getNumberOfVertices()];
        distances = new int[graph.getNumberOfVertices()];
        q = new DijkstraPriorityQueue();

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

    private int findMinimumDistance() throws MyException {
        int amountOfNotFreeBeds = 0;
        int min = INFINITY;
        int index = 0;
        for (int i = 0; i <= hospitals.getMaxId(); i++) {
            if (distances[i] < min && distances[i] != 0 && hospitals.getHospitalById(i).getNumberOfAvailableBeds() != 0) {
                min = distances[i];
                index = i;
            } else {
                amountOfNotFreeBeds++;
            }
        }
        if (amountOfNotFreeBeds > hospitals.getMaxId()) {
            ErrorHandler.handleError(ErrorHandler.NO_HOSPITALS_AVAILABLE, "System został przepełniony. " +
                    "Brak wolnych łóżek w szpitalach");
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
                    if (i >= hospitals.getMaxId() + 1) {
                        pointsToVisitCopy.add(intersections.getList().get(predecessorIndex - hospitals.getMaxId() - 1));
                    } else {
                        pointsToVisitCopy.add(hospitals.getHospitalById(predecessorIndex));
                    }
                    predecessorIndex = predecessors[i];
                    break;
                }
            }
        }

        for (int i = pointsToVisitCopy.size() - 1; i >= 0; i--) {
            pointsToVisit.add(pointsToVisitCopy.get(i));
        }

        return pointsToVisit;
    }

    public Hospital getNewHospital() {
        return newHospital;
    }

    public void decreasePriority(int id, int distance) {
        q.setNewPriority(id, distance);
    }

    public List<Vertex> shortestPathFromSelectedVertexToHospital(Vertex start) throws MyException {
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