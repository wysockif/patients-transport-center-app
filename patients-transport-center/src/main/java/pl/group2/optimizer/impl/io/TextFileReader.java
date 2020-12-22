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

    private final String path;
    private int lineNumber;
    private String fileName;
    private Scanner scanner;
    private Patients patients;
    private String whereInFileMessage;

    public TextFileReader(String path) {
        this.path = path;
    }

    public void readData() {
        File inputFile = new File(path);
        scanner = createScannerIfSpecifiedFileExists(inputFile);
        checkIfArgumentsAreNotNull(scanner);
        fileName = inputFile.getName();

        patients = loadPatientsFromFile();
    }

    private Items readDataFromFile(Items items) {
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            lineNumber++;
            if (isHeadlineCorrect(line)) {
                break;
            }
            whereInFileMessage = "[Plik wejściowy: " + fileName + ", nr linii: " + lineNumber + "]. ";
            loadSingleItem(items, line);
        }
        return items;
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

    private void checkIfArgumentsAreNotNull(Object... args) {
        for (Object o : args) {
            if (o == null) {
                throw new IllegalArgumentException("Niezainicjowany argument!");
            }
        }
    }

    private Patients loadPatientsFromFile() {
        checkHeadline();
        return (Patients) readDataFromFile(new Patients());
    }

    private void checkHeadline() {
        String headline = null;

        if (scanner.hasNext()) {
            headline = scanner.nextLine();
            lineNumber++;
        }
        if (headline == null || !isHeadlineCorrect(headline)) {
            String message = whereInFileMessage + "Niepoprawny nagłówek";
            ErrorHandler.handleError(INPUT_FILE_INCORRECT_HEADLINE, message);
        }
    }

    private boolean isHeadlineCorrect(String headline) {
        return headline.startsWith("#");
    }

    private void loadSingleItem(Items item, String line) {
        String[] attributes = line.split(Pattern.quote(" | "));
        try {
            Object[] convertedAttributes = item.convertAttributes(attributes);
            item.validateAttributes(convertedAttributes);
            item.addNewElement(convertedAttributes);
        } catch (DataFormatException e) {
            String message = whereInFileMessage + e.getMessage();
            ErrorHandler.handleError(INPUT_FILE_INCORRECT_FORMAT, message);
        }
    }

    public Patients getPatients() {
        return patients;
    }
}