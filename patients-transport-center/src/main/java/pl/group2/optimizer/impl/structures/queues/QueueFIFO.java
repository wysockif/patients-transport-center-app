package pl.group2.optimizer.impl.structures.queues;

import java.util.Collection;
import java.util.LinkedList;

public class QueueFIFO<T> implements QueueInterface<T> {
    private final LinkedList<T> queue;

    public QueueFIFO() {
        queue = new LinkedList<>();
    }

    @Override
    public void add(T item) {
        validateArgument(item);
        queue.addLast(item);
    }

    @Override
    public T remove() {
        return queue.removeFirst();
    }

    @Override
    public int size() {
        return queue.size();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public boolean contains(T item) {
        validateArgument(item);
        return queue.contains(item);
    }

    private void validateArgument(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }
    }

    public T front() {
        return queue.getFirst();
    }

    public Collection<T> getCollection() {
        return queue;
    }
}
