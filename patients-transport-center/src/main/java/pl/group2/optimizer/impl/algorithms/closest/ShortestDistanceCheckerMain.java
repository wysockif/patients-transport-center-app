package pl.group2.optimizer.impl.algorithms.closest;

import pl.group2.optimizer.Optimizer;
import pl.group2.optimizer.impl.io.MyException;
import pl.group2.optimizer.impl.items.hospitals.Hospitals;
import pl.group2.optimizer.impl.items.patients.Patients;

public class ShortestDistanceCheckerMain {
    public static void main(String[] args) throws MyException {
        ShortestDistanceChecker distanceChecker = new ShortestDistanceChecker();

        Optimizer optimizer = new Optimizer();
        optimizer.loadMap("exemplaryData/correct/map10.txt");
        optimizer.loadPatients("exemplaryData/correct/patients1.txt");

        Hospitals hospitals = optimizer.getHospitals();
        Patients patients = optimizer.getPatients();

        long before = System.nanoTime();
        String timeFormat = "[ %.4fs ]\n";

        double NANOSECONDS_IN_SECOND = 1_000_000_000.0;

        distanceChecker.closestHospital(patients.getFirst(), hospitals);

        double time = (double) (System.nanoTime() - before) / NANOSECONDS_IN_SECOND;
        System.out.print("SZUKANIE NAJBLIŻSZEGO SZPITALA DLA DANEGO PACJENTA... ");
        System.out.printf(timeFormat, time);
    }
}
