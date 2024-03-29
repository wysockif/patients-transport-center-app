package pl.group2.optimizer.impl.items.hospitals;

import pl.group2.optimizer.impl.items.Items;

import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

import static java.awt.Color.WHITE;
import static pl.group2.optimizer.gui.components.Plan.HEIGHT;
import static pl.group2.optimizer.gui.components.Plan.MARGIN;
import static pl.group2.optimizer.gui.components.Plan.PADDING;

public class Hospitals implements Items {
    private final Map<Integer, Hospital> hospitalsByIndex;
    private final Map<Integer, Integer> indexById;
    private final List<Integer> indexes;
    private int counter;

    public Hospitals() {
        hospitalsByIndex = new HashMap<>();
        indexById = new HashMap<>();
        indexes = new LinkedList<>();
    }

    public int size() {
        return hospitalsByIndex.size();
    }

    public int getMaxId() {
        checkNumberOfElements();
        int max = indexes.get(0);
        for (int i = 1; i < indexes.size(); i++) {
            if (indexes.get(i) > max) {
                max = indexes.get(i);
            }
        }
        return max;
    }

    public void add(Hospital hospital) {
        int id = hospital.getId();
        hospitalsByIndex.put(counter, hospital);
        indexById.put(id, counter);
        indexes.add(id);
        counter++;
    }

    public Hospital getHospitalByIndex(int index) {
        return hospitalsByIndex.get(index);
    }

    public boolean contain(int id) {
        return indexById.containsKey(id);
    }

    public Hospital getHospitalById(int id) {
        int index = indexById.get(id);
        return hospitalsByIndex.get(index);
    }

    @Override
    public Object[] convertAttributes(String[] attributes) throws DataFormatException {
        checkIfArgumentIsNotNull(attributes);
        if (attributes.length != 6) {
            throw new DataFormatException("Niepoprawny format danych");
        }
        return parseAttributes(attributes);
    }

    @Override
    public void validateAttributes(Object[] attributes) throws DataFormatException {
        checkIfArgumentIsNotNull(attributes);
        int id = (int) attributes[0];
        int numberOfBeds = (int) attributes[4];
        int numberOfAvailableBeds = (int) attributes[5];

        if (id < 0) {
            String message = "Niepoprawny format danych. Ujemna wartość reprezentująca id szpitala";
            throw new DataFormatException(message);
        }
        if (contain(id)) {
            throw new DataFormatException("Nie można dodawać szpitali o tym samym id.");
        }

        if (numberOfBeds < 0 || numberOfAvailableBeds < 0) {
            String message = "Niepoprawny format danych. Ujemna wartość reprezentująca liczbę łóżek szpitala.";
            throw new DataFormatException(message);
        }
    }

    @Override
    public void addNewElement(Object[] attributes) {
        checkIfArgumentIsNotNull(attributes);
        int id = (int) attributes[0];
        String name = (String) attributes[1];
        int x = (int) attributes[2];
        int y = (int) attributes[3];
        int numberOfBeds = (int) attributes[4];
        int numberOfAvailableBeds = (int) attributes[5];
        Hospital hospital = new Hospital(id, name, x, y, numberOfBeds, numberOfAvailableBeds);
        int spriteType = hospitalsByIndex.size() % 5 + 1;
        hospital.loadSprite(spriteType);
        add(hospital);
    }

    @Override
    public void draw(Graphics g, double scalaX, double scalaY, int minX, int minY) {
        for (Hospital hospital : getCollection()) {
            BufferedImage image = hospital.getImage();
            int xShift = hospital.getImageWidth() / 2;
            int yShift = hospital.getImageHeight() / 2;

            int x = (int) Math.round(PADDING + hospital.getXCoordinate() * scalaX + MARGIN - xShift - minX * scalaX);
            int y = (int) Math.round(PADDING + HEIGHT - (hospital.getYCoordinate() * scalaY) - MARGIN - yShift + minY * scalaY);

            g.drawImage(image, x, y, null);

            drawCenteredString(g, hospital.getId() + " | " + hospital.getNumberOfAvailableBeds(),
                    new Rectangle(x, y - yShift / 2, 2 * xShift, yShift),
                    new Font("id-font", Font.PLAIN, 12));
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
            convertedAttributes[1] = attributes[1];
            convertedAttributes[2] = Integer.parseInt(attributes[2]);
            convertedAttributes[3] = Integer.parseInt(attributes[3]);
            convertedAttributes[4] = Integer.parseInt(attributes[4]);
            convertedAttributes[5] = Integer.parseInt(attributes[5]);
        } catch (NumberFormatException e) {
            String regex = Pattern.quote("For input string: ");
            String info = e.getMessage().replaceAll(regex, "");
            String message = "Nieudana konwersja danej: " + info;
            throw new DataFormatException(message);
        }
        return convertedAttributes;
    }

    private void checkNumberOfElements() {
        if(indexes.isEmpty()){
            String message = "Zbyt mała liczba elementów!";
            JOptionPane.showMessageDialog(null, message, "Patients Transport Center", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
    }

    public Collection<Hospital> getCollection() {
        return hospitalsByIndex.values();
    }
}
