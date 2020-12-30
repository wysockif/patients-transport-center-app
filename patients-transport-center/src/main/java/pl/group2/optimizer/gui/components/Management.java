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

import static javax.swing.JFileChooser.APPROVE_OPTION;
import static javax.swing.SwingConstants.CENTER;

public class Management extends JPanel {
    private final Optimizer optimizer;
    private JLabel title;
    private JLabel attachMapLabel;
    private JButton attachMapButton;
    private JLabel attachPatientsLabel;
    private JButton attachPatientsButton;

    private JButton runButton;

    private JLabel paceLabel;
    private JSlider paceSlider;

    public Management(Optimizer optimizer) {
        this.optimizer = optimizer;
        title = createTitle();
        createMapElements();
        createPatientsElements();
        createSliderElements();
        runButton = createRunButton();
        setSize(250, 820);
        setLocation(950, 0);
        setLayout(null);
    }

    private JButton createRunButton() {
        JButton button = new JButton("Uruchom symulację");
        button.setBounds(40, 550, 155, 40);
        button.addActionListener(e -> optimizer.run());
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
        paceSlider.setBackground(Color.gray);
        paceSlider.setMajorTickSpacing(25);
        paceSlider.setMinorTickSpacing(5);
        paceSlider.setPaintTicks(true);
        paceSlider.setPaintLabels(true);
        add(paceSlider);
    }

    private void createPatientsElements() {
        attachPatientsLabel = new JLabel("Załącz plik z pacjentami:", CENTER);
        attachPatientsLabel.setFont(new Font("TitleFont", Font.BOLD, 14));
        attachPatientsLabel.setBounds(0, 140, 235, 20);
        add(attachPatientsLabel);

        attachPatientsButton = new JButton("Wczytaj");
        attachPatientsButton.setBounds(40, 165, 155, 40);
        attachPatientsButton.addActionListener(e -> {
            try {
                optimizer.loadPatients(getPathFromFileChooser());
                JOptionPane.showMessageDialog(null, optimizer.messageAboutDownloadedPatients(),
                        "Patients Transport Center", JOptionPane.INFORMATION_MESSAGE);
            } catch (MyException myException) {
                myException.printStackTrace();
            }
        });
        add(attachPatientsButton);
    }

    private void createMapElements() {
        attachMapLabel = new JLabel("Załącz plik z mapą:", CENTER);
        attachMapLabel.setFont(new Font("TitleFont", Font.BOLD, 14));
        attachMapLabel.setBounds(0, 60, 235, 20);
        add(attachMapLabel);

        attachMapButton = new JButton("Wczytaj");
        attachMapButton.setBounds(40, 85, 155, 40);
        attachMapButton.addActionListener(e -> {
            try {
                optimizer.loadMap(getPathFromFileChooser());
                JOptionPane.showMessageDialog(null, optimizer.messageAboutDownloadedMap(),
                        "Patients Transport Center", JOptionPane.INFORMATION_MESSAGE);
            } catch (MyException myException) {
                myException.printStackTrace();
            }
        });
        add(attachMapButton);
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
        g.fillRect(0, 0, 250, 820);
    }
}
