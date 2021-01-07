package pl.group2.optimizer.impl.algorithms.dijkstra;

import pl.group2.optimizer.impl.items.Vertex;
import pl.group2.optimizer.impl.items.hospitals.Hospital;
import pl.group2.optimizer.impl.items.hospitals.Hospitals;
import pl.group2.optimizer.impl.items.paths.Path;
import pl.group2.optimizer.impl.items.paths.Paths;
import pl.group2.optimizer.impl.items.patients.Patients;
import pl.group2.optimizer.impl.structures.graph.Graph;
import pl.group2.optimizer.impl.structures.graph.WeightedGraph;

import java.util.LinkedList;
import java.util.List;

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

    public List<Hospital> neighboursWithMoreThanZeroBeds(Hospital hospital) {
        List<Hospital> neighbours = new LinkedList<>();
        int id = hospital.getId();
        for (Path path : paths.getList()) {
            if (path.getFrom().getId() == id) {
                Hospital neighbour = (Hospital) path.getTo();

                if (neighbour.getNumberOfAvailableBeds() != 0) {
                    neighbours.add(neighbour);
                }

            } else if (path.getTo().getId() == id) {
                Hospital neighbour = (Hospital) path.getFrom();

                if (neighbour.getNumberOfAvailableBeds() != 0) {
                    neighbours.add(neighbour);
                }
            }
        }
//        System.out.print("Sąsiadami szpitala " + hospital);
//        System.out.println(" są szpitale " + neighbours);
        return neighbours;
    }

    public Graph makeGraph() {
        Graph graph = new WeightedGraph(hospitals.getMaxId() + 1);
        for (Path path : paths.getList()) {
            graph.addEdge(path.getFrom().getId(), path.getTo().getId(), path.getDistance());

//            System.out.print("Krawędź grafu, która łączy szpitale o id: ");
//            System.out.print(path.getFrom().getId() + " " + path.getTo().getId());
//            System.out.println(" ma odległość " + graph.getWeightOfEdge(path.getFrom().getId(), path.getTo().getId()));
        }
        return graph;
    }

    private Vertex findClosestNeighbour(Vertex start) {
        List<Hospital> neighbours = neighboursWithMoreThanZeroBeds((Hospital) start);

        List<Integer> distances = new LinkedList<>();

        if (neighbours.size() == 0) {
            return start;
        }

        int min = graph.getWeightOfEdge(start.getId(), neighbours.get(0).getId());
        int closestNeighbourId = neighbours.get(0).getId();

        for (int i = 1; i < neighbours.size(); i++) {
            int distance = graph.getWeightOfEdge(start.getId(), neighbours.get(i).getId());
            if (distance < min) {
                min = distance;
                closestNeighbourId = neighbours.get(i).getId();
            }
        }

//        System.out.print("SĄSIAD, który jest najbliżej to: ");
//        System.out.println(hospitals.getHospitalById(closestNeighbourId));
//        System.out.println("z odległością " + graph.getWeightOfEdge(start.getId(), closestNeighbourId));
        return hospitals.getHospitalById(closestNeighbourId);
    }

    public Vertex shortestPathFromSelectedVertexToHospital(Vertex start) {
//        int[] distances = new int[hospitals.getMaxId()+1];
//        distances[start.getId()] = 0;

        return findClosestNeighbour(start);
    }

}
