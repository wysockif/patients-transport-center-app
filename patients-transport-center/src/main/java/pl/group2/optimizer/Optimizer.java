package pl.group2.optimizer;

import pl.group2.optimizer.impl.io.TextFileReader;
import pl.group2.optimizer.impl.items.hospitals.Hospital;
import pl.group2.optimizer.impl.items.hospitals.Hospitals;
import pl.group2.optimizer.impl.items.paths.Path;
import pl.group2.optimizer.impl.items.patients.Patients;
import pl.group2.optimizer.impl.items.specialobjects.SpecialObject;

public class Optimizer {
    private static String inputFilePath;
    public static final double NANOSECONDS_IN_SECOND = 1_000_000_000.0;
    private Patients patients;
    private final String timeFormat;

    public Optimizer() {
        timeFormat = "[ %.4fs ]\n";
    }

    public static void main(String[] args) {
        Hospital hospital1 = new Hospital(1, "Szpital Wojewódzki nr 997", 10, 10, 1000, 100);
        Hospital hospital2 = new Hospital(2, "Krakowski Szpital Kliniczny", 100, 120, 999, 99);
        Hospital hospital3 = new Hospital(3, "Pierwszy Szpital im. Prezesa RP", 120, 130, 99, 0);
        Hospital hospital4 = new Hospital(4, "Drugi Szpital im. Naczelnika RP", 10, 140, 70, 1);
        Hospital hospital5 = new Hospital(5, "Trzeci Szpital im. Króla RP", 140, 10, 996, 0);

        Hospitals hospitals = new Hospitals();
        hospitals.add(hospital1);
        hospitals.add(hospital2);
        hospitals.add(hospital3);
        hospitals.add(hospital4);
        hospitals.add(hospital5);

        SpecialObject specialObject1 = new SpecialObject(1, "Pomnik Wikipedii", -1, 50);
        SpecialObject specialObject2 = new SpecialObject(2, "Pomnik Fryderyka Chopina", 110, 55);
        SpecialObject specialObject3 = new SpecialObject(3, "Pomnik Anonimowego Przechodnia", 40, 70);

        Path path1 = new Path(1, hospital1, hospital2, 700);
        Path path2 = new Path(2, hospital1, hospital4, 550);
        Path path3 = new Path(3, hospital1, hospital5, 800);
        Path path4 = new Path(4, hospital2, hospital3, 300);
        Path path5 = new Path(5, hospital2, hospital4, 550);
        Path path6 = new Path(6, hospital3, hospital5, 600);
        Path path7 = new Path(7, hospital4, hospital5, 750);

        Optimizer optimizer = new Optimizer();
        optimizer.loadData();
        System.out.println("Pobrano pacjentów");
        System.out.println("Ich ilość to: " + optimizer.patients.size());
        System.out.println("Pierwszy z nich to: " + optimizer.patients.getFirst().toString());

    }

    private void loadData() {
        inputFilePath = "exemplaryData/correct/patients1.txt";

        System.out.print("TRWA ODCZYTYWANIE I WALIDACJA DANYCH... ");
        long before = System.nanoTime();
        TextFileReader textFileReader = new TextFileReader(inputFilePath);
        patients = textFileReader.getPatients();
        double time = (double) (System.nanoTime() - before) / NANOSECONDS_IN_SECOND;
        System.out.printf(timeFormat, time);
    }
}
