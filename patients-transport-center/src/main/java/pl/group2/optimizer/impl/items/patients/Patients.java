package pl.group2.optimizer.impl.items.patients;

import pl.group2.optimizer.impl.structures.queue.QueueFIFO;
import pl.group2.optimizer.impl.structures.queue.QueueInterface;

public class Patients {
    private final QueueInterface<Patient> patientsQueue;

    public Patients() {
        patientsQueue = new QueueFIFO<>();
    }

    public void addNew(Patient patient){
        patientsQueue.add(patient);
    }

    public Patient getNextToHandle(){
        return patientsQueue.remove();
    }

    public boolean contain(int id){
        return patientsQueue.contains(new Patient(id, 0,0));
    }

}
