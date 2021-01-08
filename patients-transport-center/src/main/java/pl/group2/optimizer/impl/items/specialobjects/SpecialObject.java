package pl.group2.optimizer.impl.items.specialobjects;

import pl.group2.optimizer.gui.sprites.Sprite;
import pl.group2.optimizer.impl.items.area.Point;
import pl.group2.optimizer.impl.items.hospitals.Hospital;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class SpecialObject implements Point, Sprite {
    public static final String SPRITE_PATH = "/img/monument/monument.png";

    private final int id;
    private final String name;

    private final int xCoordinate;
    private final int yCoordinate;
    private BufferedImage image;

    public SpecialObject(int id, String name, int xCoordinate, int yCoordinate) {
        this.id = id;
        this.name = name;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        loadSprite();
    }

    private void loadSprite() {
        try {
            image = ImageIO.read(SpecialObject.class.getResource(SPRITE_PATH));
        } catch (IOException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "Błąd krytyczny!\n" +
                    "Nie mogę znaleźć pliku z obrazem obiektu specjalnego!", "Błąd krytyczny!", ERROR_MESSAGE);
            System.exit(2);
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public int getXCoordinate() {
        return xCoordinate;
    }

    @Override
    public int getYCoordinate() {
        return yCoordinate;
    }

    @Override
    public String toString() {
        return "(" + xCoordinate +
                "," + yCoordinate +
                ')';
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    public int getImageWidth() {
        return image.getWidth();
    }

    public int getImageHeight() {
        return image.getHeight();
    }
}
