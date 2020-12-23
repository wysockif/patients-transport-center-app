package pl.group2.optimizer.gui;

import pl.group2.optimizer.Optimizer;
import pl.group2.optimizer.impl.io.MyException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.io.File;

import static javax.swing.JFileChooser.APPROVE_OPTION;

public class MyWindow extends JFrame {
    private JButton loadMapButton;
    private JButton loadPatientsButton;
    private JPanel panel;
    private Optimizer optimizer;

    public MyWindow(Optimizer optimizer) {
        super("Patients Transport Center");
        this.optimizer = optimizer;

        panel = new JPanel();
        loadMapButton = new JButton("Wczytaj mapę");
        loadPatientsButton = new JButton("Wczytaj pacjentów");
        panel.setSize(640, 480);
        setSize(panel.getSize());

        setLoadMapButton();
        setLoadPatientsButton();

        panel.add(loadMapButton);
        panel.add(loadPatientsButton);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setContentPane(panel);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private String getPathFromFileChooser() {
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File("D:/"));
        if (fc.showOpenDialog(null) == APPROVE_OPTION) {
            return fc.getSelectedFile().getPath();
        }
        return "";
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
}
