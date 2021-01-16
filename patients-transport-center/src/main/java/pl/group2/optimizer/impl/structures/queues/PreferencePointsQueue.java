package pl.group2.optimizer.impl.structures.queues;

import pl.group2.optimizer.impl.items.pathspoints.PathPoint;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class PreferencePointsQueue implements QueueInterface<PathPoint> {
    private final LinkedList<PathPoint> preferenceQueue;

    public PreferencePointsQueue() {
        preferenceQueue = new LinkedList<>();
    }

    @Override
    public void add(PathPoint item) {
        validateArgument(item);
        preferenceQueue.add(item);
        sortInPriorityOrder();
    }

    public void addAll(List<PathPoint> list) {
        validateArgument(list);
        preferenceQueue.addAll(list);
        sortInPriorityOrder();
    }

    @Override
    public PathPoint remove() {
        return preferenceQueue.removeFirst();
    }

    @Override
    public int size() {
        return preferenceQueue.size();
    }

    @Override
    public boolean isEmpty() {
        return preferenceQueue.isEmpty();
    }

    @Override
    public boolean contains(PathPoint item) {
        validateArgument(item);
        return preferenceQueue.contains(item);
    }

    public void delete(PathPoint item) {
        validateArgument(item);
        preferenceQueue.remove(item);
        sortInPriorityOrder();
    }

    private void validateArgument(Object item) {
        if (item == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }
    }

    private void sortInPriorityOrder() {
        Collections.sort(preferenceQueue, Comparator.comparing(PathPoint::getXCoordinate)
                .thenComparing(PathPoint::isLeft)
                .thenComparing(PathPoint::getYCoordinate)
                .thenComparing(PathPoint::isVerticalPath));
    }

    public void showQueue() {
        for(PathPoint pointe: preferenceQueue) {
            System.out.println(pointe.toString());
        }
    }
}
