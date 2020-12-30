package pl.group2.optimizer;

import pl.group2.optimizer.gui.Window;
import pl.group2.optimizer.gui.components.Plan;
import pl.group2.optimizer.impl.algorithms.graham.Graham;
import pl.group2.optimizer.impl.io.MyException;
import pl.group2.optimizer.impl.io.TextFileReader;
import pl.group2.optimizer.impl.items.area.HandledArea;
import pl.group2.optimizer.impl.items.area.Point;
import pl.group2.optimizer.impl.items.hospitals.Hospitals;
import pl.group2.optimizer.impl.items.paths.Paths;
import pl.group2.optimizer.impl.items.patients.Patients;
import pl.group2.optimizer.impl.items.specialobjects.SpecialObjects;

import java.util.List;

public class Optimizer {
    public static final double NANOSECONDS_IN_SECOND = 1_000_000_000.0;
    private final String timeFormat;

    private Patients patients;
    private Hospitals hospitals;
    private SpecialObjects specialObjects;
    private Paths paths;
    private Window window;

    public Optimizer() {
        timeFormat = "[ %.4fs ]\n";
    }

    public void checkIfPatientsWereDownloaded() {
        System.out.println("Pobrano pacjentów");
        System.out.println("Ich ilość to: " + patients.size());
        System.out.println("Pierwszy z nich to: " + patients.getFirst().toString());
    }

    public void checkIfMapWasDownloaded() {
        System.out.println("Pobrano szpitale:");
        System.out.println("Ostatni z nich to: " + hospitals.getHospitalByIndex(4).getName());

        System.out.println("Pobrano special objects:");
        System.out.println("Ostatni z nich to: " + specialObjects.getSpecialObjectById(3).getName());

        System.out.println("Pobrano paths:");
        System.out.println("Ich ilość to: " + paths.size());
        System.out.println("x From drogi o indexie 5 to: " + paths.get(5).getFrom().getXCoordinate());
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

    public void createWindow() {
        Optimizer optimizer = new Optimizer();
        window = new Window(optimizer);
    }

    public HandledArea prepareData() {
        Graham grahamAlgorithm = new Graham();
        grahamAlgorithm.loadPoints(hospitals, specialObjects);
        List<Point> points = grahamAlgorithm.findConvexHull();

        HandledArea area = new HandledArea();
        area.createArea(points);
        return area;
    }

    public void run() {
        HandledArea area = prepareData();
        double scalaX = (double) (Plan.WIDTH - 150) / area.getMaxWidth();
        double scalaY = (double) (Plan.HEIGHT - 150) / area.getMaxHeight();
        System.out.println(scalaX + " " + scalaY);

    }

    public Patients getPatients() {
        return patients;
    }

    public Hospitals getHospitals() {
        return hospitals;
    }

    public SpecialObjects getSpecialObjects() {
        return specialObjects;
    }
}
