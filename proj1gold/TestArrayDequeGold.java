import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestArrayDequeGold {
    @Test
    public void testStudentArrayDeque() {
        StudentArrayDeque<Integer> sad = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> ads = new ArrayDequeSolution<>();
        final int itemNumbers = 13;
        StringBuilder stringBuilder = new StringBuilder();
        String operation;

        for (int i = 0; i < itemNumbers; i += 1) {
            double numberBetweenZeroAndOne = StdRandom.uniform();
            if (numberBetweenZeroAndOne < 0.5) {
                operation = "addLast(" + i + ")\n";
                stringBuilder.append(operation);
                sad.addLast(i);
                ads.addLast(i);
            } else {
                operation = "addFirst(" + i + ")\n";
                stringBuilder.append(operation);
                sad.addFirst(i);
                ads.addFirst(i);
            }
        }

        for (int i = 0; i < itemNumbers; i += 1) {
            double numberBetweenZeroAndOne = StdRandom.uniform();
            if (numberBetweenZeroAndOne < 0.5) {
                stringBuilder.append("removeLast()\n");
                assertEquals(stringBuilder.toString(), sad.removeLast(), ads.removeLast());
            } else {
                stringBuilder.append("removeFirst()\n");
                assertEquals(stringBuilder.toString(), sad.removeFirst(), ads.removeFirst());
            }
        }
    }
}
