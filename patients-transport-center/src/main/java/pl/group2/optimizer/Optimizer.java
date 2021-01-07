package pl.group2.optimizer;

import pl.group2.optimizer.gui.Window;
import pl.group2.optimizer.gui.components.Communicator;
import pl.group2.optimizer.gui.components.PatientsManagement;
import pl.group2.optimizer.gui.components.Plan;
import pl.group2.optimizer.impl.algorithms.closest.ShortestDistanceChecker;
import pl.group2.optimizer.impl.algorithms.graham.Graham;
import pl.group2.optimizer.impl.io.MyException;
import pl.group2.optimizer.impl.io.TextFileReader;
import pl.group2.optimizer.impl.items.ambulance.AmbulanceService;
import pl.group2.optimizer.impl.items.area.HandledArea;
import pl.group2.optimizer.impl.items.area.Point;
import pl.group2.optimizer.impl.items.hospitals.Hospital;
import pl.group2.optimizer.impl.items.hospitals.Hospitals;
import pl.group2.optimizer.impl.items.intersections.Intersections;
import pl.group2.optimizer.impl.items.paths.Paths;
import pl.group2.optimizer.impl.items.patients.Patients;
import pl.group2.optimizer.impl.items.specialobjects.SpecialObject;
import pl.group2.optimizer.impl.items.specialobjects.SpecialObjects;

import java.awt.image.BufferedImage;
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
    private AmbulanceService ambulanceService;
    private Communicator communicator;

    private double scaleX;
    private double scaleY;
    private boolean running;
    private Intersections intersections;


    public Optimizer() {
        timeFormat = "[ %.4fs ]\n";
    }

    public String messageAboutDownloadedPatients() {
        String time = String.format("[ %.4fs ]", timeOfDownloadingPatients);
        return "Wczytano " + patients.size() + " pacjentów" + " w " + time;
    }

    public String messageAboutDownloadedMap() {
        String time = String.format("[ %.4fs ]", timeOfDownloadingMap);
        return "Wczytano " + hospitals.size() + " szpitali, " +
                specialObjects.size() + " specjalnych obiektów, " +
                paths.size() + " dróg" +
                " w " + time;

    }


    double timeOfDownloadingPatients = 0;


    public void loadPatients(String inputFilePath) throws MyException {
        long before = System.nanoTime();

        TextFileReader textFileReader = new TextFileReader();
        textFileReader.readData(inputFilePath, patientsManagement);

        patients = textFileReader.getPatients();
        double time = (double) (System.nanoTime() - before) / NANOSECONDS_IN_SECOND;
        timeOfDownloadingPatients = time;
        //System.out.printf(timeFormat, time);
        communicator.saveMessage(messageAboutDownloadedPatients());
    }

    double timeOfDownloadingMap = 0;


    private int numberOfElements = 0;

    public void loadMap(String inputFilePath) throws MyException {
        long before = System.nanoTime();

        TextFileReader textFileReader = new TextFileReader();
        textFileReader.readData(inputFilePath);
        hospitals = textFileReader.getHospitals();
        specialObjects = textFileReader.getSpecialObjects();
        paths = textFileReader.getPaths();

        numberOfElements = hospitals.size() + specialObjects.size();
        specialObjects.setNumberOfMapElements(numberOfElements);
        hospitals.setNumberOfMapElements(numberOfElements);
        paths.setNumberOfMapElements(numberOfElements);

        intersections = new Intersections();
        intersections.lookForIntersections(paths.getList());

        double time = (double) (System.nanoTime() - before) / NANOSECONDS_IN_SECOND;
        timeOfDownloadingMap = time;
        communicator.saveMessage(messageAboutDownloadedMap());

        //System.out.printf(timeFormat, time);

        scaleMap(numberOfElements);
        area = prepareData();
    }


    Window window;

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


    public void run() {
        // komentarz dla Bartka
        // możesz tutaj stworzyć obiekt klasy reprezentującej ten algorytm
        // i wstrzyknąć go w poniższym konstruktorze do Ambulance Service
        // żeby nie wciągać tam oddzielnie skrzyżowań, ścieżek, a juz sam algorytm

        ambulanceService = new AmbulanceService(patients, hospitals, area, communicator);
        ambulanceService.start();
    }

    public void showMap() {
        area = prepareData();
        int multiplier = 100;
        scaleX = Math.floor((double) multiplier * (WIDTH - MARGIN * 2 - PADDING) / area.getMaxWidth()) / multiplier;
        scaleY = Math.floor((double) multiplier * (HEIGHT - MARGIN * 2 - PADDING) / area.getMaxHeight()) / multiplier;
        plan.setProperties(scaleX, scaleY, area.getMinX(), area.getMinY());
        plan.showMap();
        running = true;
    }

    public void scaleMap(int numberOfElements) {
        double scale;
        if (numberOfElements < 20) {
            scale = 1;
        } else if (numberOfElements < 100) {
            scale = 0.7;
        } else {
            scale = 0.5;
        }

        for (Hospital hospital : hospitals.getCollection()) {
            hospital.setImage(hospital.scale(hospital.getImage(), scale));
        }

        for (SpecialObject specialObject : specialObjects.getCollection()) {
            specialObject.setImage(specialObject.scale((BufferedImage) specialObject.getImage(), scale));
        }
    }

    private void scalePatients(int numberOfPatients) {

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

    public double getScaleY() {
        return scaleY;
    }

    public boolean isRunning() {
        return running;
    }

    public int getMinY() {
        return area.getMinY();
    }

    public int getMinX() {
        return area.getMinX();
    }

    public Communicator getCommunicator() {
        return communicator;
    }

    public Intersections getIntersections() {
        return intersections;
    }

    public AmbulanceService getAmbulanceService() {
        return ambulanceService;
    }

    public void changePeriod(long value) {
        if(ambulanceService != null){
            long interval = 1500000 - 150000 * value;
            ambulanceService.setInterval(interval);
        }
    }
}
