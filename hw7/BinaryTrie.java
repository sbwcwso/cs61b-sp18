import edu.princeton.cs.algs4.MinPQ;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @ https://algs4.cs.princeton.edu/55compression/Huffman.java.html
 */
public class BinaryTrie implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Node root;
    private final Map<Character, Integer> frequencyTable;

    private static class Node implements Comparable<Node>, Serializable {
        private static final long serialVersionUID = 2L;
        private final char ch;
        private final int freq;
        private final Node left, right;

        Node(char ch, int freq, Node left, Node right) {
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        // is the node a leaf node?
        private boolean isLeaf() {
            assert ((left == null) && (right == null)) || ((left != null) && (right != null));
            return (left == null) && (right == null);
        }

        // compare, based on frequency
        public int compareTo(Node that) {
            return this.freq - that.freq;
        }
    }

    // build the Huffman trie according to the frequencyTable
    private Node buildTrie() {

        // initialize priority queue with singleton trees
        MinPQ<Node> pq = new MinPQ<Node>();
        frequencyTable.forEach((key, value) -> {
            if (value > 0) {
                pq.insert(new Node(key, value, null, null));
            }
        });

        // merge two smallest trees
        while (pq.size() > 1) {
            Node left = pq.delMin();
            Node right = pq.delMin();
            Node parent = new Node('\0', left.freq + right.freq, left, right);
            pq.insert(parent);
        }
        return pq.delMin();
    }

    /**
     * Build a Huffman decoding trie
     *
     * @param frequencyTable: maps a symbols of type character to their relative frequencies.
     */
    public BinaryTrie(Map<Character, Integer> frequencyTable) {
        this.frequencyTable = Collections.unmodifiableMap(frequencyTable);
        this.root = buildTrie();
    }

    /**
     * find the longest prefix that matches the given querySequence
     *
     * @param querySequence:
     * @return a Match object for that match
     */
    public Match longestPrefixMatch(BitSequence querySequence) {
        Node current = root;
        BitSequence sequence = new BitSequence();
        for (int i = 0; i < querySequence.length(); i++) {
            if (querySequence.bitAt(i) == 0) {
                current = current.left;
                sequence = sequence.appended(0);
            } else {
                current = current.right;
                sequence = sequence.appended(1);
            }

            if (current.isLeaf()) {
                return new Match(sequence, current.ch);
            }
        }

        return null;
    }

    /**
     * @return a inverse of the coding trie.
     */
    public Map<Character, BitSequence> buildLookupTable() {
        Map<Character, BitSequence> lookupTable = new HashMap<>();
        buildCode(lookupTable, root, new BitSequence());
        return lookupTable;
    }

    private void buildCode(Map<Character, BitSequence> lookupTable, Node x, BitSequence sequence) {
        if (!x.isLeaf()) {
            buildCode(lookupTable, x.left, sequence.appended(0));
            buildCode(lookupTable, x.right, sequence.appended(1));
        } else {
            lookupTable.put(x.ch, sequence);
        }
    }
}
