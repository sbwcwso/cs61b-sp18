package lab14;


import lab14lib.Generator;

public class SawToothGenerator implements Generator {
    private final int period;
    private final double slope;
    private int state;

    public SawToothGenerator(int period) {
        this.state = 0;
        this.period = period;
        this.slope = 2.0 / (period - 1);
    }

    public double next() {
        state = (state + 1) % period;
        return slope * state - 1;
    }
}
