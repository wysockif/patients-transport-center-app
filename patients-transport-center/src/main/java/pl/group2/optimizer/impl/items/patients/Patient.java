package pl.group2.optimizer.impl.items.patients;

import pl.group2.optimizer.impl.items.hospitals.Hospital;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class Patient {
    private final int id;
    private int xCoordinate;
    private int yCoordinate;

    private BufferedImage image;

    public Patient(int id, int xCoordinate, int yCoordinate) {
        this.id = id;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public void loadSprite(int type) {
        String path = String.format("/img/patients/%d.png", type);
        readSprite(path);
    }

    private void readSprite(String spritePath) {
        try {
            image = ImageIO.read(Hospital.class.getResource(spritePath));
        } catch (IOException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "Błąd krytyczny!\n" +
                    "Nie mogę znaleźć pliku z obrazem pocisku!", "Błąd krytyczny!", ERROR_MESSAGE);
            System.exit(2);
        }
    }


    public void setXCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public void setYCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public int getId() {
        return id;
    }

    public int getXCoordinate() {
        return xCoordinate;
    }

    public int getYCoordinate() {
        return yCoordinate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Patient patient = (Patient) o;
        return id == patient.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", x=" + xCoordinate +
                ", y=" + yCoordinate +
                '}';
    }

    public int getImageWidth() {
        return image.getWidth();
    }

    public int getImageHeight() {
        return image.getHeight();
    }

    public Image getImage() {
        return image;
    }
}
