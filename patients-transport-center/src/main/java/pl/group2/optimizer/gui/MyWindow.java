package pl.group2.optimizer.gui;

import pl.group2.optimizer.Optimizer;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MyWindow extends JFrame {

    private JPanel panel;
    private Optimizer optimizer;

    public MyWindow(Optimizer optimizer) {
        super("Patients Transport Center");
        this.optimizer = optimizer;

        panel = new MyPanel(optimizer);
        setSize(panel.getSize());
        setContentPane(panel);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void drawHospitals() {
        Graphics g = panel.getGraphics();
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("images/hospital1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        g.drawImage(image, 20, 20, null);
    }
}