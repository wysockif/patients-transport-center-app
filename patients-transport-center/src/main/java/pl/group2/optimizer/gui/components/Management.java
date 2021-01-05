package pl.group2.optimizer.gui.components;

import pl.group2.optimizer.Optimizer;
import pl.group2.optimizer.impl.io.MyException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.File;

import static java.awt.Color.DARK_GRAY;
import static java.awt.Color.GRAY;
import static java.awt.Color.GREEN;
import static java.awt.Color.WHITE;
import static javax.swing.JFileChooser.APPROVE_OPTION;
import static javax.swing.SwingConstants.CENTER;

public class Management extends JPanel {
    private boolean arePatientsAttached;
    private boolean isMapAttached;
    private final Optimizer optimizer;
    private JButton attachMapButton;
    private JButton attachPatientsButton;

    private final JButton runButton;
    private JLabel paceLabel;
    private JSlider paceSlider;

    public Management(Optimizer optimizer) {
        this.optimizer = optimizer;
        createTitle();
        createMapElements();
        createPatientsElements();
        createSliderElements();
        runButton = createRunButton();
        setSize(250, 400);
        setLocation(950, 0);
        setLayout(null);
    }

    private JButton createRunButton() {
        JButton button = new JButton("Uruchom symulację");
        button.setBounds(30, 320, 175, 40);
        button.setBackground(DARK_GRAY);
        button.setForeground(WHITE);
        button.addActionListener(e -> {
            optimizer.run();
            button.setEnabled(false);
            button.setText("Uruchomiono symulację");
        });
        button.setFocusable(false);
        button.setEnabled(false);
        add(button);
        return button;
    }

    private void createSliderElements() {
        paceLabel = new JLabel("Tempo:", CENTER);
        paceLabel.setFont(new Font("TitleFont", Font.BOLD, 14));
        paceLabel.setBounds(0, 220, 235, 20);
        add(paceLabel);

        paceSlider = new JSlider(0, 100, 50);
        paceSlider.setBounds(20, 245, 195, 50);
        paceSlider.setBackground(GRAY);
        paceSlider.setForeground(DARK_GRAY);
        paceSlider.setMajorTickSpacing(25);
        paceSlider.setMinorTickSpacing(5);
        paceSlider.setPaintTicks(true);
        paceSlider.setPaintLabels(true);
        add(paceSlider);
    }

    private void createPatientsElements() {
        JLabel attachPatientsLabel = new JLabel("Załącz plik z pacjentami:", CENTER);
        attachPatientsLabel.setFont(new Font("TitleFont", Font.BOLD, 14));
        attachPatientsLabel.setBounds(0, 140, 235, 20);
        add(attachPatientsLabel);

        attachPatientsButton = new JButton("Załącz");
        attachPatientsButton.setBounds(40, 165, 155, 40);
        attachPatientsButton.addActionListener(e -> attachPatients());
        attachPatientsButton.setFocusable(false);
        attachPatientsButton.setBackground(DARK_GRAY);
        attachPatientsButton.setForeground(WHITE);
        add(attachPatientsButton);
    }

    private void createMapElements() {
        JLabel attachMapLabel = new JLabel("Załącz plik z mapą:", CENTER);
        attachMapLabel.setFont(new Font("TitleFont", Font.BOLD, 14));
        attachMapLabel.setBounds(0, 60, 235, 20);
        add(attachMapLabel);

        attachMapButton = new JButton("Załącz");
        attachMapButton.setBounds(40, 85, 155, 40);
        attachMapButton.addActionListener(e -> attachMap());
        attachMapButton.setFocusable(false);
        attachMapButton.setBackground(DARK_GRAY);
        attachMapButton.setForeground(WHITE);
        add(attachMapButton);
    }

    private void attachPatients() {
        try {
            optimizer.loadPatients(getPathFromFileChooser());

            JOptionPane.showMessageDialog(null, optimizer.messageAboutDownloadedPatients(),
                    "Patients Transport Center", JOptionPane.INFORMATION_MESSAGE);

            attachPatientsButton.setForeground(GREEN.darker());
            attachPatientsButton.setText("Załączono");
            attachPatientsButton.setEnabled(false);
            arePatientsAttached = true;
            optimizer.getPatientsManagement().enablePanel();
            checkIfBothFilesAreAttached();
        } catch (MyException myException) {
            myException.printStackTrace();
        }
    }

    private void attachMap() {
        try {
            optimizer.loadMap(getPathFromFileChooser());

            JOptionPane.showMessageDialog(null, optimizer.messageAboutDownloadedMap(),
                    "Patients Transport Center", JOptionPane.INFORMATION_MESSAGE);

            attachMapButton.setForeground(GREEN.darker());
            attachMapButton.setText("Załączono");
            attachMapButton.setEnabled(false);
            isMapAttached = true;
            checkIfBothFilesAreAttached();
        } catch (MyException myException) {
            myException.printStackTrace();
        }
    }

    private void checkIfBothFilesAreAttached() {
        if (isMapAttached && arePatientsAttached) {
            runButton.setEnabled(true);
        }
    }

    private String getPathFromFileChooser() {
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File("/"));
        if (fc.showOpenDialog(null) == APPROVE_OPTION) {
            return fc.getSelectedFile().getPath();
        }
        return "";
    }

    private JLabel createTitle() {
        JLabel jLabel = new JLabel("PANEL ZARZĄDZANIA", CENTER);
        jLabel.setFont(new Font("TitleFont", Font.BOLD, 20));
        jLabel.setBounds(0, 0, 235, 70);
        add(jLabel);
        return jLabel;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.gray);
        g.fillRect(0, 0, 250, 400);
        g.setColor(new Color(99, 99, 99));
        g.fillRect(0, 385, 250, 5);
        g.fillRect(0, 395, 250, 5);

    }
}
