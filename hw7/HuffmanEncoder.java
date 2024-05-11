import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HuffmanEncoder {
    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        Map<Character, Integer> frequencyTable = new HashMap<>();
        for (char c : inputSymbols) {
            frequencyTable.put(c, frequencyTable.getOrDefault(c, 0) + 1);
        }
        return frequencyTable;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Usage: java HuffmanEncoder filename");
        }

        char[] inputSymbols = FileUtils.readFile(args[0]);

        Map<Character, Integer> frequencyTable = buildFrequencyTable(inputSymbols);
        BinaryTrie binaryTrie = new BinaryTrie(frequencyTable);

        ObjectWriter huf = new ObjectWriter(args[0] + ".huf");
        huf.writeObject(binaryTrie);

        Map<Character, BitSequence> lookupTable = binaryTrie.buildLookupTable();
        List<BitSequence> sequences = new ArrayList<>();
        for (char c : inputSymbols) {
            sequences.add(lookupTable.get(c));
        }
        BitSequence assembleSequences = BitSequence.assemble(sequences);
        huf.writeObject(assembleSequences);
    }
}
