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
    MyPQ q;

    public List<Vertex> shortestPathFromSelectedVertexToHospital(Vertex start) {

//        System.out.println("Sąsiedzi poszukwianego wierzchołka to " + neighbours(start.getId()));

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
//            System.out.println("Dodaje do kolejki id równe " + predecessors[i]);
//            System.out.println("Dodaje do kolejki odległość równą " + distances[i]);
        }

        Set<Integer> w = new HashSet<>();

        System.out.println();

        while (!q.isEmpty()) {
//            q.printQuery();
            Predecessor u = q.remove();

//            System.out.println("Wyjmuje z kolejki odległość równą " + u.getDistance());
//            System.out.println("Wyjmuje z kolejki id równe " + u.getId());

            w.add(u.getId());
//            System.out.println("Odwiedzony wierzchołek to: " + u.getId());
            for (Vertex v : neighbours(u.getId())) {
                if (!w.contains(v.getId())) {
//                    System.out.println("Sąsiad to " + v.getId());
                    relax(u.getId(), v.getId());
                }

            }
        }
//
//        System.out.println();
//        System.out.println("--------------------------------");
//        System.out.println("Od szpitala " + start + " najkrótsza  odległość biegnie przez: ");
//        System.out.println(Arrays.toString(predecessors));
//        System.out.println(Arrays.toString(distances));
//        System.out.println(Arrays.toString(w.toArray()));
//        System.out.println("-------------------------------");
//        System.out.println();

        int min = INFINITY;
        int index = 0;
        for (int i = 0; i < distances.length; i++) {
            if (distances[i] < min && distances[i] != 0 && hospitals.getHospitalById(i).getNumberOfAvailableBeds() != 0) {
                min = distances[i];
                index = i;
            }
        }

        System.out.println();
        System.out.println("Najkrótsza droga jest do szpitala o id: " + index);
        System.out.println(Arrays.toString(predecessors));

        newHospital = hospitals.getHospitalById(index);

//        System.out.println("I wiedzie ona przez " + predecessors[index]
//                + " (jeżeli id = id początku to znaczy, że jest połączenie bezpośrednie)");
        System.out.println();

        List<Vertex> pointsToVisit = new ArrayList<>();

        // ...

        pointsToVisit.add(hospitals.getHospitalById(index));

        return pointsToVisit;
    }

    Hospital newHospital;

    public Hospital getNewHospital() {
        return newHospital;
    }

    public void decreasePriority(int id, int distance) {
        q.setNewPriority(id, distance);
    }

    public void relax(int u, int v) {
//        System.out.print("Dla sąsiada o id = " + v);
//        System.out.print(" Odległość v wynosi " + distances[v]);

        int alt = distances[u] + graph.getWeightOfEdge(u, v);

//        System.out.println(" Odległość alternatywna " + alt);
        if (alt < distances[v]) {
//            System.out.println("Odległość alternatywna jest mniejsza");
            distances[v] = alt;
            predecessors[v] = u;
            decreasePriority(v, alt);
        }
    }

//    public void relax(int u, int v) {
//        System.out.print("Dla sąsiada o id = " + v);
//        System.out.print(" Odległość v wynosi " + distances[v]);
//
//        int alt = distances[v] + graph.getWeightOfEdge(u, v);
//
//        System.out.println(" Odległość alternatywna " + alt);
//        if (distances[u] > alt) {
//            System.out.println("Odległość alternatywna jest mniejsza");
//            distances[u] = alt;
//            predecessors[u] = v;
//            decreasePriority(u, alt);
//        }
//    }
}
