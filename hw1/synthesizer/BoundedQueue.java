package synthesizer;

public interface BoundedQueue<T> extends Iterable<T> {
    /**
     * Return size of the buffer
     */
    int capacity();

    /**
     * Return number of items currently in the buffer
     */
    int fillCount();

    /**
     * Add item x to the end
     */
    void enqueue(T x);

    /**
     * Delete and return item from the front
     */
    T dequeue();

    /**
     * Return (but do not delete) item from the front
     */
    T peek();

    /**
     * Is the buffer empty (fillCount equals zero)?
     */
    default boolean isEmpty() {
        return fillCount() == 0;
    }

    /**
     * Is the buffer full (fillCount is same as capacity)?
     */
    default boolean isFull() {
        return fillCount() == capacity();
    }
}
