package pl.group2.optimizer;

import pl.group2.optimizer.gui.Window;
import pl.group2.optimizer.gui.components.Communicator;
import pl.group2.optimizer.gui.components.PatientsManagement;
import pl.group2.optimizer.gui.components.Plan;
import pl.group2.optimizer.impl.algorithms.closest.ShortestDistanceChecker;
import pl.group2.optimizer.impl.algorithms.graham.Graham;
import pl.group2.optimizer.impl.io.MyException;
import pl.group2.optimizer.impl.io.TextFileReader;
import pl.group2.optimizer.impl.items.area.HandledArea;
import pl.group2.optimizer.impl.items.area.Point;
import pl.group2.optimizer.impl.items.hospitals.Hospital;
import pl.group2.optimizer.impl.items.hospitals.Hospitals;
import pl.group2.optimizer.impl.items.intersections.Intersections;
import pl.group2.optimizer.impl.items.paths.Paths;
import pl.group2.optimizer.impl.items.patients.Patient;
import pl.group2.optimizer.impl.items.patients.Patients;
import pl.group2.optimizer.impl.items.specialobjects.SpecialObjects;

import javax.swing.JOptionPane;
import java.awt.Graphics2D;
import java.util.List;

import static pl.group2.optimizer.gui.components.Plan.HEIGHT;
import static pl.group2.optimizer.gui.components.Plan.MARGIN;
import static pl.group2.optimizer.gui.components.Plan.PADDING;
import static pl.group2.optimizer.gui.components.Plan.WIDTH;

public class Optimizer {


    private Patients patients;
    private Hospitals hospitals;
    private SpecialObjects specialObjects;
    private Paths paths;
    private Plan plan;
    private HandledArea area;
    private PatientsManagement patientsManagement;
    private Communicator communicator;

    private double scaleX;
    private double scaleY;
    private boolean running;
    private Intersections intersections;


    public String messageAboutDownloadedPatients() {
        return "Pobrano " + patients.size() + " pacjentów";
    }

    public String messageAboutDownloadedMap() {
        return "Pobrano " + hospitals.size() + " szpitali;\n" +
                " > Pobrano " + specialObjects.size() + " specjalnych obiektów;\n" +
                " > Pobrano " + paths.size() + " dróg";

    }

    public String messageAboutTimeOfGraham(double timeOfGraham) {
        String time = String.format("[ %.4fs ]", timeOfGraham);
        return "Algorytm tworzenia obszaru trwał " + time;
    }


    public void loadPatients(String inputFilePath) throws MyException {
        TextFileReader textFileReader = new TextFileReader();
        textFileReader.readData(inputFilePath, patientsManagement);

        patients = textFileReader.getPatients();
        // if patients are the first to download it will throw null exception
        patients.setHospitalsSize(hospitals.size());
        communicator.saveMessage(messageAboutDownloadedPatients());
    }

    public void loadMap(String inputFilePath) throws MyException {
        TextFileReader textFileReader = new TextFileReader();
        textFileReader.readData(inputFilePath);
        hospitals = textFileReader.getHospitals();

        specialObjects = textFileReader.getSpecialObjects();
        specialObjects.setHospitalsSize(hospitals.size());

        paths = textFileReader.getPaths();
        intersections = new Intersections();
        intersections.lookForIntersections(paths.getList());
        paths.setHospitalsSize(hospitals.size());
        communicator.saveMessage(messageAboutDownloadedMap());
    }

    public void createWindow() {
        plan = new Plan(this);
        patientsManagement = new PatientsManagement(this);
        communicator = new Communicator();
        new Window(this, plan, patientsManagement, communicator);
    }

    public HandledArea prepareData() {
        Graham grahamAlgorithm = new Graham();
        grahamAlgorithm.loadPoints(hospitals, specialObjects);
        List<Point> points = grahamAlgorithm.findConvexHull();

        HandledArea area = new HandledArea();
        area.createArea(points);
        return area;
    }

    private void showMessagesAboutClosesHospitals() {
        ShortestDistanceChecker distanceChecker = new ShortestDistanceChecker();

        Hospital closest;

        int size = patients.size();
        communicator.saveMessage("Obsługa pacjentów z pliku:");
        for (int i = 0; i < size; i++) {
            Patient first = patients.getFirst();
            closest = distanceChecker.closestHospital(patients.getNextToHandle(), hospitals);

            String message = "Closest hospital to patient: " + first.toString()
                    + " is " + closest.toString();

            communicator.saveMessage(message);

            // tymczasowo, żeby wszyscy pacjenci naraz nie zniknęli
            JOptionPane.showMessageDialog(null, message,
                    "Patients Transport Center", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void run() {
        area = prepareData();
        int multiplier = 100;
        scaleX = Math.floor((double) multiplier * (WIDTH - MARGIN * 2 - PADDING) / area.getMaxWidth()) / multiplier;
        scaleY = Math.floor((double) multiplier * (HEIGHT - MARGIN * 2 - PADDING) / area.getMaxHeight()) / multiplier;
        plan.setProperties(scaleX, scaleY, area.getMinX(), area.getMinY());
        plan.runSimulation();
        running = true;

//        showMessagesAboutClosesHospitals();
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

    public Communicator getCommunicator() {
        return communicator;
    }

    public Intersections getIntersections() {
        return intersections;
    }
}
