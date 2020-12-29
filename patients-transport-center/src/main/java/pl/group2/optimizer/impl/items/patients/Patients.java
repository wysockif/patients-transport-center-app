package pl.group2.optimizer.impl.items.patients;

import pl.group2.optimizer.impl.items.Items;
import pl.group2.optimizer.impl.structures.queues.QueueFIFO;
import pl.group2.optimizer.impl.structures.queues.QueueInterface;
import pl.group2.optimizer.impl.structures.queues.QueueLIFO;

import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

public class Patients implements Items {
    private final QueueFIFO<Patient> patientsQueue;

    public Patients() {
        patientsQueue = new QueueFIFO<>();
    }

    public void addNew(Patient patient) {
        patientsQueue.add(patient);
    }

    public Patient getNextToHandle() {
        return patientsQueue.remove();
    }

    public boolean contain(int id) {
        return patientsQueue.contains(new Patient(id, 0, 0));
    }

    public int size() {
        return patientsQueue.size();
    }

    public Patient getFirst() {
        return patientsQueue.front();
    }

    @Override
    public Object[] convertAttributes(String[] attributes) throws DataFormatException {
        checkIfArgumentIsNotNull(attributes);
        if (attributes.length != 3) {
            throw new DataFormatException("Niepoprawny format danych");
        }
        return parseAttributes(attributes);
    }

    @Override
    public void validateAttributes(Object[] attributes) throws DataFormatException {
        checkIfArgumentIsNotNull(attributes);
        int id = (int) attributes[0];

        if (id < 0) {
            String message = "Niepoprawny format danych. Ujemna wartość reprezentująca id pacjenta";
            throw new DataFormatException(message);
        }
        if (contain(id)) {
            throw new DataFormatException("Nie można dodawać pacjentów o tym samym id");
        }
    }

    @Override
    public void addNewElement(Object[] attributes) {
        checkIfArgumentIsNotNull(attributes);
        int id = (int) attributes[0];
        int x = (int) attributes[1];
        int y = (int) attributes[1];

        Patient patient = new Patient(id, x, y);
        addNew(patient);
    }

    private void checkIfArgumentIsNotNull(Object argument) {
        if (argument == null) {
            throw new IllegalArgumentException("Niezainicjowany argument");
        }
    }

    private Object[] parseAttributes(String[] attributes) throws DataFormatException {
        checkIfArgumentIsNotNull(attributes);
        Object[] convertedAttributes = new Object[attributes.length];
        try {
            convertedAttributes[0] = Integer.parseInt(attributes[0]);
            convertedAttributes[1] = Integer.parseInt(attributes[1]);
            convertedAttributes[2] = Integer.parseInt(attributes[2]);
        } catch (NumberFormatException e) {
            String regex = Pattern.quote("For input string: ");
            String info = e.getMessage().replaceAll(regex, "");
            String message = "Nieudana konwersja danej: " + info;
            throw new DataFormatException(message);
        }
        return convertedAttributes;
    }
}