package pl.group2.optimizer.gui;

import pl.group2.optimizer.Optimizer;
import pl.group2.optimizer.gui.components.Communicator;
import pl.group2.optimizer.gui.components.Management;
import pl.group2.optimizer.gui.components.Plan;

import javax.swing.JFrame;

public class Window extends JFrame {
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 820;

    public Window(Optimizer optimizer) {
        super("Patients Transport Center");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
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
}
