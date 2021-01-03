package pl.group2.optimizer.impl.items.paths;

import pl.group2.optimizer.impl.items.Items;
import pl.group2.optimizer.impl.items.hospitals.Hospitals;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

import static java.awt.Color.DARK_GRAY;
import static pl.group2.optimizer.gui.components.Plan.HEIGHT;
import static pl.group2.optimizer.gui.components.Plan.MARGIN;
import static pl.group2.optimizer.gui.components.Plan.PADDING;

public class Paths implements Items {
    private final List<Path> paths;
    private final Hospitals hospitals;

    public Paths(Hospitals hospitals) {
        paths = new LinkedList<>();
        this.hospitals = hospitals;
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

    public Path get(int index) {
        return paths.get(index);
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
        int distance = (int) attributes[3];
        int from = (int) attributes[1];
        int to = (int) attributes[2];

        if (id < 0) {
            String message = "Niepoprawny format danych. Ujemna wartość reprezentująca id drogi";
            throw new DataFormatException(message);
        }

        if (distance < 0) {
            String message = "Niepoprawny format danych. Ujemna wartość reprezentująca odległość drogi";
            throw new DataFormatException(message);
        }

        if (distance <= 0) {
            String message = "Niepoprawny format danych. Zerowa wartość reprezentująca odległość drogi";
            throw new DataFormatException(message);
        }

        if (from < 0) {
            String message = "Niepoprawny format danych. Ujemna wartość reprezentująca id szpitala w drogach";
            throw new DataFormatException(message);
        }

        if (to < 0) {
            String message = "Niepoprawny format danych. Ujemna wartość reprezentująca id szpitala w drogach";
            throw new DataFormatException(message);
        }

//        if (contains(id)) {
//            throw new DataFormatException("Nie można dodawać specjalnych obiektów o tym samym id.");
//        }

//        if (containsHospitals(id)) {
//            throw new DataFormatException("Nie można dodawać drogi, która łączy te same szpitale.");
//        }
    }

    @Override
    public void addNewElement(Object[] attributes) {
        checkIfArgumentIsNotNull(attributes);
        int id = (int) attributes[0];
        int from = (int) attributes[1];
        int to = (int) attributes[2];
        int distance = (int) attributes[3];

        paths.add(new Path(id, hospitals.getHospitalById(from), hospitals.getHospitalById(to), distance));
    }

    @Override
    public void draw(Graphics g, double scalaX, double scalaY, int minX, int minY) {
        for (Path path : paths) {
            g.setColor(DARK_GRAY);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(6.0F));

            int xFrom = path.getFrom().getXCoordinate();
            int yFrom = path.getFrom().getYCoordinate();
            int xTo = path.getTo().getXCoordinate();
            int yTo = path.getTo().getYCoordinate();

            int x1 = (int) Math.round(PADDING - minX * scalaX + xFrom * scalaX + MARGIN);
            int y1 = (int) Math.round(PADDING + minY * scalaY + HEIGHT - (yFrom * scalaY) - MARGIN);
            int x2 = (int) Math.round(PADDING - minX * scalaX + xTo * scalaX + MARGIN);
            int y2 = (int) Math.round(PADDING + minY * scalaY + HEIGHT - (yTo * scalaY) - MARGIN);
            g.drawLine(x1, y1, x2, y2);
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
