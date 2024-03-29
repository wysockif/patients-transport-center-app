package pl.group2.optimizer.impl.items.hospitals;

import pl.group2.optimizer.gui.sprites.Sprite;
import pl.group2.optimizer.impl.items.Vertex;
import pl.group2.optimizer.impl.items.area.Point;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class Hospital extends Vertex implements Point, Sprite {
    private final String name;
    private final int numberOfBeds;
    private int numberOfAvailableBeds;

    private BufferedImage image;

    public Hospital(int id, String name, int xCoordinate, int yCoordinate, int numberOfBeds, int numberOfAvailableBeds) {
        super(id, xCoordinate, yCoordinate);
        this.name = name;
        this.numberOfBeds = numberOfBeds;
        this.numberOfAvailableBeds = numberOfAvailableBeds;
    }

    public void loadSprite(int type) {
        String path = String.format("/img/hospitals/hospital%d.png", type);
        readSprite(path);
        image = scale(image, 0.3);
    }

    private void readSprite(String spritePath) {
        try {
            image = ImageIO.read(Hospital.class.getResource(spritePath));
        } catch (IOException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "Błąd krytyczny!\n" +
                    "Nie mogę znaleźć pliku z obrazem szpitala!", "Błąd krytyczny!", ERROR_MESSAGE);
            System.exit(2);
        }
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public int getImageWidth() {
        return image.getWidth();
    }

    public int getImageHeight() {
        return image.getHeight();
    }

    public String getName() {
        return name;
    }

    public int getNumberOfAvailableBeds() {
        return numberOfAvailableBeds;
    }

    public BufferedImage getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "Hospital{" +
                "name='" + name + '\'' +
                ", numberOfBeds=" + numberOfBeds +
                ", numberOfAvailableBeds=" + numberOfAvailableBeds +
                ", image=" + image +
                '}';
    }

    public void decreaseNumberOfAvailableBeds() {
        numberOfAvailableBeds--;
    }

}
