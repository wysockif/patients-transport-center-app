package pl.group2.optimizer.impl.items.patients;

import pl.group2.optimizer.gui.components.PatientsManagement;
import pl.group2.optimizer.impl.items.Items;
import pl.group2.optimizer.impl.structures.queues.QueueFIFO;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

import static java.awt.Color.WHITE;
import static pl.group2.optimizer.gui.components.Plan.HEIGHT;
import static pl.group2.optimizer.gui.components.Plan.MARGIN;
import static pl.group2.optimizer.gui.components.Plan.PADDING;

public class Patients implements Items {
    private final QueueFIFO<Patient> patientsQueue;
    private final PatientsManagement patientsManagement;

    public Patients(PatientsManagement patientsManagement) {
        this.patientsManagement = patientsManagement;
        patientsQueue = new QueueFIFO<>();
    }

    public void addNew(Patient patient) {
        patient.loadSprite(patientsQueue.size() % 6);
        patientsManagement.addNewPatient(patient.getId(), patient.getXCoordinate(), patient.getYCoordinate());
        patientsQueue.add(patient);
    }

    public Patient popFirst() {
        patientsManagement.removeFirst();
        return patientsQueue.remove();
    }

    public boolean contain(int id) {
        return patientsQueue.contains(new Patient(id, 0, 0));
    }

    public int size() {
        return patientsQueue.size();
    }


    @Override
    public Object[] convertAttributes(String[] attributes) throws DataFormatException {
        checkIfArgumentIsNotNull(attributes);
        if (attributes.length != 3) {
            throw new DataFormatException("Niepoprawny format danych");
        }
        return parseAttributes(attributes);
    }

    @Override
    public void validateAttributes(Object[] attributes) throws DataFormatException {
        checkIfArgumentIsNotNull(attributes);
        int id = (int) attributes[0];

        if (id < 0) {
            String message = "Niepoprawny format danych. Ujemna wartość reprezentująca id pacjenta";
            throw new DataFormatException(message);
        }
        if (contain(id)) {
            throw new DataFormatException("Nie można dodawać pacjentów o tym samym id");
        }
    }

    @Override
    public void addNewElement(Object[] attributes) {
        checkIfArgumentIsNotNull(attributes);
        int id = (int) attributes[0];
        int x = (int) attributes[1];
        int y = (int) attributes[2];

        Patient patient = new Patient(id, x, y);

        addNew(patient);
    }

    private void checkIfArgumentIsNotNull(Object argument) {
        if (argument == null) {
            throw new IllegalArgumentException("Niezainicjowany argument");
        }
    }

    private Object[] parseAttributes(String[] attributes) throws DataFormatException {
        checkIfArgumentIsNotNull(attributes);
        Object[] convertedAttributes = new Object[attributes.length];
        try {
            convertedAttributes[0] = Integer.parseInt(attributes[0]);
            convertedAttributes[1] = Integer.parseInt(attributes[1]);
            convertedAttributes[2] = Integer.parseInt(attributes[2]);
        } catch (NumberFormatException e) {
            String regex = Pattern.quote("For input string: ");
            String info = e.getMessage().replaceAll(regex, "");
            String message = "Nieudana konwersja danej: " + info;
            throw new DataFormatException(message);
        }
        return convertedAttributes;
    }

    public Collection<Patient> getCollection() {
        return patientsQueue.getCollection();
    }


    public void setNumberOfMapElements(int numberOfMapElements) {
    }

    @Override
    public void draw(Graphics g, double scalaX, double scalaY, int minX, int minY) {
        drawAureole(g, scalaX, scalaY, minX, minY);
        for (Patient patient : getCollection()) {
            int xShift = patient.getImageWidth() / 2;
            int yShift = patient.getImageHeight() / 2;

            int x = (int) Math.round(PADDING + patient.getXCoordinate() * scalaX + MARGIN - xShift - minX * scalaX);
            int y = (int) Math.round(PADDING + HEIGHT - (patient.getYCoordinate() * scalaY) - MARGIN - yShift + minY * scalaY);

            Image image = patient.getImage();
            g.drawImage(image, x, y, null);
            drawCenteredString(g, String.valueOf(patient.getId()),
                    new Rectangle(x + 1, y - yShift, 2 * xShift, yShift),
                    new Font("id-font", Font.PLAIN, 11));
        }
    }

    private void drawAureole(Graphics g, double scalaX, double scalaY, int minX, int minY) {
        if (!patientsQueue.isEmpty()) {
            Patient firstPatient = patientsQueue.front();
            int xShift = firstPatient.getImageWidth() / 2;
            int yShift = firstPatient.getImageHeight() / 2;
            int x = (int) Math.round(PADDING + firstPatient.getXCoordinate() * scalaX + MARGIN - xShift - minX * scalaX);
            int y = (int) Math.round(PADDING + HEIGHT - (firstPatient.getYCoordinate() * scalaY) - MARGIN - yShift + minY * scalaY);
            g.setColor(new Color(193, 116, 13, 100));
            g.fillOval(x - 10, y - 10, 45, 45);
        }
    }

    public void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.setColor(WHITE);
        g.setFont(font);
        g.drawString(text, x, y);
    }
}