package pl.group2.optimizer.impl.algorithms.closest;

import pl.group2.optimizer.impl.items.hospitals.Hospital;
import pl.group2.optimizer.impl.items.hospitals.Hospitals;
import pl.group2.optimizer.impl.items.patients.Patient;

public class ShortestDistanceChecker {

    private int minIndex(double[] distances) {
        double min = distances[0];
        int minIndex = 0;

        for (int i = 1; i < distances.length; i++) {
            if (distances[i] < min) {
                min = distances[i];
                minIndex = i;
            }
        }

        return minIndex;
    }

    public Hospital closestHospital(Patient patient, Hospitals hospitals) {
        Hospital hospital;
        double[] distances = new double[hospitals.size()];
        for (int i = 0; i < hospitals.size(); i++) {
            hospital = hospitals.getHospitalByIndex(i);
            double dx = Math.pow((hospital.getXCoordinate() - patient.getXCoordinate()), 2);
            double dy = Math.pow((hospital.getYCoordinate() - patient.getYCoordinate()), 2);
            distances[i] = dx + dy;
        }

        Hospital closest = hospitals.getHospitalByIndex(minIndex(distances));

        return closest;
    }
}
