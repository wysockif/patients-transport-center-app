package pl.group2.optimizer.gui.components;

import pl.group2.optimizer.Optimizer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.DataFormatException;

import static java.awt.Color.DARK_GRAY;
import static java.awt.Color.GRAY;
import static java.awt.Color.WHITE;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.SwingConstants.CENTER;

public class PatientsManagement extends JPanel {
    private final Optimizer optimizer;
    private final JTextArea area;
    private final List<String> patients;
    private final JButton addButton;
    private JTextField idField, xField, yField;

    public PatientsManagement(Optimizer optimizer) {
        this.optimizer = optimizer;
        setSize(250, 420);
        setLocation(950, 400);
        setLayout(null);
        addTitle();
        addLabel();
        addingComponents();
        addButton = createButton();
        patients = new LinkedList<>();
        area = createPatientsArea();
    }

    private JButton createButton() {
        JButton button = new JButton("Dodaj");
        button.setBounds(40, 300, 155, 40);
        button.addActionListener(e -> createPatient());
        button.setFocusable(false);
        button.setBackground(DARK_GRAY);
        button.setForeground(WHITE);
        button.setEnabled(false);
        add(button);
        return button;
    }

    private void createPatient() {
        String[] attributes = {idField.getText(), xField.getText(), yField.getText()};
        try {
            for (String attribute : attributes) {
                if (attribute.isBlank()) {
                    throw new DataFormatException("Aby dodać pacjenta wszystkie pola muszą zostać wypełnione!");
                }
            }
            Object[] convertedAttributes = optimizer.getPatients().convertAttributes(attributes);
            optimizer.getPatients().validateAttributes(convertedAttributes);
            optimizer.getPatients().addNewElement(convertedAttributes);
            String message = "Dodano pacjenta o id = " + idField.getText();
            optimizer.getCommunicator().saveMessage(message);
            resetFields();
        } catch (DataFormatException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Błąd", ERROR_MESSAGE);
        }
    }

    private void resetFields() {
        idField.setText("");
        xField.setText("");
        yField.setText("");
    }

    private void addingComponents() {
        createLabel("id:", 25);
        idField = createField(25);
        createLabel("x:", 85);
        xField = createField(85);
        createLabel("y:", 145);
        yField = createField(145);
    }

    private void createLabel(String name, int x) {
        JLabel label = new JLabel(name, CENTER);
        label.setBounds(x, 235, 60, 20);
        add(label);
    }

    private JTextField createField(int x) {
        JTextField textField = new JTextField();
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setBackground(DARK_GRAY.darker());
        textField.setForeground(WHITE);
        textField.setBounds(x, 255, 60, 40);
        textField.setEnabled(false);
        add(textField);
        return textField;
    }

    private void addLabel() {
        JLabel title = new JLabel("Dodaj nowego pacjenta:", CENTER);
        title.setFont(new Font("TitleFont", Font.BOLD, 14));
        title.setBounds(0, 200, 235, 40);
        add(title);
    }

    private void addTitle() {
        JLabel title = new JLabel("Kolejka pacjentów:", CENTER);
        title.setFont(new Font("TitleFont", Font.BOLD, 14));
        title.setBounds(0, 0, 235, 40);
        add(title);
    }

    private JTextArea createPatientsArea() {
        JTextArea jTextArea = new JTextArea();
        jTextArea.setBackground(DARK_GRAY.darker());
        jTextArea.setForeground(WHITE);
        jTextArea.setEditable(false);
        JScrollPane jScrollPane = new JScrollPane(jTextArea);
        jScrollPane.setLocation(5, 40);

        jScrollPane.setSize(225, 150);
        jScrollPane.setBorder(new EmptyBorder(1, 1, 1, 1));
        add(jScrollPane);
        return jTextArea;
    }

    public void addNewPatient(int id, int x, int y) {
        String singlePatient = String.format("  Pacjent o id = %d (x=%d, y=%d)", id, x, y);
        patients.add(singlePatient);
        String allPatients = String.join("\n", patients);
        area.setText(allPatients);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(GRAY);
        g.fillRect(0, 0, 250, 420);
    }


    public void enablePanel() {
        addButton.setEnabled(true);
        idField.setEnabled(true);
        xField.setEnabled(true);
        yField.setEnabled(true);
    }

    public JTextField getXField() {
        return xField;
    }

    public JTextField getYField() {
        return yField;
    }
}
