package pl.group2.optimizer.impl.io;

import pl.group2.optimizer.gui.components.PatientsManagement;
import pl.group2.optimizer.impl.items.Items;
import pl.group2.optimizer.impl.items.hospitals.Hospitals;
import pl.group2.optimizer.impl.items.paths.Paths;
import pl.group2.optimizer.impl.items.patients.Patients;
import pl.group2.optimizer.impl.items.specialobjects.SpecialObjects;

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
    private String fileName;
    private Scanner scanner;
    private String whereInFileMessage;

    private Patients patients;
    private Hospitals hospitals;
    private SpecialObjects specialObjects;
    private Paths paths;

    public void readData(String path) throws MyException {
        prepareFile(path);
        hospitals = loadHospitalsFromFile();
        specialObjects = loadSpecialObjectsFromFile();
        paths = loadPathsFromFile();
    }

    public void readData(String path, PatientsManagement patientsManagement) throws MyException {
        prepareFile(path);
        patients = loadPatientsFromFile(patientsManagement);
    }

    private void prepareFile(String path) throws MyException {
        File inputFile = new File(path);
        scanner = createScannerIfSpecifiedFileExists(inputFile);
        checkIfArgumentsAreNotNull(scanner);
        fileName = inputFile.getName();
    }

    private Items readDataFromFile(Items items) throws MyException {
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            lineNumber++;
            if (isHeadlineCorrect(line)) {
                break;
            }
            whereInFileMessage = "[Plik wejściowy: " + fileName + ", nr linii: " + lineNumber + "]. ";
            if(!loadSingleItem(items, line)){
                break;
            }
        }
        return items;
    }

    private Scanner createScannerIfSpecifiedFileExists(File file) throws MyException {
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

    private Patients loadPatientsFromFile(PatientsManagement patientsManagement) throws MyException {
        checkHeadline(true);
        return (Patients) readDataFromFile(new Patients(patientsManagement));
    }

    private Hospitals loadHospitalsFromFile() throws MyException {
        checkHeadline(true);
        return (Hospitals) readDataFromFile(new Hospitals());
    }

    private SpecialObjects loadSpecialObjectsFromFile() throws MyException {
        checkHeadline(false);
        return (SpecialObjects) readDataFromFile(new SpecialObjects());
    }

    private Paths loadPathsFromFile() throws MyException {
        checkHeadline(false);
        return (Paths) readDataFromFile(new Paths(hospitals));
    }

    private void checkHeadline(boolean firstInFile) throws MyException {
        if (firstInFile) {
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
    }

    private boolean isHeadlineCorrect(String headline) {
        return headline.startsWith("#");
    }

    private boolean loadSingleItem(Items item, String line) throws MyException {
        String[] attributes = line.split(Pattern.quote(" | "));
        try {
            Object[] convertedAttributes = item.convertAttributes(attributes);
            item.validateAttributes(convertedAttributes);
            item.addNewElement(convertedAttributes);
        } catch (DataFormatException e) {
            String message = whereInFileMessage + e.getMessage();
            ErrorHandler.handleError(INPUT_FILE_INCORRECT_FORMAT, message);
            return false;
        }
        return true;
    }

    public Patients getPatients() {
        return patients;
    }

    public Hospitals getHospitals() {
        return hospitals;
    }

    public SpecialObjects getSpecialObjects() {
        return specialObjects;
    }

    public Paths getPaths() {
        return paths;
    }
}