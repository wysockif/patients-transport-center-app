package pl.group2.optimizer.impl.items.paths;

import java.util.LinkedList;
import java.util.List;

public class Paths {
    private final List<Path> paths;

    public Paths() {
        paths = new LinkedList<>();
    }

    public void addNewPath(Path path) {
        checkIfArgumentIsNull(path);
        paths.add(path);
    }

    public boolean contains(Path path){
        checkIfArgumentIsNull(path);
        return paths.contains(path);
    }

    private void checkIfArgumentIsNull(Object argument) {
        if (argument == null) {
            throw new IllegalArgumentException("Niezainicjowany argument");
        }
    }
}
