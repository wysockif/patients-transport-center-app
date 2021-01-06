package pl.group2.optimizer.gui.components;

import pl.group2.optimizer.Optimizer;
import pl.group2.optimizer.impl.io.MyException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
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
import static java.awt.Font.BOLD;
import static javax.swing.JFileChooser.APPROVE_OPTION;
import static javax.swing.SwingConstants.CENTER;

public class Management extends JPanel {
    private boolean arePatientsAttached;
    private boolean isMapAttached;
    private final Optimizer optimizer;
    private JButton attachMapButton;
    private JButton attachPatientsButton;

    private final JButton runButton;
    private JSlider paceSlider;
    private JButton defaultDataButton;

    public Management(Optimizer optimizer) {
        this.optimizer = optimizer;
        createTitle();
        createMapElements();
        createPatientsElements();
        createSliderElements();
        runButton = createRunButton();
        defaultDataButton = createRunWithDefaultButton();

        setSize(250, 420);
        setLocation(950, 0);
        setLayout(null);
    }

    private JButton createRunWithDefaultButton() {
        JLabel attachPatientsLabel = new JLabel("Załącz dane przykładowe:", CENTER);
        attachPatientsLabel.setFont(new Font("TitleFont", BOLD, 14));
        attachPatientsLabel.setBounds(0, 195, 235, 20);
        add(attachPatientsLabel);

        defaultDataButton = new JButton("Załącz");
        defaultDataButton.setBounds(40, 215, 155, 35);
        defaultDataButton.setBackground(DARK_GRAY);
        defaultDataButton.setForeground(WHITE);
        defaultDataButton.addActionListener(e -> {
            defaultDataButton.setEnabled(false);
            defaultDataButton.setText("Załączono");
            try {
                optimizer.qloadMap("/data/map.txt");
                optimizer.loadPatients("/data/patients.txt");
            } catch (MyException myException) {
                myException.printStackTrace();
            }
            optimizer.showMap();
            isMapAttached = true;
            attachMapButton.setEnabled(false);
            arePatientsAttached = true;
            attachPatientsButton.setEnabled(false);
            optimizer.getPatientsManagement().enablePanel();
            runButton.setEnabled(true);
        });
        defaultDataButton.setFocusable(false);
        add(defaultDataButton);
        return defaultDataButton;
    }

    private JButton createRunButton() {
        JLabel attachPatientsLabel = new JLabel("Uruchom symulację:", CENTER);
        attachPatientsLabel.setFont(new Font("TitleFont", BOLD, 14));
        attachPatientsLabel.setBounds(0, 260, 235, 20);
        add(attachPatientsLabel);

        JButton button = new JButton("Uruchom");
        button.setBounds(40, 280, 155, 35);
        button.setBackground(DARK_GRAY);
        button.setForeground(GREEN.darker());
        button.addActionListener(e -> {
            optimizer.run();
            button.setEnabled(false);
            button.setText("Uruchomiono");
        });
        button.setFocusable(false);
        button.setEnabled(false);
        add(button);
        return button;
    }

    private void createSliderElements() {
        JLabel paceLabel = new JLabel("Tempo:", CENTER);
        paceLabel.setFont(new Font("TitleFont", BOLD, 14));
        paceLabel.setBounds(0, 340, 235, 20);
        add(paceLabel);

        paceSlider = new JSlider(0, 100, 50);
        paceSlider.setBounds(20, 360, 195, 50);
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
        attachPatientsLabel.setFont(new Font("TitleFont", BOLD, 14));
        attachPatientsLabel.setBounds(0, 125, 235, 20);
        add(attachPatientsLabel);

        attachPatientsButton = new JButton("Załącz");
        attachPatientsButton.setBounds(40, 145, 155, 35);
        attachPatientsButton.addActionListener(e -> attachPatients());
        attachPatientsButton.setFocusable(false);
        attachPatientsButton.setBackground(DARK_GRAY);
        attachPatientsButton.setForeground(WHITE);
        add(attachPatientsButton);
    }

    private void createMapElements() {
        JLabel attachMapLabel = new JLabel("Załącz plik z mapą:", CENTER);
        attachMapLabel.setFont(new Font("TitleFont", BOLD, 14));
        attachMapLabel.setBounds(0, 55, 235, 20);
        add(attachMapLabel);

        attachMapButton = new JButton("Załącz");
        attachMapButton.setBounds(40, 75, 155, 35);
        attachMapButton.addActionListener(e -> attachMap());
        attachMapButton.setFocusable(false);
        attachMapButton.setBackground(DARK_GRAY);
        attachMapButton.setForeground(WHITE);
        add(attachMapButton);
    }

    private void attachPatients() {
        try {
            optimizer.loadPatients(getPathFromFileChooser());

//            JOptionPane.showMessageDialog(null, optimizer.messageAboutDownloadedPatients(),
//                    "Patients Transport Center", JOptionPane.INFORMATION_MESSAGE);

            attachPatientsButton.setForeground(GREEN.darker());
            attachPatientsButton.setText("Załączono");
            attachPatientsButton.setEnabled(false);
            defaultDataButton.setEnabled(false);
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
            attachMapButton.setForeground(GREEN.darker());
            attachMapButton.setText("Załączono");
            attachMapButton.setEnabled(false);
            defaultDataButton.setEnabled(false);
            isMapAttached = true;
            checkIfBothFilesAreAttached();
            optimizer.showMap();
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

    private void createTitle() {
        JLabel jLabel = new JLabel("PANEL ZARZĄDZANIA", CENTER);
        jLabel.setFont(new Font("TitleFont", BOLD, 20));
        jLabel.setBounds(0, 0, 235, 70);
        add(jLabel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(GRAY);
        g.fillRect(0, 0, 250, 420);
        g.setColor(new Color(99, 99, 99));
        g.fillRect(0, 335, 250, 5);
        g.fillRect(0, 415, 250, 5);

    }
}
