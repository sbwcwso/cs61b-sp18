import java.util.Arrays;

/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 */
public class RadixSort {
    public static final int CHAR_LENGTH = 256;

    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        String[] sortedAsciis = new String[asciis.length];
        System.arraycopy(asciis, 0, sortedAsciis, 0, asciis.length);
        int maxLength = Arrays.stream(sortedAsciis).mapToInt(String::length).max().orElse(0);
        sortHelperLSD(sortedAsciis, maxLength - 1);
        return sortedAsciis;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     *
     * @param asciis Input array of Strings
     * @param index  The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort
        if (index < 0) {
            return;
        }

        int[] counts = new int[CHAR_LENGTH];
        for (String string : asciis) {
            counts[getAsciiAtIndex(string, index)]++;
        }

        int[] start = new int[CHAR_LENGTH];
        int pos = 0;
        for (int i = 0; i < CHAR_LENGTH; i++) {
            start[i] = pos;
            pos += counts[i];
        }
        String[] sortedAsciis = new String[asciis.length];
        for (String string : asciis) {
            int asciiAtIndex = getAsciiAtIndex(string, index);
            sortedAsciis[start[asciiAtIndex]++] = string;
        }
        System.arraycopy(sortedAsciis, 0, asciis, 0, asciis.length);
        sortHelperLSD(asciis, index - 1);
    }

    /**
     * Get the ascii of char in the given index of string, if index is out of range, return 0
     */
    private static int getAsciiAtIndex(String string, int index) {
        if (index < string.length()) {
            return string.charAt(index);
        }

        return 0;
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start  int for where to start sorting in this method (includes String at start)
     * @param end    int for where to end sorting in this method (does not include String at end)
     * @param index  the index of the character the method is currently sorting on
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
    }

    public static void main(String[] args) {
        String[] original = {"Hello", "World", "Java", "Jay", "Hi", "Wo"};
        String[] sorted = sort(original);
        System.out.print("The original array is:\t");
        for (String string : original) {
            System.out.print(string + " ");
        }
        System.out.println();

        System.out.print("The sorted array is:\t");
        for (String string : sorted) {
            System.out.print(string + " ");
        }
    }
}
