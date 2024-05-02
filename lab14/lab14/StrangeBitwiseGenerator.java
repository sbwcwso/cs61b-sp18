package lab14;

import lab14lib.Generator;

public class StrangeBitwiseGenerator implements Generator {
    private final int period;
    private final double slope;
    private int state;

    public StrangeBitwiseGenerator(int period) {
        this.state = 0;
        this.period = period;
        this.slope = 2.0 / (period - 1);
    }

    public double next() {
        state = state + 1;
        int weirdState;
//        weirdState = state & (state >>> 3) % period;
//        weirdState = state & (state >> 3) & (state >> 8) % period;
        weirdState = state & (state >> 7) % period;
        return slope * weirdState - 1;
    }
}
