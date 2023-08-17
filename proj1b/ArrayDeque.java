public class ArrayDeque<Item> implements Deque<Item> {
    private Item[] array;
    private int size;
    private int arraySize;
    private int nextFirst;
    private int nextLast;

    /**
     * Creates an empty array deque
     */
    public ArrayDeque() {
        array = (Item[]) new Object[8];
        size = 0;
        arraySize = 8;
        nextFirst = 3;
        nextLast = 4;
    }

    /**
     * Resize the array
     */
    private void resize(int capacity) {
        Item[] newArray = (Item[]) new Object[capacity];

        int itemIndex = addOne(nextFirst);
        for (int i = 0; i < size; i++) {
            newArray[i] = array[itemIndex];
            itemIndex = addOne(itemIndex);
        }
        nextFirst = capacity - 1;
        nextLast = size;
        array = newArray;
        arraySize = capacity;
    }

    /**
     * Reduce array size when usage factor not greater than 25%
     * Only works whe arraySize > 16
     */
    private void reduceSize() {
        if (arraySize > 16 && size > 0 && arraySize / size >= 4) {
            resize(arraySize / 2);
        }
    }

    /**
     * Compute the index immediately "before" a given index
     */
    private int minusOne(int index) {
        index = index - 1;
        if (index < 0) {
            return arraySize - 1;
        }
        return index;
    }

    /**
     * Compute the index immediately "after" a given index
     */
    private int addOne(int index) {
        index = index + 1;
        if (index >= arraySize) {
            return 0;
        }
        return index;
    }

    /**
     * Adds an item of type T to the front of the deque.
     */
    @Override
    public void addFirst(Item item) {
        if (size == arraySize) {
            resize(arraySize * 2);
        }
        array[nextFirst] = item;
        nextFirst = minusOne(nextFirst);
        size++;
    }

    /**
     * Adds an item of type T to the back of the deque.
     */
    @Override
    public void addLast(Item item) {
        if (size == arraySize) {
            resize(arraySize * 2);
        }
        array[nextLast] = item;
        nextLast = addOne(nextLast);
        size++;
    }

    /**
     * Returns true if deque is empty, false otherwise.
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of items in the deque.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Prints the items in the deque from first to last, separated by a space.
     */
    @Override
    public void printDeque() {
        int index = addOne(nextFirst);
        int count = 0;
        if (size > 0) {
            while (true) {
                System.out.print(array[index]);
                count++;
                index = addOne(index);
                if (count == size) {
                    break;
                }
                System.out.print(" ");
            }
        }
        System.out.println();
    }

    /**
     * Removes and returns the item at the front of the deque. If no such item exists, returns null.
     */
    @Override
    public Item removeFirst() {
        if (size == 0) {
            return null;
        }
        int firstIndex = addOne(nextFirst);
        Item first = array[firstIndex];
        array[firstIndex] = null;
        nextFirst = firstIndex;
        size--;
        reduceSize();
        return first;
    }

    /**
     * Removes and returns the item at the back of the deque. If no such item exists, returns null.
     */
    @Override
    public Item removeLast() {
        if (size == 0) {
            return null;
        }
        int lastIndex = minusOne(nextLast);
        Item last = array[lastIndex];
        array[lastIndex] = null;
        nextLast = lastIndex;
        size--;
        reduceSize();
        return last;
    }

    /**
     * Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null. Must not alter the deque!
     */
    @Override
    public Item get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return array[(nextFirst + 1 + index) % arraySize];
    }
}
