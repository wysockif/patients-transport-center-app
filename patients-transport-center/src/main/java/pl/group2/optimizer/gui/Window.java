package pl.group2.optimizer.gui;

import pl.group2.optimizer.Optimizer;
import pl.group2.optimizer.gui.components.Communicator;
import pl.group2.optimizer.gui.components.Management;
import pl.group2.optimizer.gui.components.PatientsManagement;
import pl.group2.optimizer.gui.components.Plan;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class Window extends JFrame {
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 820;

    public Window(Optimizer optimizer, Plan plan, PatientsManagement patientsManagement, Communicator communicator) {
        super("Patients Transport Center");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addAskingIfUserWantToExit();
        add(plan);
        setResizable(false);
        setLayout(null);
        setLocationRelativeTo(null);
        createPanels(optimizer, patientsManagement, communicator);
        setVisible(true);
        setIcon();
    }

    private void setIcon() {
        try {
            setIconImage(new ImageIcon(Window.class.getResource("/img/icon/1.png")).getImage());
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Błąd krytyczny!\n" +
                    "Nie mogę znaleźć pliku z ikoną pacjenta!", "Błąd krytyczny!", ERROR_MESSAGE);
            System.exit(2);
        }
    }

    private void createPanels(Optimizer optimizer, PatientsManagement patientsManagement, Communicator communicator) {
        Management management = new Management(optimizer);
        add(management);
        add(patientsManagement);
        createPanels(optimizer, communicator);
        setVisible(true);
    }

    private void createPanels(Optimizer optimizer, Communicator communicator) {
        Management management = new Management(optimizer);
        add(management);
        add(communicator);
    }

    private void addAskingIfUserWantToExit() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                String[] ObjButtons = {"Tak", "Nie"};
                int PromptResult = JOptionPane.showOptionDialog(null,
                        "Czy na pewno chcesz zamknąć aplikację?", "Patients Transport Center",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
                        ObjButtons, ObjButtons[1]);
                if (PromptResult == 0) {
                    System.exit(0);
                }
            }
        });
    }
}
