package pl.group2.optimizer.gui;

import pl.group2.optimizer.Optimizer;
import pl.group2.optimizer.impl.io.MyException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MyWindow extends JFrame {
    private JButton button;
    private JPanel panel;
    private Optimizer optimizer;

    public MyWindow(Optimizer optimizer) {
        super("Patients Transport Center");
        this.optimizer = optimizer;

        panel = new JPanel();
        button = new JButton("BUTTON");
        panel.setSize(640, 480);
        setSize(panel.getSize());

        setButton();
        panel.add(button);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setContentPane(panel);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setButton() {
        button.addActionListener(e -> {
            try {
                optimizer.loadData();
                optimizer.checkIfDataWasDownloaded();
            } catch (MyException myException) {
                myException.printStackTrace();
            }
        });
    }
}
