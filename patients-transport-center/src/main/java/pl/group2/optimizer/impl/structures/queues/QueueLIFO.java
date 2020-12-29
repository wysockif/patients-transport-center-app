package pl.group2.optimizer.impl.structures.queues;

import java.util.LinkedList;

public class QueueLIFO<T> implements QueueInterface<T> {
    private final LinkedList<T> queue;

    public QueueLIFO() {
        queue = new LinkedList<>();
    }

    @Override
    public void add(T item) {
        validateArgument(item);
        queue.addLast(item);
    }

    @Override
    public T remove() {
        return queue.removeLast();
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

    public T nextToTop() {
        int size = queue.size();
        if (size < 2) {
            throw new IllegalArgumentException("Cannot return non-existent value");
        }
        return queue.get(size - 2);
    }

    private void validateArgument(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }
    }

    public T top() {
        if (queue.isEmpty()) {
            throw new IllegalArgumentException("Cannot return non-existent  value");
        }
        return queue.getLast();
    }
}
