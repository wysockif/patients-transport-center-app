package pl.group2.optimizer.gui;

import pl.group2.optimizer.Optimizer;
import pl.group2.optimizer.gui.components.Communicator;
import pl.group2.optimizer.gui.components.Management;
import pl.group2.optimizer.gui.components.Plan;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Window extends JFrame {
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 820;

    public Window(Optimizer optimizer) {
        super("Patients Transport Center");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addAskingIfUserWantToExit();

        setResizable(false);
        setLayout(null);
        setLocationRelativeTo(null);
        createPanels(optimizer);
        setVisible(true);
    }

    private void createPanels(Optimizer optimizer) {
        Plan plan = new Plan(optimizer);
        Management management = new Management(optimizer);

        Communicator communicator = new Communicator();
        add(communicator);

        add(plan);
        add(management);
    }

    private void addAskingIfUserWantToExit() {
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent we) {
                String ObjButtons[] = {"Yes", "No"};
                int PromptResult = JOptionPane.showOptionDialog(null,
                        "Are you sure you want to exit?", "Patients Transport Center",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
                        ObjButtons, ObjButtons[1]);
                if (PromptResult == 0) {
                    System.exit(0);
                }
            }
        });
    }
}
