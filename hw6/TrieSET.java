/******************************************************************************
 *  A set for strings only contains [a-z], implemented  using a 26-way trie.
 *
 ******************************************************************************/

/**
 * Modified from https://algs4.cs.princeton.edu/52trie/TrieSET.java.html
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
            if (!x.isString) {
                n++;
            }
            x.isString = true;
        } else {
            char c = key.charAt(d);
            int index = c - 'a';
            x.next[index] = add(x.next[index], key, d + 1);
        }
        return x;
    }

    public boolean startsWithPrefix(String prefix) {
        Node x = get(root, prefix, 0);
        return x != null;
    }
}
