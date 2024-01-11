package synthesizer;

public abstract class AbstractBoundedQueue<T> implements BoundedQueue<T> {
    protected int capacity;
    protected int fillCount;

    public int capacity() {
        return capacity;
    }

    public int fillCount() {
        return fillCount;
    }
}
