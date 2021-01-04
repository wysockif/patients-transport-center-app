package pl.group2.optimizer;

import pl.group2.optimizer.gui.Window;
import pl.group2.optimizer.gui.components.Plan;
import pl.group2.optimizer.impl.algorithms.closest.ShortestDistanceChecker;
import pl.group2.optimizer.impl.algorithms.graham.Graham;
import pl.group2.optimizer.impl.io.MyException;
import pl.group2.optimizer.impl.io.TextFileReader;
import pl.group2.optimizer.impl.items.area.HandledArea;
import pl.group2.optimizer.impl.items.area.Point;
import pl.group2.optimizer.impl.items.hospitals.Hospitals;
import pl.group2.optimizer.impl.items.paths.Paths;
import pl.group2.optimizer.impl.items.patients.Patients;
import pl.group2.optimizer.impl.items.specialobjects.SpecialObjects;
import pl.group2.optimizer.impl.structures.queues.QueueFIFO;

import java.util.List;

import static pl.group2.optimizer.gui.components.Plan.HEIGHT;
import static pl.group2.optimizer.gui.components.Plan.MARGIN;
import static pl.group2.optimizer.gui.components.Plan.PADDING;
import static pl.group2.optimizer.gui.components.Plan.WIDTH;

public class Optimizer {
    public static final double NANOSECONDS_IN_SECOND = 1_000_000_000.0;

    private final String timeFormat;

    private Patients patients;
    private Hospitals hospitals;
    private SpecialObjects specialObjects;
    private Paths paths;
    private Plan plan;
    private HandledArea area;

    public Optimizer() {
        timeFormat = "[ %.4fs ]\n";
    }

    public String messageAboutDownloadedPatients() {
        return "Pobrano " + patients.size() + " pacjentów" + '\n';
    }

    public String messageAboutDownloadedMap() {
        return "Pobrano " + hospitals.size() + " szpitali" + '\n' +
                "Pobrano " + specialObjects.size() + " specjalnych obiektów" + '\n' +
                "Pobrano " + paths.size() + " dróg";
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
        plan = new Plan(this);
        System.out.println(plan);
        new Window(this, plan);
    }

    public HandledArea prepareData() {
        Graham grahamAlgorithm = new Graham();
        grahamAlgorithm.loadPoints(hospitals, specialObjects);
        List<Point> points = grahamAlgorithm.findConvexHull();

        HandledArea area = new HandledArea();
        area.createArea(points);
        return area;
    }

    private QueueFIFO<Hospitals> firstClosestHospitalsToPatients() {
        QueueFIFO<Hospitals> closest = new QueueFIFO<>();
        ShortestDistanceChecker distanceChecker = new ShortestDistanceChecker();
        int size = patients.size();
        for (int i = 0; i < size; i++) {
            hospitals.add(distanceChecker.closestHospital(patients.getNextToHandle(), hospitals));
        }

        return closest;
    }

    public void run() {
        area = prepareData();
        int multiplier = 100;
        double scalaX = Math.floor((double) multiplier * (WIDTH - MARGIN * 2 - PADDING) / area.getMaxWidth()) / multiplier;
        double scalaY = Math.floor((double) multiplier * (HEIGHT - MARGIN * 2 - PADDING) / area.getMaxHeight()) / multiplier;
        System.out.println(scalaX + " " + scalaY);


        double NANOSECONDS_IN_SECOND = 1_000_000_000.0;
        String timeFormat = "[ %.4fs ]\n";

        long before = System.nanoTime();

        firstClosestHospitalsToPatients();

        double time = (double) (System.nanoTime() - before) / NANOSECONDS_IN_SECOND;
        System.out.print("SZUKANIE NAJBLIŻSZEGO SZPITALA DLA DANYCH PACJENTÓW... ");
        System.out.printf(timeFormat, time);

        plan.setProperties(scalaX, scalaY, area.getMinX(), area.getMinY());
        plan.runSimulation();
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

    public Paths getPaths() {
        return paths;
    }

    public HandledArea getHandledArea() {
        return area;
    }
}
