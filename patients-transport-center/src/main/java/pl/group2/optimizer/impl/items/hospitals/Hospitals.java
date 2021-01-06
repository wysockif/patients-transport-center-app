package pl.group2.optimizer.impl.items.hospitals;

import pl.group2.optimizer.impl.items.Items;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

import static pl.group2.optimizer.gui.components.Plan.HEIGHT;
import static pl.group2.optimizer.gui.components.Plan.MARGIN;
import static pl.group2.optimizer.gui.components.Plan.PADDING;

public class Hospitals implements Items {
    private final Map<Integer, Hospital> hospitalsByIndex;
    private final Map<Integer, Integer> indexById;
    private int counter;

    public Hospitals() {
        hospitalsByIndex = new HashMap<>();
        indexById = new HashMap<>();
    }

    public int size() {
        return hospitalsByIndex.size();
    }

    public void add(Hospital hospital) {
        int id = hospital.getId();
        hospitalsByIndex.put(counter, hospital);
        indexById.put(id, counter);
        counter++;
    }

    public Hospital getHospitalByIndex(int index) {
        return hospitalsByIndex.get(index);
    }

    public boolean contain(int id) {
        return indexById.containsKey(id);
    }

    public int getHospitalIndexById(int id) {
        return indexById.get(id);
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

    int numberOfMapElements;

    public void setNumberOfMapElements(int numberOfMapElements) {
        this.numberOfMapElements = numberOfMapElements;
    }

    @Override
    public void draw(Graphics g, double scalaX, double scalaY, int minX, int minY) {
        for (Hospital hospital : getCollection()) {
            int xShift = hospital.getImageWidth() / 2;
            int yShift = hospital.getImageHeight() / 2;

            int x = (int) Math.round(PADDING + hospital.getXCoordinate() * scalaX + MARGIN - xShift - minX * scalaX);
            int y = (int) Math.round(PADDING + HEIGHT - (hospital.getYCoordinate() * scalaY) - MARGIN - yShift + minY * scalaY);

            BufferedImage image = hospital.getImage();
            g.drawImage(image, x, y, null);
        }
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

    public Collection<Hospital> getCollection() {
        return hospitalsByIndex.values();
    }
}
