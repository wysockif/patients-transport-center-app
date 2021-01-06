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
    private long period;
    private boolean running;

    private Ambulance ambulance;
    private boolean visible;
    private double rotation;
    private AffineTransform af;

    public AmbulanceService(Patients patients, Hospitals hospitals, HandledArea area, Communicator communicator, boolean running) {
        this.patients = patients;
        this.hospitals = hospitals;
        this.area = area;
        this.communicator = communicator;
        this.running = running;
        shortestDistanceChecker = new ShortestDistanceChecker();
        graveList = new LinkedList<>();
        this.period = 60;
    }

    @Override
    public void run() {
        while (running) {
            System.out.println(patients.size());
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
        }
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

        double vx = destX - sourceX;
        double vy = destY - sourceY;

        double distance = Math.sqrt((destX - sourceX) * (destX - sourceX) + (destY - sourceY) * (destY - sourceY));
        double step = 1 / (100 * distance);

        System.out.println(Thread.currentThread().getName());

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
            try {
                sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        visible = false;

        String message = "Pacjent o id = " + ambulance.getPatientId() + " został przetransportowany " +
                "do szpitala o id = " + hospital.getId() + " (" + hospital.getName() + ")";
        communicator.saveMessage(message);
    }


    public Ambulance getAmbulance() {
        return ambulance;
    }


    public void drawAmbulance(Graphics g, double scalaX, double scalaY, int minX, int minY) {
        if (visible) {
            int xShift = 29;
            int yShift = 30;

            int x = (int) Math.round(PADDING + ambulance.getXCoordinate() * scalaX + MARGIN - xShift - minX * scalaX);
            int y = (int) Math.round(PADDING + HEIGHT - (ambulance.getYCoordinate() * scalaY) - MARGIN - yShift + minY * scalaY);

//            g.drawImage(rotate(ambulance.getCurrentImage(), rotation), x, y, null);

            Graphics2D g2d = (Graphics2D) g;
            g2d.rotate(rotation);


            double rotationRequired = Math.toRadians(45);
            AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, xShift, yShift);
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

// Drawing the rotated image at the required drawing locations
            g2d.drawImage(op.filter(ambulance.getCurrentImage(), null), x, y, null);

//            g.drawImage(ambulance.getCurrentImage(), x, y, null);
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
}
