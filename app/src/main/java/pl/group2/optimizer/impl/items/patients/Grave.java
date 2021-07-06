package pl.group2.optimizer.impl.items.patients;

import pl.group2.optimizer.impl.items.hospitals.Hospital;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class Grave {
    private final int x;
    private final int y;

    private BufferedImage image;

    public Grave(int x, int y) {
        this.x = x;
        this.y = y;
        readSprite("/img/grave/1.png");
    }


    private void readSprite(String spritePath) {
        try {
            image = ImageIO.read(Hospital.class.getResource(spritePath));
        } catch (IOException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "Błąd krytyczny!\n" +
                    "Nie mogę znaleźć pliku z obrazem grobu!", "Błąd krytyczny!", ERROR_MESSAGE);
            System.exit(2);
        }
    }

    public int getXCoordinate() {
        return x;
    }

    public int getYCoordinate() {
        return y;
    }

    public BufferedImage getImage() {
        return image;
    }
}
