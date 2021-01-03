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
    private final JTextArea textArea;
    private String messages;

    public Communicator() {
        setBounds(0, 650, 950, 150);
        setLayout(null);
        textArea = createTextArea();
        messages = "";
        createSomeMessages();
    }

    private JTextArea createTextArea() {
        JTextArea jTextArea = new JTextArea();
        jTextArea.setSize(950, 150);
        jTextArea.setBackground(DARK_GRAY.darker());
        jTextArea.setForeground(WHITE);
        jTextArea.setFont(new Font("Font", Font.ITALIC, 14));
        jTextArea.setEditable(false);
        DefaultCaret caret = (DefaultCaret) jTextArea.getCaret();
        caret.setUpdatePolicy(ALWAYS_UPDATE);
        JScrollPane jScrollPane = new JScrollPane(jTextArea);
        jScrollPane.setSize(950, 130);
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

    // do skasowania
    private void createSomeMessages() {
        saveMessage("Pacjent o id 3 został przyjęty do szpitala o id 2");
        saveMessage("Pacjent o id 4 został przyjęty do szpitala o id 2");
        saveMessage("Pacjent o id 5 został przyjęty do szpitala o id 4");
        saveMessage("Pacjent o id 6 nie został przyjęty do szpitala o id 1");
        saveMessage("Pacjent o id 6 został przyjęty do szpitala o id 3");
        saveMessage("Pacjent o id 1 nie został przyjęty do szpitala o id 3");
        saveMessage("Pacjent o id 7 nie został przyjęty do szpitala o id 1");
        saveMessage("Pacjent o id 8 został przyjęty do szpitala o id 3");
        saveMessage("Pacjent o id 9 nie został przyjęty do szpitala o id 3");
    }

}
