package pl.group2.optimizer.gui.components;

import pl.group2.optimizer.Optimizer;
import pl.group2.optimizer.gui.MouseClickListener;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;

public class Plan extends JPanel {
    public static final int WIDTH = 950;
    public static final int HEIGHT = 650;
    public static final int MARGIN = 50;
    public static final int PADDING = 0;

    private final Optimizer optimizer;
    private double scalaX;
    private double scalaY;
    private int minX;
    private int minY;
    private boolean running;

    public Plan(Optimizer optimizer) {
        this.optimizer = optimizer;
        running = false;
        setSize(WIDTH, HEIGHT);
        setLocation(0, 0);
        addMouseListener(new MouseClickListener(optimizer));
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(new Color(4, 88, 4));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.BLACK);
        if (running) {
            optimizer.getHandledArea().draw(g, scalaX, scalaY);
            optimizer.getPaths().draw(g, scalaX, scalaY, minX, minY);
            optimizer.getIntersections().draw(g, scalaX, scalaY, minX, minY);
            optimizer.getSpecialObjects().draw(g, scalaX, scalaY, minX, minY);
            if (optimizer.getPatients() != null) {
                optimizer.getPatients().draw(g, scalaX, scalaY, minX, minY);
            }
            if (optimizer.getAmbulanceService() != null) {
                optimizer.getAmbulanceService().drawAmbulance(g, scalaX, scalaY, minX, minY);
                optimizer.getAmbulanceService().drawDeadPatients(g, scalaX, scalaY, minX, minY);
            }
            optimizer.getHospitals().draw(g, scalaX, scalaY, minX, minY);
        } else {
            g.setFont(g.getFont().deriveFont(20.0f));
            g.drawString("ZAŁADUJ DANE", 400, 300);
        }


    }

    public void setProperties(double scalaX, double scalaY, int minX, int minY) {
        this.scalaX = scalaX;
        this.scalaY = scalaY;
        this.minX = minX;
        this.minY = minY;
    }

    public void showMap() {
        running = true;
        double framesPerSecond = 60;
        double millisecondsInSecond = 1000;

        Timer time = new Timer();
        time.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        }, 0, (long) (millisecondsInSecond / framesPerSecond));
    }
}
