package pl.group2.optimizer.impl.items.paths;

import pl.group2.optimizer.impl.items.Items;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

public class Paths implements Items {
    private final List<Path> paths;

    public Paths() {
        paths = new LinkedList<>();
    }

    public void addNewPath(Path path) {
        checkIfArgumentIsNull(path);
        paths.add(path);
    }

    public boolean contains(Path path) {
        checkIfArgumentIsNull(path);
        return paths.contains(path);
    }

    public int size() {
        return paths.size();
    }

    private void checkIfArgumentIsNull(Object argument) {
        if (argument == null) {
            throw new IllegalArgumentException("Niezainicjowany argument");
        }
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
            String message = "Niepoprawny format danych. Ujemna wartość reprezentująca id specjalnego obiektu.";
            throw new DataFormatException(message);
        }
//        if (contains(id)) {
//            throw new DataFormatException("Nie można dodawać specjalnych obiektów o tym samym id.");
//        }
    }

    @Override
    public void addNewElement(Object[] attributes) {
        checkIfArgumentIsNotNull(attributes);
        int id = (int) attributes[0];
        int from = (int) attributes[1];
        int to = (int) attributes[2];
        int distance = (int) attributes[3];
        paths.add(new Path(id, from, to, distance));
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
            convertedAttributes[3] = Integer.parseInt(attributes[3]);
        } catch (NumberFormatException e) {
            String regex = Pattern.quote("For input string: ");
            String info = e.getMessage().replaceAll(regex, "");
            String message = "Nieudana konwersja danej: " + info;
            throw new DataFormatException(message);
        }
        return convertedAttributes;
    }
}
