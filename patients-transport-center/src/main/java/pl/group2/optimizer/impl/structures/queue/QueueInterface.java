package pl.group2.optimizer.impl.structures.queue;

public interface QueueInterface<T> {
    void add(T item);

    T remove();

    T front();

    int size();

    boolean isEmpty();

    boolean contains(T item);

}
