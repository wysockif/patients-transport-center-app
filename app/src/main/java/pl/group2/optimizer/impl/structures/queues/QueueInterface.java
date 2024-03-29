package pl.group2.optimizer.impl.structures.queues;

public interface QueueInterface<T> {
    void add(T item);

    T remove();

    int size();

    boolean isEmpty();

    boolean contains(T item);

}
