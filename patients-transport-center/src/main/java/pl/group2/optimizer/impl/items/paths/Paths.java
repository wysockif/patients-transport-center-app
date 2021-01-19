package pl.group2.optimizer.impl.items.paths;

import pl.group2.optimizer.impl.items.Items;
import pl.group2.optimizer.impl.items.hospitals.Hospitals;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

import static java.awt.Color.DARK_GRAY;
import static java.awt.Color.WHITE;
import static pl.group2.optimizer.gui.components.Plan.HEIGHT;
import static pl.group2.optimizer.gui.components.Plan.MARGIN;
import static pl.group2.optimizer.gui.components.Plan.PADDING;

public class Paths implements Items {
    private final List<Path> pathList;
    private final Hospitals hospitals;


    public Paths(Hospitals hospitals) {
        pathList = new LinkedList<>();
        this.hospitals = hospitals;
    }
    
    public boolean contains(Path path) {
        checkIfArgumentIsNotNull(path);
        return pathList.contains(path);
    }

    public int size() {
        return pathList.size();
    }

    public Path get(int index) {
        return pathList.get(index);
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

        validateId(id);
        validateDistance(distance);
        validateNegativeNumbers(from, to);
        verifyIfPathExists(from, to);
    }

    private void verifyIfPathExists(int from, int to) throws DataFormatException {
        if (exists(from, to) || exists(to, from)) {
            throw new DataFormatException("Nie można dodawać drogi, która łączy te same szpitale.");
        }
    }

    private void validateNegativeNumbers(int from, int to) throws DataFormatException {
        if (from < 0) {
            String message = "Niepoprawny format danych. Ujemna wartość reprezentująca id szpitala w drogach";
            throw new DataFormatException(message);
        }
        if (to < 0) {
            String message = "Niepoprawny format danych. Ujemna wartość reprezentująca id szpitala w drogach";
            throw new DataFormatException(message);
        }
    }

    private void validateId(int id) throws DataFormatException {
        if (id < 0) {
            String message = "Niepoprawny format danych. Ujemna wartość reprezentująca id drogi";
            throw new DataFormatException(message);
        }
        if (containsId(id)) {
            throw new DataFormatException("Nie można dodawać dróg o tym samym id.");
        }
    }

    private void validateDistance(int distance) throws DataFormatException {
        if (distance <= 0) {
            String message = "Niepoprawny format danych. Niedodatnia wartość reprezentująca odległość drogi";
            throw new DataFormatException(message);
        }
    }

    private boolean exists(int from, int to) {
        for (Path path1 : pathList) {
            if (path1.getFrom().getId() == from && path1.getTo().getId() == to) {
                return true;
            }
        }
        return false;
    }

    private boolean containsId(int id) {
        for (Path path : pathList) {
            if (path.getId() == id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addNewElement(Object[] attributes) {
        checkIfArgumentIsNotNull(attributes);
        int id = (int) attributes[0];
        int from = (int) attributes[1];
        int to = (int) attributes[2];
        int distance = (int) attributes[3];
        pathList.add(new Path(id, hospitals.getHospitalById(from), hospitals.getHospitalById(to), distance));
    }


    @Override
    public void draw(Graphics g, double scalaX, double scalaY, int minX, int minY) {
        for (Path path : pathList) {
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
            drawCenteredString(g, String.valueOf(path.getDistance()), x1, y1, x2, y2,
                    new Font("id-font", Font.PLAIN, 12));
        }
    }

    public void drawCenteredString(Graphics g, String text, int x1, int y1, int x2, int y2, Font font) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = (x1 + x2 - metrics.stringWidth(text)) / 2;
        int y = (y1 + y2 - metrics.getHeight()) / 2 + metrics.getAscent();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(WHITE);
        g2d.setFont(font);
        g2d.drawString(text, x, y);
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

    public List<Path> getList() {
        return pathList;
    }
}
