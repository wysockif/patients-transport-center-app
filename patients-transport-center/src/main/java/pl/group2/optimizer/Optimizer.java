package pl.group2.optimizer;

import pl.group2.optimizer.gui.Window;
import pl.group2.optimizer.gui.components.PatientsManagement;
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

import javax.swing.JOptionPane;
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
    private PatientsManagement patientsManagement;

    private double scaleX;
    private double scaleY;
    private boolean running;

    public Optimizer() {
        timeFormat = "[ %.4fs ]\n";
    }

    public String messageAboutDownloadedPatients() {
        String time = String.format("[ %.4fs ]", timeOfDownloadingPatients);
        return "Pobrano " + patients.size() + " pacjentów" + '\n' + "w " + time;
    }

    public String messageAboutDownloadedMap() {
        String time = String.format("[ %.4fs ]", timeOfDownloadingMap);
        return "Pobrano " + hospitals.size() + " szpitali" + '\n' +
                "Pobrano " + specialObjects.size() + " specjalnych obiektów" + '\n' +
                "Pobrano " + paths.size() + " dróg" + '\n' +
                "w " + time;

    }

    public String messageAboutTimeOfGraham(double timeOfGraham) {
        String time = String.format("[ %.4fs ]", timeOfGraham);
        return "Algorytm tworzenia obszaru trwał " + time;
    }

    double timeOfDownloadingPatients = 0;

    public void loadPatients(String inputFilePath) throws MyException {
        System.out.print("TRWA ODCZYTYWANIE I WALIDACJA DANYCH PACJENTÓW... ");
        long before = System.nanoTime();

        TextFileReader textFileReader = new TextFileReader();
        textFileReader.readData(inputFilePath, patientsManagement);

        patients = textFileReader.getPatients();
        // if patients are the first to download it will throw null exception
        patients.setHospitalsSize(hospitals.size());

        double time = (double) (System.nanoTime() - before) / NANOSECONDS_IN_SECOND;
        timeOfDownloadingPatients = time;
        System.out.printf(timeFormat, time);
    }

    double timeOfDownloadingMap = 0;
    public void loadMap(String inputFilePath) throws MyException {
        System.out.print("TRWA ODCZYTYWANIE I WALIDACJA DANYCH SZPITALI, S. OBIEKTÓW I DRÓG... ");
        long before = System.nanoTime();

        TextFileReader textFileReader = new TextFileReader();
        textFileReader.readData(inputFilePath);
        hospitals = textFileReader.getHospitals();

        specialObjects = textFileReader.getSpecialObjects();
        specialObjects.setHospitalsSize(hospitals.size());

        paths = textFileReader.getPaths();
        paths.setHospitalsSize(hospitals.size());

        double time = (double) (System.nanoTime() - before) / NANOSECONDS_IN_SECOND;
        timeOfDownloadingMap = time;
        System.out.printf(timeFormat, time);
    }

    public void createWindow() {
        plan = new Plan(this);
        patientsManagement = new PatientsManagement(this);
        new Window(this, plan, patientsManagement);
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
        long before = System.nanoTime();
        area = prepareData();
        double time = (double) (System.nanoTime() - before) / NANOSECONDS_IN_SECOND;
        JOptionPane.showMessageDialog(null, messageAboutTimeOfGraham(time),
                "Patients Transport Center", JOptionPane.INFORMATION_MESSAGE);

        int multiplier = 100;
        scaleX = Math.floor((double) multiplier * (WIDTH - MARGIN * 2 - PADDING) / area.getMaxWidth()) / multiplier;
        scaleY = Math.floor((double) multiplier * (HEIGHT - MARGIN * 2 - PADDING) / area.getMaxHeight()) / multiplier;
//        System.out.println(scaleX + " " + scaleY);

        plan.setProperties(scaleX, scaleY, area.getMinX(), area.getMinY());
        plan.runSimulation();
        running = true;
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

    public PatientsManagement getPatientsManagement() {
        return patientsManagement;
    }

    public double getScaleX() {
        return scaleX;
    }

    public double getScaleY(){
        return scaleY;
    }

    public boolean isRunning() {
        return running;
    }

    public int getMinY() {
        return area.getMinY();
    }

    public int getMinX(){
        return area.getMinX();
    }
}
