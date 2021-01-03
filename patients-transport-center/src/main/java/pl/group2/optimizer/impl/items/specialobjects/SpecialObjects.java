package pl.group2.optimizer.impl.items.specialobjects;

import pl.group2.optimizer.impl.items.Items;
import pl.group2.optimizer.impl.items.hospitals.Hospital;

import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

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

    public SpecialObject getSpecialObjectByIndex(int index) {
        return specialObjectByIndex.get(index);
    }

    public boolean contain(int id) {
        return indexById.containsKey(id);
    }

    public int getSpecialObjectIndexById(int id) {
        return indexById.get(id);
    }

    public SpecialObject getSpecialObjectById(int id) {
        int index = indexById.get(id);
        return specialObjectByIndex.get(index);
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
    public void draw(Graphics g, int scalaX, int scalaY) {
        g.setColor(Color.RED);
        for (SpecialObject specialObject : getCollection()) {
            g.fillRect(specialObject.getXCoordinate() * scalaX + 75 - 5, 600 - (specialObject.getYCoordinate() * scalaY) - 75 - 5, 10, 10);
        }
    }
}
