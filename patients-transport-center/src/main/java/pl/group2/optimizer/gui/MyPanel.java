package pl.group2.optimizer.gui;

import pl.group2.optimizer.Optimizer;
import pl.group2.optimizer.impl.io.MyException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.io.File;

import static javax.swing.JFileChooser.APPROVE_OPTION;

public class MyPanel extends JPanel {
    private Optimizer optimizer;
    private JButton loadMapButton;
    private JButton loadPatientsButton;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.repaint();
    }

    public MyPanel(Optimizer optimizer) {
        this.optimizer = optimizer;

        setButtons();

        setSize(640, 480);

        setLoadMapButton();
        setLoadPatientsButton();

        addButtonsToPanel();
    }

    private void setButtons() {
        loadMapButton = new JButton("Wczytaj mapę");
        loadPatientsButton = new JButton("Wczytaj pacjentów");
    }

    private void addButtonsToPanel() {
        add(loadMapButton);
        add(loadPatientsButton);
    }

    private void setLoadPatientsButton() {
        loadPatientsButton.addActionListener(e -> {
            try {
                optimizer.loadPatients(getPathFromFileChooser());
                optimizer.checkIfPatientsWereDownloaded();
            } catch (MyException myException) {
                myException.printStackTrace();
            }
        });
    }

    private void setLoadMapButton() {
        loadMapButton.addActionListener(e -> {
            try {
                optimizer.loadMap(getPathFromFileChooser());
                optimizer.checkIfMapWasDownloaded();
            } catch (MyException myException) {
                myException.printStackTrace();
            }
        });
    }

    private String getPathFromFileChooser() {
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File("D:/"));
        if (fc.showOpenDialog(null) == APPROVE_OPTION) {
            return fc.getSelectedFile().getPath();
        }
        return "";
    }
}