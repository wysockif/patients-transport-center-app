package pl.group2.optimizer.impl.items.specialobjects;

import java.util.HashMap;
import java.util.Map;

public class SpecialObjects {
    private final Map<Integer,SpecialObject> specialObjectByIndex;
    private final Map<Integer, Integer> indexById;
    private int counter;

    public SpecialObjects() {
        specialObjectByIndex = new HashMap<>();
        indexById = new HashMap<>();
    }

    public void add(SpecialObject specialObject){
        int id = specialObject.getId();
        specialObjectByIndex.put(counter, specialObject);
        indexById.put(id, counter);
        counter++;
    }

    public SpecialObject getSpecialObjectByIndex(int index){
        return specialObjectByIndex.get(index);
    }

    public boolean contain(int id){
        return indexById.containsKey(id);
    }

    public int getSpecialObjectIndexById(int id){
        return indexById.get(id);
    }

    public SpecialObject getSpecialObjectById(int id){
        int index = indexById.get(id);
        return specialObjectByIndex.get(index);
    }
}
