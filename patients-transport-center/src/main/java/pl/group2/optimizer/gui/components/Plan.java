package pl.group2.optimizer.gui.components;

import pl.group2.optimizer.Optimizer;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;

public class Plan extends JPanel {
    public static final int WIDTH = 950;
    public static final int HEIGHT = 650;
    private final Optimizer optimizer;
    private double scalaX;
    private double scalaY;
    private boolean running;

    public Plan(Optimizer optimizer) {
        this.optimizer = optimizer;
        running = false;
        setSize(WIDTH, HEIGHT);
        setLocation(0, 0);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(new Color(0, 70, 0));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.BLACK);
        if (running) {
            optimizer.getPatients().draw(g, scalaX, scalaY);
            optimizer.getHospitals().draw(g, scalaX, scalaY);
            optimizer.getSpecialObjects().draw(g, scalaX, scalaY);
        } else {
            g.drawString("ZAŁADUJ DANE", 400, 300);
        }
    }

    public void setScales(double scalaX, double scalaY) {
        this.scalaX = scalaX;
        this.scalaY = scalaY;
    }

    public void setRunning(boolean isRunning) {
        running = isRunning;
    }
}
