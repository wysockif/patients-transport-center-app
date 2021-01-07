package pl.group2.optimizer.impl.items.ambulance;

import pl.group2.optimizer.gui.components.Communicator;
import pl.group2.optimizer.impl.algorithms.closest.ShortestDistanceChecker;
import pl.group2.optimizer.impl.items.area.HandledArea;
import pl.group2.optimizer.impl.items.area.Point;
import pl.group2.optimizer.impl.items.hospitals.Hospital;
import pl.group2.optimizer.impl.items.hospitals.Hospitals;
import pl.group2.optimizer.impl.items.intersections.Intersections;
import pl.group2.optimizer.impl.items.patients.Grave;
import pl.group2.optimizer.impl.items.patients.Patient;
import pl.group2.optimizer.impl.items.patients.Patients;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.util.LinkedList;
import java.util.List;

import static pl.group2.optimizer.gui.components.Plan.HEIGHT;
import static pl.group2.optimizer.gui.components.Plan.MARGIN;
import static pl.group2.optimizer.gui.components.Plan.PADDING;

public class AmbulanceService extends Thread {
    private final ShortestDistanceChecker shortestDistanceChecker;
    private final HandledArea area;
    private final Communicator communicator;
    private List<Grave> graveList;
    private Patients patients;
    private Hospitals hospitals;
    private long interval;
    private boolean running;

    private Ambulance ambulance;
    private boolean visible;
    private double rotation;
    private AffineTransform af;

    public AmbulanceService(Patients patients, Hospitals hospitals, HandledArea area, Communicator communicator) {
        this.patients = patients;
        this.hospitals = hospitals;
        this.area = area;
        this.communicator = communicator;
        running = true;
        shortestDistanceChecker = new ShortestDistanceChecker();
        graveList = new LinkedList<>();
        interval = 1200000;
    }

    @Override
    public void run() {
        while (running) {
            System.out.print("");
            if (patients.size() > 0) {
                attendToPatients();
            }
        }
    }

    public void attendToPatients() {
        Patient patient = patients.popFirst();
        ambulance = new Ambulance(patient.getId(), patient.getXCoordinate(), patient.getYCoordinate());
        ambulance.flash();
        Hospital hospital = shortestDistanceChecker.closestHospital(patient, hospitals);
        if (isOutsideArea(patient)) {
            createGrave(patient);
        } else {
            transportPatient(ambulance, hospital);
            if (hospital.getNumberOfAvailableBeds() > 0) {
                leavePatient(patient, hospital);
            } else {
                findAnotherHospital(patient, hospital, ambulance);
            }
        }
    }

    private void findAnotherHospital(Patient patient, Hospital hospital, Ambulance ambulance) {
        communicator.saveMessage("Pacjent o id = " + patient.getId() + " nie został przyjęty " +
                "w szpitalu o id = " + hospital.getId() + " (" + hospital.getName() + ")");

        // Bartek tutaj algotytm najkrótszej ścieżki
        // np. List<Points> pointsToVisit = twójAlgorytm.znajdźNajkrótsząŚcieżkę(zTegoSzpitala);
        // ja zakładam w dalszej części, że w tej liście będzie też szpital z którego startujemy
        // i chyba ten algorytm jak już ustali najkrótsze ścieżki do każdego szpitala to powinien
        // zwrócić najkrótszą, ale do tego w którym jeszcze są wolne łóżka, bo bez sensu jakby tam
        // go znowu nie przyjeli, bo z tamtego szpitala najkrótsza ścieżka może być spowrotem do tego
        // 1 szpitala i tak by krążył w kółko

        // poniższy kod zostawia ich na skrzyżowaniu

//        List<Point> pointsToVisit = new LinkedList<>();
//        pointsToVisit.add(hospital);
//        pointsToVisit.add(new Point() {
//            @Override
//            public int getXCoordinate() {
//                return 68;
//            }
//
//            @Override
//            public int getYCoordinate() {
//                return 81;
//            }
//        });
//
//        if (pointsToVisit.size() < 2) {
//            throw new UnsupportedOperationException();
//        }
//
//        for (int i = 1; i < pointsToVisit.size(); i++) {
//            ambulance.setXCoordinate(pointsToVisit.get(i - 1).getXCoordinate());
//            ambulance.setYCoordinate(pointsToVisit.get(i - 1).getYCoordinate());
//
//            Point point = pointsToVisit.get(i);
//            transportPatient(ambulance, point);
//
//        }

    }

    private void leavePatient(Patient patient, Hospital hospital) {
        hospital.decreaseNumberOfAvailableBeds();
        communicator.saveMessage("Pacjent o id = " + patient.getId() + " został przyjęty " +
                "w szpitalu o id = " + hospital.getId() + " (" + hospital.getName() + ")");
    }

    private void createGrave(Patient patient) {
        Grave grave = new Grave(patient.getXCoordinate(), patient.getYCoordinate());
        graveList.add(grave);
        String message = "Pacjent o id = " + patient.getId() + " znajduje się poza obsługiwanym obszarem";
        communicator.saveMessage(message);
        try {
            sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean isOutsideArea(Patient patient) {
        return !area.contains(patient.getXCoordinate(), patient.getYCoordinate());
    }

    private void transportPatient(Ambulance ambulance, Point point) {
        double sourceX = ambulance.getXCoordinate();
        double sourceY = ambulance.getYCoordinate();
        int destX = point.getXCoordinate();
        int destY = point.getYCoordinate();

        rotation = Math.toRadians(90);
        if(destX - sourceX != 0) {
            rotation = -Math.atan((destY - sourceY) / (destX - sourceX));
        }

        if (destX < sourceX) {
            ambulance.setRightSprite();
        } else {
            ambulance.setLeftSprite();
        }

        double vx = destX - sourceX;
        double vy = destY - sourceY;

        double distance = Math.sqrt((destX - sourceX) * (destX - sourceX) + (destY - sourceY) * (destY - sourceY));
        double step = 1 / (200 * distance);

        visible = true;
        for (double dist = 0.0; dist <= 1; dist += step) {
            ambulance.setXCoordinate((sourceX + vx * dist));
            ambulance.setYCoordinate((sourceY + vy * dist));

            long start = System.nanoTime();
            long end;
            do {
                end = System.nanoTime();
            } while (start + interval >= end);

        }
        visible = false;
    }

    public void drawAmbulance(Graphics g, double scalaX, double scalaY, int minX, int minY) {
        if (visible) {
            int xShift = 37;
            int yShift = 65;

            int x = (int) Math.round(PADDING + ambulance.getXCoordinate() * scalaX + MARGIN - xShift - minX * scalaX);
            int y = (int) Math.round(PADDING + HEIGHT - (ambulance.getYCoordinate() * scalaY) - MARGIN - yShift + minY * scalaY);

            double locationX = 37;
            double locationY = 30;
            AffineTransform tx = AffineTransform.getRotateInstance(rotation, locationX, locationY);
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(op.filter(ambulance.getCurrentImage(), null), x, y, null);
        }
    }

    public void drawDeadPatients(Graphics g, double scalaX, double scalaY, int minX, int minY) {
        for (Grave grave : graveList) {
            int xShift = 20;
            int yShift = 25;

            int x = (int) Math.round(PADDING + grave.getXCoordinate() * scalaX + MARGIN - xShift - minX * scalaX);
            int y = (int) Math.round(PADDING + HEIGHT - (grave.getYCoordinate() * scalaY) - MARGIN - yShift + minY * scalaY);

            g.drawImage(grave.getImage(), x, y, null);
        }
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }
}
