public class HuffmanDecoder {
    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("Usage: java HuffmanEncoder filename newFilename");
        }

        ObjectReader huf = new ObjectReader(args[0]);
        BinaryTrie binaryTrie = (BinaryTrie) huf.readObject();
        BitSequence sequences = (BitSequence) huf.readObject();
        StringBuilder result = new StringBuilder();
        while (sequences.length() > 0) {
            Match match = binaryTrie.longestPrefixMatch(sequences);
            result.append(match.getSymbol());
            sequences = sequences.allButFirstNBits(match.getSequence().length());
        }
        FileUtils.writeCharArray(args[1], result.toString().toCharArray());
    }
}
