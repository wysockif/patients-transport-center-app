package pl.group2.optimizer.impl.algorithms.dijkstra;

import pl.group2.optimizer.Optimizer;
import pl.group2.optimizer.impl.algorithms.closest.ShortestDistanceChecker;
import pl.group2.optimizer.impl.io.MyException;
import pl.group2.optimizer.impl.items.hospitals.Hospital;
import pl.group2.optimizer.impl.items.hospitals.Hospitals;
import pl.group2.optimizer.impl.items.paths.Path;
import pl.group2.optimizer.impl.items.paths.Paths;
import pl.group2.optimizer.impl.items.patients.Patients;
import pl.group2.optimizer.impl.structures.graph.Graph;
import pl.group2.optimizer.impl.structures.graph.WeightedGraph;


public class DijkstraMain {
    public static void main(String[] args) throws MyException {

        Optimizer optimizer = new Optimizer();
        optimizer.createWindow();
        optimizer.loadMap("exemplaryData/correct/map1.txt");
        optimizer.loadPatients("exemplaryData/correct/patients1.txt");

        Hospitals hospitals = optimizer.getHospitals();
        Patients patients = optimizer.getPatients();
        Paths paths = optimizer.getPaths();

        ShortestDistanceChecker distanceChecker = new ShortestDistanceChecker();
        Graph graph = new WeightedGraph(hospitals.getMaxId() + 1);
        for (Path path : paths.getList()) {
            graph.addEdge(path.getFrom().getId(), path.getTo().getId(), path.getDistance());

            System.out.print("Krawędź grafu, która łączy szpitale o id: ");
            System.out.print(path.getFrom().getId() + " " + path.getTo().getId());
            System.out.println(" ma odległość " + graph.getWeightOfEdge(path.getFrom().getId(), path.getTo().getId()));
        }

        DijkstraAlgorithm dijkstraAlgorithm = new DijkstraAlgorithm();
        Hospital closest = distanceChecker.closestHospital(patients.getFirst(), hospitals);

        Hospital next = (Hospital) dijkstraAlgorithm.shortestPathFromSelectedVertexToHospital(closest, paths, graph, hospitals);
        System.out.println("Jeżeli ten szpital jest zajęty do ten: ");
        dijkstraAlgorithm.shortestPathFromSelectedVertexToHospital(next, paths, graph, hospitals);


    }
}
