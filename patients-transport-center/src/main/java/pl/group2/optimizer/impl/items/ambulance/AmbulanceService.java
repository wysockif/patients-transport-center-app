package pl.group2.optimizer.impl.items.ambulance;

import pl.group2.optimizer.gui.components.Communicator;
import pl.group2.optimizer.impl.algorithms.closest.ShortestDistanceChecker;
import pl.group2.optimizer.impl.items.area.HandledArea;
import pl.group2.optimizer.impl.items.hospitals.Hospital;
import pl.group2.optimizer.impl.items.hospitals.Hospitals;
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

        //algotytm najkrótszej ścieżki
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

    private void transportPatient(Ambulance ambulance, Hospital hospital) {


        double sourceX = ambulance.getXCoordinate();
        double sourceY = ambulance.getYCoordinate();
        int destX = hospital.getXCoordinate();
        int destY = hospital.getYCoordinate();

        rotation = -Math.atan((destY - sourceY) / (destX - sourceX));

        if(destX < sourceX){
            ambulance.setRightSprite();
        } else {
            ambulance.setLeftSprite();
        }

        double vx = destX - sourceX;
        double vy = destY - sourceY;

        double distance = Math.sqrt((destX - sourceX) * (destX - sourceX) + (destY - sourceY) * (destY - sourceY));
        double step = 1 / (200 * distance);

//        System.out.println(Thread.currentThread().getName());

//        final Double[] ti = {0.0};
//
//        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(0);
//        scheduledExecutorService.scheduleAtFixedRate(() -> {
//            ti[0] += step;
//            System.out.println(Thread.currentThread().getName());
//            if (ti[0] >= 1.0) {
//                scheduledExecutorService.shutdown();
//                scheduledExecutorService.shutdownNow();
//            }

//
//        }, 0, 10, TimeUnit.MILLISECONDS);


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

//        String message = "Pacjent o id = " + ambulance.getPatientId() + " został przetransportowany " +
//                "do szpitala o id = " + hospital.getId() + " (" + hospital.getName() + ")";
//        communicator.saveMessage(message);
    }


    public Ambulance getAmbulance() {
        return ambulance;
    }


    public void drawAmbulance(Graphics g, double scalaX, double scalaY, int minX, int minY) {
        if (visible) {
            int xShift = 37;
            int yShift = 37;

            int x = (int) Math.round(PADDING + ambulance.getXCoordinate() * scalaX + MARGIN - xShift - minX * scalaX);
            int y = (int) Math.round(PADDING + HEIGHT - (ambulance.getYCoordinate() * scalaY) - MARGIN - yShift + minY * scalaY);

            double rotationRequired = rotation;
            double locationX = ambulance.getCurrentImage().getWidth() / 2;
            double locationY = ambulance.getCurrentImage().getHeight() / 2;
            AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
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
