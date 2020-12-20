package pl.group2.optimizer.impl.items.hospitals;

import java.util.HashMap;
import java.util.Map;

public class Hospitals {
    private final Map<Integer, Hospital> hospitalsByIndex;
    private final Map<Integer, Integer> indexById;
    private int counter;

    public Hospitals() {
        hospitalsByIndex = new HashMap<>();
        indexById = new HashMap<>();
    }

    public void add(Hospital hospital){
        int id = hospital.getId();
        hospitalsByIndex.put(counter, hospital);
        indexById.put(id, counter);
        counter++;
    }

    public Hospital getHospitalByIndex(int index){
        return hospitalsByIndex.get(index);
    }

    public boolean contain(int id){
        return indexById.containsKey(id);
    }

    public int getHospitalIndexById(int id){
        return indexById.get(id);
    }

    public Hospital getHospitalById(int id){
        int index = indexById.get(id);
        return hospitalsByIndex.get(index);
    }

}
