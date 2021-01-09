package pl.group2.optimizer.impl.algorithms.dijkstra;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MyPQ {
    List<Predecessor> predecessors;

    public MyPQ() {
        predecessors = new ArrayList<>();
    }

    public void add(Predecessor predecessor) {
        predecessors.add(predecessor);
        predecessors.sort(new Comparator<Predecessor>() {
            @Override
            public int compare(Predecessor o1, Predecessor o2) {
                if (o1.distance - o2.distance < 0) {
                    return -1;
                } else if (o1.distance - o2.distance > 0) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
    }
}
