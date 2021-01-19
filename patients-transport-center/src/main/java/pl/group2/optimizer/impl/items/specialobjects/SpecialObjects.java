package pl.group2.optimizer.impl.items.specialobjects;

import pl.group2.optimizer.impl.items.Items;

import java.awt.Graphics;
import java.awt.Image;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

import static pl.group2.optimizer.gui.components.Plan.HEIGHT;
import static pl.group2.optimizer.gui.components.Plan.MARGIN;
import static pl.group2.optimizer.gui.components.Plan.PADDING;

public class SpecialObjects implements Items {
    private final Map<Integer, SpecialObject> specialObjectByIndex;
    private final Map<Integer, Integer> indexById;
    private int counter;


    public int size() {
        return specialObjectByIndex.size();
    }

    public SpecialObjects() {
        specialObjectByIndex = new HashMap<>();
        indexById = new HashMap<>();
    }

    public void add(SpecialObject specialObject) {
        int id = specialObject.getId();
        specialObjectByIndex.put(counter, specialObject);
        indexById.put(id, counter);
        counter++;
    }

    public boolean contain(int id) {
        return indexById.containsKey(id);
    }

    @Override
    public Object[] convertAttributes(String[] attributes) throws DataFormatException {
        checkIfArgumentIsNotNull(attributes);
        if (attributes.length != 4) {
            throw new DataFormatException("Niepoprawny format danych");
        }
        return parseAttributes(attributes);
    }

    @Override
    public void validateAttributes(Object[] attributes) throws DataFormatException {
        checkIfArgumentIsNotNull(attributes);
        int id = (int) attributes[0];

        if (id < 0) {
            String message = "Niepoprawny format danych. Ujemna wartość reprezentująca id specjalnego obiektu";
            throw new DataFormatException(message);
        }
        if (contain(id)) {
            throw new DataFormatException("Nie można dodawać specjalnych obiektów o tym samym id.");
        }

    }

    @Override
    public void addNewElement(Object[] attributes) {
        checkIfArgumentIsNotNull(attributes);
        int id = (int) attributes[0];
        String name = (String) attributes[1];
        int x = (int) attributes[2];
        int y = (int) attributes[3];
        add(new SpecialObject(id, name, x, y));
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
        } catch (NumberFormatException e) {
            String regex = Pattern.quote("For input string: ");
            String info = e.getMessage().replaceAll(regex, "");
            String message = "Nieudana konwersja danej: " + info;
            throw new DataFormatException(message);
        }
        return convertedAttributes;
    }

    public Collection<SpecialObject> getCollection() {
        return specialObjectByIndex.values();
    }


    @Override
    public void draw(Graphics g, double scalaX, double scalaY, int minX, int minY) {
        for (SpecialObject specialObject : getCollection()) {
            Image image = specialObject.getImage();

            int xShift = specialObject.getImageWidth() / 2;
            int yShift = specialObject.getImageHeight() / 2;

            int x = (int) Math.round(PADDING + specialObject.getXCoordinate() * scalaX + MARGIN - xShift - minX * scalaX);
            int y = (int) Math.round(PADDING + HEIGHT - (specialObject.getYCoordinate() * scalaY) - MARGIN - yShift + minY * scalaY);

            g.drawImage(image, x, y, null);
        }
    }
}
