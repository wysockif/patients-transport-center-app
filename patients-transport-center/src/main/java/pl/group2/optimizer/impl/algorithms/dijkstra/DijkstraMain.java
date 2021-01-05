package pl.group2.optimizer.impl.algorithms.dijkstra;

import pl.group2.optimizer.Optimizer;
import pl.group2.optimizer.impl.algorithms.closest.ShortestDistanceChecker;
import pl.group2.optimizer.impl.io.MyException;
import pl.group2.optimizer.impl.items.Vertex;
import pl.group2.optimizer.impl.items.hospitals.Hospital;
import pl.group2.optimizer.impl.items.hospitals.Hospitals;
import pl.group2.optimizer.impl.items.paths.Paths;
import pl.group2.optimizer.impl.items.patients.Patient;
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

        DijkstraAlgorithm dijkstraAlgorithm = new DijkstraAlgorithm();

        int size = patients.size();
        Hospital closest;
        for (int i = 0; i < size; i++) {
            Patient first = patients.getFirst();
            closest = distanceChecker.closestHospital(patients.getNextToHandle(), hospitals);

            Vertex v = closest;
            int n = hospitals.size();
            int m = paths.size();

            Graph graph = new WeightedGraph(m);
        }
    }
}
