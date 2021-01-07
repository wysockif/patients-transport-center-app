package pl.group2.optimizer.impl.algorithms.dijkstra;

import pl.group2.optimizer.Optimizer;
import pl.group2.optimizer.impl.algorithms.closest.ShortestDistanceChecker;
import pl.group2.optimizer.impl.io.MyException;
import pl.group2.optimizer.impl.items.hospitals.Hospital;
import pl.group2.optimizer.impl.items.hospitals.Hospitals;
import pl.group2.optimizer.impl.items.paths.Paths;
import pl.group2.optimizer.impl.items.patients.Patients;


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

        DijkstraAlgorithm dijkstraAlgorithm = new DijkstraAlgorithm(hospitals, patients, paths);

        Hospital closest = distanceChecker.closestHospital(patients.getFirst(), hospitals);
        Hospital next = (Hospital) dijkstraAlgorithm.shortestPathFromSelectedVertexToHospital(closest);
        System.out.println("Jeżeli ten szpital jest zajęty do ten: ");
        dijkstraAlgorithm.shortestPathFromSelectedVertexToHospital(next);


    }
}
