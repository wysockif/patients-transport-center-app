package pl.group2.optimizer.impl.io;

import javax.swing.JOptionPane;

public class ErrorHandler {

    public static final int INPUT_FILE_NOT_FOUND = -100;
    public static final int INPUT_FILE_INCORRECT_HEADLINE = -101;
    public static final int INPUT_FILE_INCORRECT_FORMAT = -102;
    public static final int NO_HOSPITALS_AVAILABLE = -103;

    public static void handleError(int codeError, String message) throws MyException {
        String informationAboutError = "\n| Błąd " + codeError + " | " + message + "!";

        JOptionPane.showMessageDialog(null, informationAboutError, "Patients Transport Center", JOptionPane.ERROR_MESSAGE);
        throw new MyException(message);
  }
}