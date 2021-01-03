package pl.group2.optimizer.gui.components;

import pl.group2.optimizer.Optimizer;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;

public class Plan extends JPanel {
    public static final int WIDTH = 950;
    public static final int HEIGHT = 650;
    private final Optimizer optimizer;
    private int scalaX;
    private int scalaY;
    private boolean running;

    public Plan(Optimizer optimizer) {
        this.optimizer = optimizer;
        running = false;
        setSize(WIDTH, HEIGHT);
        setLocation(0, 0);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(new Color(4, 88, 4));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.BLACK);
        if (running) {
            optimizer.getHandledArea().draw(g, scalaX, scalaY);
            optimizer.getPaths().draw(g, scalaX, scalaY);
            optimizer.getHospitals().draw(g, scalaX, scalaY);
            optimizer.getSpecialObjects().draw(g, scalaX, scalaY);
            optimizer.getPatients().draw(g, scalaX, scalaY);
        } else {
            g.setFont( g.getFont().deriveFont( 20.0f ) );
            g.drawString("ZAŁADUJ DANE", 400, 300);
        }
    }

    public void setScales(int scalaX, int scalaY) {
        this.scalaX = scalaX;
        this.scalaY = scalaY;
    }

    public void runSimulation() {
        running = true;
        Timer time = new Timer();
        time.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        }, 0, 100);
    }
}
