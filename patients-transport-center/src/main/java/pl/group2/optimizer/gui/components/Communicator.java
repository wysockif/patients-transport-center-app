package pl.group2.optimizer.gui.components;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import java.awt.Font;

import static java.awt.Color.DARK_GRAY;
import static java.awt.Color.WHITE;
import static javax.swing.text.DefaultCaret.ALWAYS_UPDATE;

public class Communicator extends JPanel {
    public static final int WIDTH = 950;
    public static final int HEIGHT = 150;

    private final JTextArea textArea;
    private String messages;

    public Communicator() {
        setBounds(0, 650, WIDTH, HEIGHT);
        setLayout(null);
        textArea = createTextArea();
        messages = "";
    }

    private JTextArea createTextArea() {
        JTextArea jTextArea = new JTextArea();
        jTextArea.setSize(WIDTH, HEIGHT);
        jTextArea.setBackground(DARK_GRAY.darker());
        jTextArea.setForeground(WHITE);
        jTextArea.setFont(new Font("Font", Font.ITALIC, 14));
        jTextArea.setEditable(false);
        DefaultCaret caret = (DefaultCaret) jTextArea.getCaret();
        caret.setUpdatePolicy(ALWAYS_UPDATE);
        JScrollPane jScrollPane = new JScrollPane(jTextArea);
        jScrollPane.setSize(WIDTH, HEIGHT - 20);
        jScrollPane.setBorder(new EmptyBorder(1, 1, 1, 1));
        add(jScrollPane);
        return jTextArea;
    }

    public void saveMessage(String message) {
        String singleMessage;
        if (messages.isEmpty()) {
            singleMessage = " > " + message + ";";
        } else {
            singleMessage = "\n > " + message + ";";
        }
        messages += singleMessage;
        textArea.setText(messages);
    }
}
