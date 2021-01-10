package pl.group2.optimizer.impl.items.ambulance;

import pl.group2.optimizer.gui.components.Communicator;
import pl.group2.optimizer.impl.algorithms.closest.ShortestDistanceChecker;
import pl.group2.optimizer.impl.algorithms.dijkstra.DijkstraAlgorithm;
import pl.group2.optimizer.impl.items.Vertex;
import pl.group2.optimizer.impl.items.area.HandledArea;
import pl.group2.optimizer.impl.items.area.Point;
import pl.group2.optimizer.impl.items.hospitals.Hospital;
import pl.group2.optimizer.impl.items.hospitals.Hospitals;
import pl.group2.optimizer.impl.items.patients.Grave;
import pl.group2.optimizer.impl.items.patients.Patient;
import pl.group2.optimizer.impl.items.patients.Patients;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static pl.group2.optimizer.gui.components.Plan.HEIGHT;
import static pl.group2.optimizer.gui.components.Plan.MARGIN;
import static pl.group2.optimizer.gui.components.Plan.PADDING;

public class AmbulanceService extends Thread {
    private final ShortestDistanceChecker shortestDistanceChecker;
    private final HandledArea area;
    private final Communicator communicator;
    private final List<Grave> graveList;
    private final Patients patients;
    private final Hospitals hospitals;
    private long interval;


    private Ambulance ambulance;

    private boolean running;
    private boolean isVisible;
    private double rotation;

    private AffineTransform af;
    DijkstraAlgorithm dijkstraAlgorithm;

    public AmbulanceService(Patients patients, Hospitals hospitals, HandledArea area,
                            Communicator communicator, DijkstraAlgorithm dijkstraAlgorithm) {
        this.patients = patients;
        this.hospitals = hospitals;
        this.area = area;
        this.communicator = communicator;
        running = true;
        shortestDistanceChecker = new ShortestDistanceChecker();
        graveList = new LinkedList<>();
        interval = 1200000;

        this.dijkstraAlgorithm = dijkstraAlgorithm;
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
            isVisible = false;
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

        // BBartek tutaj algotytm najkrótszej ścieżki
        // np. List<Points> pointsToVisit = twójAlgorytm.znajdźNajkrótsząŚcieżkę(zTegoSzpitala);
        // ja zakładam w dalszej części, że w tej liście będzie też szpital z którego startujemy
        // i chyba ten algorytm jak już ustali najkrótsze ścieżki do każdego szpitala to powinien
        // zwrócić najkrótszą, ale do tego w którym jeszcze są wolne łóżka, bo bez sensu jakby tam
        // go znowu nie przyjeli, bo z tamtego szpitala najkrótsza ścieżka może być spowrotem do tego
        // 1 szpitala i tak by krążył w kółko

        List<Vertex> points = dijkstraAlgorithm.shortestPathFromSelectedVertexToHospital(hospital);
        Hospital hospitalToLeave = dijkstraAlgorithm.getNewHospital();

        List<Point> pointsToVisit = new LinkedList<>();

        for (Vertex vertex : points) {
            pointsToVisit.add(new Point() {
                @Override
                public int getXCoordinate() {
                    return vertex.getXCoordinate();
                }

                @Override
                public int getYCoordinate() {
                    return vertex.getYCoordinate();
                }
            });
        }

        System.out.println(Arrays.toString(pointsToVisit.toArray()));
//
//        pointsToVisit.add(hospital);
//
//        pointsToVisit.add(ppoint);

//        if (pointsToVisit.size() < 2) {
//            throw new UnsupportedOperationException();
//        }

        for (int i = 0; i < pointsToVisit.size(); i++) {
//            System.out.println("Karetka ma jechać do punktu");
//            System.out.println(pointsToVisit.get(i).getXCoordinate());
//            System.out.println(pointsToVisit.get(i).getYCoordinate());
//            ambulance.setXCoordinate(pointsToVisit.get(i).getXCoordinate());
//            ambulance.setYCoordinate(pointsToVisit.get(i).getYCoordinate());

            Point point = pointsToVisit.get(i);
            transportPatient(ambulance, point);

//            if (hospitalToLeave.getNumberOfAvailableBeds() == 0) {
//                findAnotherHospital(patient, hospitalToLeave, ambulance);
//            } else {
//                leavePatient(patient, hospitalToLeave);
//            }
        }
        leavePatient(patient, hospitalToLeave);

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

        setHorizontalDirection(ambulance, sourceX, destX);
        rotateAmbulance(ambulance, sourceX, sourceY, destX, destY);

        double vx = destX - sourceX;
        double vy = destY - sourceY;

        double distance = Math.sqrt((destX - sourceX) * (destX - sourceX) + (destY - sourceY) * (destY - sourceY));
        double step = 1 / (200 * distance);

        driveAmbulance(ambulance, sourceX, sourceY, vx, vy, step);
    }

    private void driveAmbulance(Ambulance ambulance, double sourceX, double sourceY, double vx, double vy, double step) {
        isVisible = true;
        for (double dist = 0.05; dist <= 1; dist += step) {
            ambulance.setXCoordinate((sourceX + vx * dist));
            ambulance.setYCoordinate((sourceY + vy * dist));
            if (dist < 0.95) {
                long start = System.nanoTime();
                long end;
                do {
                    end = System.nanoTime();
                } while (start + interval >= end);
            }
        }
    }

    private void setHorizontalDirection(Ambulance ambulance, double sourceX, int destX) {
        if (destX < sourceX) {
            ambulance.setLeftSprite();
        } else {
            ambulance.setRightSprite();
        }
    }

    private void rotateAmbulance(Ambulance ambulance, double sourceX, double sourceY, int destX, int destY) {
        int xShift = 55;
        int yShift = 55;
        double x1 = Math.round(PADDING + sourceX * area.getScaleX() + MARGIN - xShift - area.getMinX() * area.getScaleX());
        double y1 = Math.round(PADDING + HEIGHT - (sourceY * area.getScaleY()) - MARGIN - yShift + area.getMinY() * area.getScaleY());
        double x2 = Math.round(PADDING + destX * area.getScaleX() + MARGIN - xShift - area.getMinX() * area.getScaleX());
        double y2 = Math.round(PADDING + HEIGHT - (destY * area.getScaleY()) - MARGIN - yShift + area.getMinY() * area.getScaleY());

        rotation = Math.toRadians(90);
        if (x2 - x1 != 0) {
            rotation = Math.atan((y2 - y1) / (x2 - x1));
        } else {
            setVerticalDirection(ambulance, sourceY, destY);
        }
    }

    private void setVerticalDirection(Ambulance ambulance, double sourceY, int destY) {
        if (destY > sourceY) {
            ambulance.setLeftSprite();
        } else {
            ambulance.setRightSprite();
        }
    }

    public void drawAmbulance(Graphics g, double scalaX, double scalaY, int minX, int minY) {
        if (isVisible) {
            int xShift = 55;
            int yShift = 58;
            int x = (int) Math.round(PADDING + ambulance.getXCoordinate() * scalaX + MARGIN - xShift - minX * scalaX);
            int y = (int) Math.round(PADDING + HEIGHT - (ambulance.getYCoordinate() * scalaY) - MARGIN - yShift + minY * scalaY);

            AffineTransform tx = AffineTransform.getRotateInstance(rotation, xShift, yShift);
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

    public void setRunning(boolean running) {
        this.running = running;
    }
}
