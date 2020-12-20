package pl.group2.optimizer.impl.io;

import pl.group2.optimizer.impl.items.Items;
import pl.group2.optimizer.impl.items.patients.Patients;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

import static pl.group2.optimizer.impl.io.ErrorHandler.INPUT_FILE_INCORRECT_FORMAT;
import static pl.group2.optimizer.impl.io.ErrorHandler.INPUT_FILE_INCORRECT_HEADLINE;
import static pl.group2.optimizer.impl.io.ErrorHandler.INPUT_FILE_NOT_FOUND;

public class TextFileReader {

    private int lineNumber;
    private final String fileName;
    private final Scanner scanner;
    private final Patients patients;

    public TextFileReader(String path) {
        File inputFile = new File(path);
        fileName = inputFile.getName();
        scanner = createScannerIfSpecifiedFileExists(inputFile);
        patients = loadPatientsFromFile(scanner);
    }

    private Patients loadPatientsFromFile(Scanner scanner) {
        checkHeadline();
        return (Patients) readDataFromFile(scanner, new Patients());
    }

    public Items readDataFromFile(Scanner scanner, Items items) {
        checkIfArgumentsAreNotNull(scanner, items);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            lineNumber++;

            if (isHeadlineCorrect(line)) {
                break;
            }
            loadSingleItem(items, line);
        }
        return items;
    }

    private void loadSingleItem(Items item, String line) {
        String[] attributes = line.split(Pattern.quote(" | "));
        try {
            Object[] convertedAttributes = item.convertAttributes(attributes);
            item.validateAttributes(convertedAttributes);
            item.addNewElement(convertedAttributes);
        } catch (DataFormatException e) {
            String message = "[Plik wejściowy: " + fileName + ", nr linii: " + lineNumber + "]. " + e.getMessage();
            ErrorHandler.handleError(INPUT_FILE_INCORRECT_FORMAT, message);
        }
    }

    private void checkIfArgumentsAreNotNull(Object... args) {
        for (Object o : args) {
            if (o == null) {
                throw new IllegalArgumentException("Niezainicjowany argument!");
            }
        }
    }

    private void checkHeadline() {
        String headline = null;

        if (scanner.hasNext()) {
            headline = scanner.nextLine();
            lineNumber++;
        }
        if (headline == null || !isHeadlineCorrect(headline)) {
            String message = "[Plik wejściowy: " + fileName + ", nr linii: " + lineNumber + "]. " + "Niepoprawny nagłówek";
            ErrorHandler.handleError(INPUT_FILE_INCORRECT_HEADLINE, message);
        }
    }

    private Scanner createScannerIfSpecifiedFileExists(File file) {
        Scanner createdScanner = null;
        try {
            createdScanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            String message = "[Plik wejściowy: " + fileName + "]. Plik nie został znaleziony";
            ErrorHandler.handleError(INPUT_FILE_NOT_FOUND, message);
        }
        return createdScanner;
    }

    private boolean isHeadlineCorrect(String headline) {
        return headline.startsWith("#");
    }

    public Patients getPatients() {
        return patients;
    }
}