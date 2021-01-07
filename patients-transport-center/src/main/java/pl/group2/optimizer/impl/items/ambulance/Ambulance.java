package pl.group2.optimizer.impl.items.ambulance;

import pl.group2.optimizer.impl.items.hospitals.Hospital;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class Ambulance {
    private double x;
    private double y;
    private int patientId;

    private BufferedImage currentImage;

    private BufferedImage imageRightBlue;
    private BufferedImage imageRightRed;
    private BufferedImage imageLeftBlue;
    private BufferedImage imageLeftRed;

    public Ambulance(int patientId, double x, double y) {
        this.patientId = patientId;
        this.x = x;
        this.y = y;
        loadImages();
    }

    private void loadImages() {
        imageRightBlue = readSprite("/img/ambulance/right-blue.png");
        imageRightRed = readSprite("/img/ambulance/right-red.png");
        imageLeftBlue = readSprite("/img/ambulance/left-blue.png");
        imageLeftRed = readSprite("/img/ambulance/left-red.png");
        currentImage = imageRightBlue;
    }

    private BufferedImage readSprite(String spritePath) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(Hospital.class.getResource(spritePath));
        } catch (IOException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "Błąd krytyczny!\n" +
                    "Nie mogę znaleźć pliku z obrazem karetki!", "Błąd krytyczny!", ERROR_MESSAGE);
            System.exit(2);
        }
        return image;
    }

    public void flash() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (currentImage == imageRightBlue) {
                    currentImage = imageRightRed;
                } else if (currentImage == imageRightRed) {
                    currentImage = imageRightBlue;
                } else if (currentImage == imageLeftBlue) {
                    currentImage = imageLeftRed;
                } else if (currentImage == imageLeftRed) {
                    currentImage = imageLeftBlue;
                }
            }
        }, 0, 250);
    }

    public int getImageWidth() {
        return currentImage.getWidth();
    }

    public int getImageHeight(){
        return currentImage.getHeight();
    }

    public double getXCoordinate() {
        return x;
    }

    public double getYCoordinate(){
        return y;
    }

    public BufferedImage getCurrentImage() {
        return currentImage;
    }

    public void setXCoordinate(double x) {
        this.x = x;
    }

    public void setYCoordinate(double y) {
        this.y = y;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setRightSprite() {
        currentImage = imageRightBlue;
    }

    public void setLeftSprite(){
        currentImage = imageLeftBlue;
    }
}
