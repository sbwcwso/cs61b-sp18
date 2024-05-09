import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

public class Boggle {

    // File path of dictionary file
    static String dictPath = "words.txt";
    private static TrieSET wordsDict;
    private static char[][] boardArray;

    /**
     * Solves a Boggle puzzle.
     *
     * @param k             The maximum number of words to return.
     * @param boardFilePath The file path to Boggle board file.
     * @return a list of words found in given Boggle board.
     * The Strings are sorted in descending order of length.
     * If multiple words have the same length,
     * have them in ascending alphabetical order.
     */
    public static List<String> solve(int k, String boardFilePath) {
        if (k <= 0) {
            throw new IllegalArgumentException("k in non-positive");
        }
        readBoardFile(boardFilePath);
        readWords();
        return getKLongestWords(k);
    }

    private static void readBoardFile(String boardFilePath) {
        In in = new In(boardFilePath);
        boardArray = new char[0][];
        if (in.exists() && !in.isEmpty()) {
            ArrayList<String> lines = new ArrayList<>();
            int length = -1;
            boolean lengthMismatch = false;

            while (!in.isEmpty()) {
                String line = in.readLine();
                length = (length == -1) ? line.length() : length;
                if (line.length() != length) {
                    lengthMismatch = true;
                    break;
                }
                lines.add(line);
            }

            if (lengthMismatch) {
                in.close();
                throw new IllegalArgumentException("Error: Lines in the board file do not have "
                    + "the same length.");
            }

            boardArray = new char[lines.size()][];
            for (int i = 0; i < lines.size(); i++) {
                boardArray[i] = lines.get(i).toCharArray();
            }
        }
        in.close();
    }

    private static void readWords() {
        wordsDict = new TrieSET();
        In in = new In(dictPath);
        if (!in.exists()) {
            throw new IllegalArgumentException("The dictionary file does not exist.");
        }
        while (true) {
            String words = in.readLine();
            if (words == null) {
                break;
            }
            words = words.toLowerCase();
            if (words.matches("^[a-z]+$")) {
                wordsDict.add(words);
            }
        }
        in.close();
    }

    private static class Position {
        private final String word;
        final int x;
        final int y;
        private final List<Position> path;
        private List<Position> unusedNeighbors;

        private Position(String word, int x, int y, List<Position> path) {
            this.word = word + boardArray[x][y];
            this.x = x;
            this.y = y;
            // add current position to the path
            this.path = new ArrayList<>(path);
            this.path.add(this);
            unusedNeighbors = null;
        }

        public String getWord() {
            return word;
        }

        private List<Position> getPath() {
            return Collections.unmodifiableList(path);
        }

        public List<Position> getNeighbors() {
            if (unusedNeighbors == null) {
                calcUnusedNeighbors();
            }
            return Collections.unmodifiableList(unusedNeighbors);
        }

        private void calcUnusedNeighbors() {
            unusedNeighbors = new ArrayList<>();
            for (int i = x - 1; i <= x + 1; i++) {
                for (int j = y - 1; j <= y + 1; j++) {
                    Position position;
                    try {
                        position = new Position(word, i, j, path);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        continue;
                    }
                    if (!path.contains(position)) {
                        unusedNeighbors.add(position);
                    }
                }
            }
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Position position = (Position) o;
            return x == position.x && y == position.y;
        }

        public int hashCode() {
            return x * boardArray.length + y;
        }

    }

    private static List<String> getKLongestWords(int k) {
        Comparator<String> comparator = (String s1, String s2) -> {
            if (s1.length() != s2.length()) {
                return Integer.compare(s1.length(), s2.length());
            }
            return s2.compareTo(s1);
        };
        PriorityQueue<String> words = new PriorityQueue<>(comparator);
        Stack<Position> positions = new Stack<>();
        for (int x = 0; x < boardArray.length; x++) {
            for (int y = 0; y < boardArray[0].length; y++) {
                positions.add(new Position("", x, y, new ArrayList<>()));
                while (!positions.isEmpty()) {
                    Position position = positions.pop();
                    String word = position.getWord();
                    if (wordsDict.startsWithPrefix(word)) {
                        for (Position p : position.getNeighbors()) {
                            positions.push(p);
                        }
                        if (word.length() >= 3 && wordsDict.contains(word)
                            && !words.contains(word)) {
                            if (words.size() < k) {
                                words.add(word);
                            } else if (comparator.compare(word, words.peek()) > 0) {
                                words.poll();
                                words.add(word);
                            }
                        }
                    }
                }
            }
        }
        List<String> result = new ArrayList<>();
        while (!words.isEmpty()) {
            result.add(words.poll());
        }
        Collections.reverse(result);
        return result;
    }

    public static void main(String[] args) {

        // test readBoardFile
        char[][] exceptedArray = {
            {'n', 'e', 's', 's'},
            {'t', 'a', 'c', 'k'},
            {'b', 'm', 'u', 'h'},
            {'e', 's', 'f', 't'}
        };

        readBoardFile("exampleBoard.txt");
        org.junit.Assert.assertArrayEquals(exceptedArray, boardArray);


        // test readWords;
        readWords();
        org.junit.Assert.assertTrue(wordsDict.contains("thumbtacks"));
        org.junit.Assert.assertFalse(wordsDict.contains("helo"));

        List<String> actual = solve(7, "exampleBoard.txt");
        List<String> excepted = Arrays.asList("thumbtacks", "thumbtack",
            "setbacks", "setback", "ascent", "humane", "smacks");
        org.junit.Assert.assertEquals(excepted, actual);

        dictPath = "trivial_words.txt";
        actual = solve(20, "exampleBoard2.txt");
        excepted = Arrays.asList("aaaaa", "aaaa");
        org.junit.Assert.assertEquals(excepted, actual);

        dictPath = "words.txt";
        long startTime, endTime, duration;
        startTime = System.currentTimeMillis();
        solve(14, "smallBoard2.txt");
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("smallBoard2： " + duration + " ms");

        startTime = System.currentTimeMillis();
        solve(14, "smallBoard.txt");
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("smallBoard： " + duration + " ms");
    }
}
