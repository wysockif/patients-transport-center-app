package pl.group2.optimizer.impl.algorithms.dijkstra;

import pl.group2.optimizer.impl.items.Vertex;
import pl.group2.optimizer.impl.items.hospitals.Hospital;
import pl.group2.optimizer.impl.items.hospitals.Hospitals;
import pl.group2.optimizer.impl.items.paths.Path;
import pl.group2.optimizer.impl.items.paths.Paths;
import pl.group2.optimizer.impl.structures.graph.Graph;

import java.util.LinkedList;
import java.util.List;

public class DijkstraAlgorithm {
    public static final int INFINITY = 1_000_000_000;
    public static final int UNDEFINED = -1;

    public List<Vertex> neighbours(Hospital hospital, Paths paths) {
        List<Vertex> neighbours = new LinkedList<>();
        int id = hospital.getId();
        for (Path path : paths.getList()) {
            if (path.getFrom().getId() == id) {
                neighbours.add(path.getTo());
            } else if (path.getTo().getId() == id) {
                neighbours.add(path.getFrom());
            }
        }
        System.out.print("Sąsiadami szpitala " + hospital);
        System.out.println(" są szpitale " + neighbours);
        return neighbours;
    }

    public Vertex shortestPathFromSelectedVertexToHospital(Vertex start, Paths paths, Graph graph, Hospitals hospitals) {
        List<Vertex> neighbours = neighbours((Hospital) start, paths);

        List<Integer> distances = new LinkedList<>();

        int min = graph.getWeightOfEdge(start.getId(), neighbours.get(0).getId());
        int closestNeighbourId = neighbours.get(0).getId();

        for (int i = 1; i < neighbours.size(); i++) {
            int distance = graph.getWeightOfEdge(start.getId(), neighbours.get(i).getId());
            if (distance < min) {
                min = distance;
                closestNeighbourId = neighbours.get(i).getId();
            }
        }

        System.out.print("SĄSIAD, który jest najbliżej to: ");
        System.out.println(hospitals.getHospitalById(closestNeighbourId));
        System.out.println("z odległością " + graph.getWeightOfEdge(start.getId(), closestNeighbourId));
        return hospitals.getHospitalByIndex(closestNeighbourId);
    }

}
