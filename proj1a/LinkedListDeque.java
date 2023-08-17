/**
 * Deque implemented by Double LinkedList
 */
public class LinkedListDeque<T> {
    private final Node sentinel;
    private int size;

    private class Node {
        T item;
        Node prev;
        Node next;

        Node(T i, Node p, Node n) {
            item = i;
            prev = p;
            next = n;
        }

        Node() {
            item = null;
            prev = this;
            next = this;
        }
    }


    /**
     * Creates an empty linked list deque.
     */
    public LinkedListDeque() {
        sentinel = new Node();
        size = 0;
    }

    /**
     * Adds an item of type T to the front of the deque.
     */
    public void addFirst(T item) {
        final Node node = new Node(item, sentinel, sentinel.next);
        sentinel.next.prev = node;
        sentinel.next = node;
        size++;
    }

    /**
     * Adds an item of type T to the back of the deque.
     */
    public void addLast(T item) {
        final Node node = new Node(item, sentinel.prev, sentinel);
        sentinel.prev.next = node;
        sentinel.prev = node;
        size++;
    }

    /**
     * Returns true if deque is empty, false otherwise.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of items in the deque.
     */
    public int size() {
        return size;
    }

    /**
     * Prints the items in the deque from first to last, separated by a space.
     */
    public void printDeque() {
        Node p = sentinel;
        if (p.next != sentinel) {
            while (true) {
                p = p.next;
                System.out.print(p.item);
                if (p.next == sentinel) {
                    break;
                }
                System.out.print(" ");
            }
        }
        System.out.println();
    }

    /**
     * Removes and returns the item at the front of the deque.
     * If no such item exists, returns null.
     */
    public T removeFirst() {
        if (sentinel.next == sentinel) {
            return null;
        }

        Node firstNode = sentinel.next;
        sentinel.next = firstNode.next;
        firstNode.next.prev = sentinel;
        firstNode.next = null;
        firstNode.prev = null;
        size--;
        return firstNode.item;
    }

    /**
     * Removes and returns the item at the back of the deque.
     * If no such item exists, returns null.
     */
    public T removeLast() {
        if (sentinel.prev == sentinel) {
            return null;
        }

        Node lastNode = sentinel.prev;
        sentinel.prev = lastNode.prev;
        lastNode.prev.next = sentinel;
        lastNode.next = null;
        lastNode.prev = null;
        size--;
        return lastNode.item;
    }

    /**
     * Gets the item at the given index,
     * where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null. Must not alter the deque!
     */
    public T get(int index) {
        Node p = sentinel.next;
        while (p != sentinel && index >= 0) {
            if (index == 0) {
                return p.item;
            }
            index--;
            p = p.next;
        }
        return null;
    }

    /**
     * Helper function for getRecursive
     */
    private T getRecursiveHelper(Node first, int index) {
        if (first == sentinel) {
            return null;
        }
        if (index == 0) {
            return first.item;
        }
        return getRecursiveHelper(first.next, index - 1);
    }

    /**
     * Same as get, but uses recursion.
     */
    public T getRecursive(int index) {
        return getRecursiveHelper(sentinel.next, index);
    }

}
