package pl.group2.optimizer.impl.io;

import static java.lang.System.exit;

public class ErrorHandler {

    public static final int INPUT_FILE_NOT_FOUND = -100;
    public static final int INPUT_FILE_INCORRECT_HEADLINE = -101;
    public static final int INPUT_FILE_INCORRECT_FORMAT = -102;

    private ErrorHandler() {
    }

    public static void handleError(int codeError, String message) {
        System.err.println("\n| Błąd " + codeError + " | " + message + "!");
        exit(codeError);
    }

    public static void handleError(int codeError, String message, String tip) {
        System.err.println("\n| Błąd " + codeError + " | " + message + "!");
        System.out.println("\nWskazówka: " + tip);
        exit(codeError);
    }
}