package pl.group2.optimizer.impl.structures.queues;

import pl.group2.optimizer.impl.algorithms.dijkstra.DijkstraAlgorithm;
import pl.group2.optimizer.impl.algorithms.dijkstra.Predecessor;

import java.util.ArrayList;
import java.util.List;

public class DijkstraPriorityQueue {
    private List<Predecessor> predecessors;

    public DijkstraPriorityQueue() {
        predecessors = new ArrayList<>();
    }

    public void add(Predecessor predecessor) {
        predecessors.add(predecessor);
        predecessors.sort((o1, o2) -> Integer.compare(o1.getDistance() - o2.getDistance(), 0));
    }

    public Predecessor remove() {
        return predecessors.remove(0);
    }

    public void setNewPriority(int id, int distance) {

        boolean isIn = false;

        for (int i = 0; i < predecessors.size(); i++) {
            if (predecessors.get(i).getId() == id) {
                predecessors.set(i, new Predecessor(predecessors.get(i).getId(), distance));
                isIn = true;
            }
        }
        if (!isIn) {
            for (int i = 0; i < predecessors.size(); i++) {
                if (predecessors.get(i).getDistance() == DijkstraAlgorithm.INFINITY) {
                    predecessors.set(i, new Predecessor(id, distance));
                    break;
                }
            }

        }

        predecessors.sort((o1, o2) -> Integer.compare(o1.getDistance() - o2.getDistance(), 0));
    }


    public boolean isEmpty() {
        return predecessors.size() == 0;
    }
}
