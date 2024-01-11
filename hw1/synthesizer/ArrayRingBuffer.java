// Make sure to make this class a part of the synthesizer package
package synthesizer;

import java.util.Iterator;

//Make sure to make this class and all of its methods public
//Make sure to make this class extend AbstractBoundedQueue<t>
public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private final T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        this.rb = (T[]) new Object[capacity];
        this.capacity = capacity;
        this.first = 0;
        this.last = 0;
        this.fillCount = 0;
    }

    /**
     * Increase last by 1, if it greater than capacity, change it to zero
     */
    private void incLast() {
        last = (last + 1) % capacity;
    }

    /**
     * Increase first by 1, if it greater than capacity, change it to zero
     */
    private void incFirst() {
        first = (first + 1) % capacity;
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    public void enqueue(T x) {
        if (isFull()) {
            throw new RuntimeException("Ring buffer overflow");
        }
        rb[last] = x;
        incLast();
        fillCount++;
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    public T dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow.");
        }
        T returnItem = rb[first];
        rb[first] = null;
        incFirst();
        fillCount--;
        return returnItem;
    }

    /**
     * Return oldest item, but don't remove it.
     */
    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow.");
        }
        return rb[first];
    }

    // When you get to part 5, implement the needed code to support iteration.
    private class ArrayRingBufferIterator implements Iterator<T> {
        private int index;

        ArrayRingBufferIterator() {
            index = first;
        }

        public boolean hasNext() {
            return index != last;
        }

        public T next() {
            T returnItem = rb[index];
            index = (index + 1) % capacity;
            return returnItem;
        }
    }

    public Iterator<T> iterator() {
        return new ArrayRingBufferIterator();
    }
}
