package pl.group2.optimizer;

import pl.group2.optimizer.gui.MyWindow;
import pl.group2.optimizer.impl.io.MyException;
import pl.group2.optimizer.impl.io.TextFileReader;
import pl.group2.optimizer.impl.items.hospitals.Hospitals;
import pl.group2.optimizer.impl.items.paths.Paths;
import pl.group2.optimizer.impl.items.patients.Patients;
import pl.group2.optimizer.impl.items.specialobjects.SpecialObjects;

public class Optimizer {
    public static final double NANOSECONDS_IN_SECOND = 1_000_000_000.0;
    private final String timeFormat;

    private Patients patients;
    private Hospitals hospitals;
    private SpecialObjects specialObjects;
    private Paths paths;

    public Patients getPatients() {
        return patients;
    }

    public Hospitals getHospitals() {
        return hospitals;
    }

    public SpecialObjects getSpecialObjects() {
        return specialObjects;
    }

    public Paths getPaths() {
        return paths;
    }

    public Optimizer() {
        timeFormat = "[ %.4fs ]\n";
    }

    public void checkIfPatientsWereDownloaded() {
        System.out.println("Pobrano " + patients.size() + "pacjentów");
        System.out.println("Pierwszy z nich to: " + patients.getFirst().toString());
    }

    public void checkIfMapWasDownloaded() {
        System.out.println("Pobrano " + hospitals.size() + " szpitali");
        System.out.println("Pobrano " + specialObjects.size() + " specjalnych obiektów");
        System.out.println("Pobrano " + paths.size() + " dróg");
    }

    public void loadPatients(String inputFilePath) throws MyException {
        System.out.print("TRWA ODCZYTYWANIE I WALIDACJA DANYCH PACJENTÓW... ");
        long before = System.nanoTime();

        TextFileReader textFileReader = new TextFileReader();
        textFileReader.readData(inputFilePath, 0);
        patients = textFileReader.getPatients();

        double time = (double) (System.nanoTime() - before) / NANOSECONDS_IN_SECOND;
        System.out.printf(timeFormat, time);
    }

    public void loadMap(String inputFilePath) throws MyException {
        System.out.print("TRWA ODCZYTYWANIE I WALIDACJA DANYCH SZPITALI, S. OBIEKTÓW I DRÓG... ");
        long before = System.nanoTime();

        TextFileReader textFileReader = new TextFileReader();
        textFileReader.readData(inputFilePath, 1);
        hospitals = textFileReader.getHospitals();
        specialObjects = textFileReader.getSpecialObjects();
        paths = textFileReader.getPaths();

        double time = (double) (System.nanoTime() - before) / NANOSECONDS_IN_SECOND;
        System.out.printf(timeFormat, time);
    }

    public void makeWindow() {
        MyWindow window = new MyWindow(this);
    }
}
