/******************************************************************************
 *  Compilation:  javac TrieSET.java
 *  Execution:    java TrieSET < words.txt
 *  Dependencies: StdIn.java
 *  Data files:   https://algs4.cs.princeton.edu/52trie/shellsST.txt
 *
 *  A set for extended ASCII strings, implemented  using a 256-way trie.
 *
 *  Sample client reads in a list of words from standard input and
 *  prints out each word, removing any duplicates.
 *
 ******************************************************************************/

/**
 * The {@code TrieSET} class represents an ordered set of strings over
 * the extended ASCII alphabet.
 * It supports the usual <em>add</em>, <em>contains</em>, and <em>delete</em>
 * methods. It also provides character-based methods for finding the string
 * in the set that is the <em>longest prefix</em> of a given prefix,
 * finding all strings in the set that <em>start with</em> a given prefix,
 * and finding all strings in the set that <em>match</em> a given pattern.
 * <p>
 * This implementation uses a 26-way trie.
 * The <em>add</em>, <em>contains</em>, <em>delete</em>, and
 * <em>longest prefix</em> methods take time proportional to the length
 * of the key (in the worst case). Construction takes constant time.
 * <p>
 * For additional documentation, see
 * <a href="https://algs4.cs.princeton.edu/52trie">Section 5.2</a> of
 * <i>Algorithms in Java, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class TrieSET {
    private static final int R = 26;        // just support a-z

    private Node root;      // root of trie
    private int n;          // number of keys in trie

    // R-way trie node
    private static class Node {
        private final Node[] next = new Node[R];
        private boolean isString;
    }

    /**
     * Initializes an empty set of strings.
     */
    public TrieSET() {
    }

    /**
     * Does the set contain the given key?
     *
     * @param key the key
     * @return {@code true} if the set contains {@code key} and
     * {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean contains(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }
        Node x = get(root, key, 0);
        if (x == null) {
            return false;
        }
        return x.isString;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) {
            return null;
        }
        if (d == key.length()) {
            return x;
        }
        char c = key.charAt(d);
        int index = c - 'a';
        return get(x.next[index], key, d + 1);
    }

    /**
     * Adds the key to the set if it is not already present.
     *
     * @param key the key to add
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void add(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to add() is null");
        }
        root = add(root, key, 0);
    }

    private Node add(Node x, String key, int d) {
        if (x == null) {
            x = new Node();
        }
        if (d == key.length()) {
            if (!x.isString) n++;
            x.isString = true;
        } else {
            char c = key.charAt(d);
            int index = c - 'a';
            x.next[index] = add(x.next[index], key, d + 1);
        }
        return x;
    }

    /**
     * Returns the number of strings in the set.
     *
     * @return the number of strings in the set
     */
    public int size() {
        return n;
    }

    /**
     * Is the set empty?
     *
     * @return {@code true} if the set is empty, and {@code false} otherwise
     */
    public boolean isEmpty() {
        return size() == 0;
    }


    public boolean startsWithPrefix(String prefix) {
        Node x = get(root, prefix, 0);
        return x != null;
    }
}
