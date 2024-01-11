/**
 * This class outputs all palindromes in the words file in the current directory.
 */
public class PalindromeFinder {
    public static void main(String[] args) {
        int minLength = 4;
        In in = new In("../library-sp18/data/words.txt");
        Palindrome palindrome = new Palindrome();

        while (!in.isEmpty()) {
            String word = in.readString();
            if (word.length() >= minLength && palindrome.isPalindrome(word)) {
//            if (word.length() >= minLength && palindrome.isPalindrome(word, new OffByOne())) {
//            if (palindrome.isPalindrome(word, new OffByN(2))) {
                System.out.println(word);
            }
        }
    }
}
