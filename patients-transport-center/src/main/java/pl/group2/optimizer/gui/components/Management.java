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
    private final Optimizer optimizer;
    private final JButton runButton;

    private boolean arePatientsAttached;
    private boolean isMapAttached;

    private JButton attachMapButton;
    private JButton attachPatientsButton;
    private JSlider paceSlider;

    public Management(Optimizer optimizer) {
        this.optimizer = optimizer;
        createTitle();
        createMapElements();
        createPatientsElements();
        createSliderElements();
        runButton = createRunButton();
        setSize(250, 420);
        setLocation(950, 0);
        setLayout(null);
    }

    private JButton createRunButton() {
        JLabel attachPatientsLabel = new JLabel("Uruchom symulację:", CENTER);
        attachPatientsLabel.setFont(new Font("TitleFont", BOLD, 14));
        attachPatientsLabel.setBounds(0, 220, 235, 20);
        add(attachPatientsLabel);

        JButton button = new JButton("Uruchom");
        button.setBounds(40, 240, 155, 35);
        button.setBackground(DARK_GRAY);
        button.setForeground(GREEN.darker());
        button.addActionListener(e -> run(button));
        button.setFocusable(false);
        button.setEnabled(false);
        add(button);
        return button;
    }

    private void run(JButton button) {
        optimizer.run();
        button.setEnabled(false);
        button.setText("Uruchomiono");
        optimizer.changePeriod(paceSlider.getValue());
    }


    private void createSliderElements() {
        JLabel paceLabel = new JLabel("Tempo:", CENTER);
        paceLabel.setFont(new Font("TitleFont", BOLD, 14));
        paceLabel.setBounds(0, 340, 235, 20);
        add(paceLabel);

        paceSlider = new JSlider(1, 7, 4);
        paceSlider.setBounds(20, 360, 195, 50);
        paceSlider.setBackground(GRAY);
        paceSlider.setForeground(DARK_GRAY);
        paceSlider.setMajorTickSpacing(1);
        paceSlider.setMinorTickSpacing(1);
        paceSlider.setPaintTicks(true);
        paceSlider.setPaintLabels(true);
        paceSlider.addChangeListener(e -> optimizer.changePeriod(paceSlider.getValue()));
        add(paceSlider);
    }

    private void createPatientsElements() {
        JLabel attachPatientsLabel = new JLabel("Załącz plik z pacjentami:", CENTER);
        attachPatientsLabel.setFont(new Font("TitleFont", BOLD, 14));
        attachPatientsLabel.setBounds(0, 150, 235, 20);
        add(attachPatientsLabel);

        attachPatientsButton = new JButton("Załącz");
        attachPatientsButton.setBounds(40, 170, 155, 35);
        attachPatientsButton.addActionListener(e -> attachPatients());
        attachPatientsButton.setFocusable(false);
        attachPatientsButton.setBackground(DARK_GRAY);
        attachPatientsButton.setForeground(WHITE);
        add(attachPatientsButton);
    }

    private void createMapElements() {
        JLabel attachMapLabel = new JLabel("Załącz plik z mapą:", CENTER);
        attachMapLabel.setFont(new Font("TitleFont", BOLD, 14));
        attachMapLabel.setBounds(0, 80, 235, 20);
        add(attachMapLabel);

        attachMapButton = new JButton("Załącz");
        attachMapButton.setBounds(40, 100, 155, 35);
        attachMapButton.addActionListener(e -> attachMap());
        attachMapButton.setFocusable(false);
        attachMapButton.setBackground(DARK_GRAY);
        attachMapButton.setForeground(WHITE);
        add(attachMapButton);
    }

    private void attachPatients() {
        String path = getPathFromFileChooser();
        try {
            if (!path.equals("")) {
                optimizer.loadPatients(path);
                attachPatientsButton.setForeground(GREEN.darker());
                attachPatientsButton.setText("Załączono");
                optimizer.getPatientsManagement().enablePanel();
                attachPatientsButton.setEnabled(false);
                arePatientsAttached = true;
                checkIfBothFilesAreAttached();
            }
        } catch (MyException myException) {
            optimizer.getPatientsManagement().reset();
            optimizer.resetPatients();
            optimizer.getCommunicator().saveMessage("Wystąpił błąd podczas załączania pacjentów");
        }
    }

    private void attachMap() {
        String path = getPathFromFileChooser();
        try {
            if (!path.equals("")) {
                optimizer.loadMap(path);
                attachMapButton.setForeground(GREEN.darker());
                attachMapButton.setText("Załączono");
                attachMapButton.setEnabled(false);
                isMapAttached = true;
                checkIfBothFilesAreAttached();
                optimizer.showMap();
            }
        } catch (MyException myException) {
            optimizer.resetMap();
            optimizer.getCommunicator().saveMessage("Wystąpił błąd podczas załączania mapy");
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
