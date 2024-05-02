package lab14;

import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator {
    private int period;
    private double ratio;
    private double slope;
    private int state;


    public AcceleratingSawToothGenerator(int period, double ratio) {
        this.state = 0;
        this.period = period;
        this.ratio = ratio;
        this.slope = 2.0 / (this.period - 1);
    }

    public double next() {
        if (state == period) {
            state = 0;
            period = (int) (period * ratio);
            if (period > 1) {
                slope = 2.0 / (period - 1);
            }
        }
        if (period <= 1) {
            return 0;
        }
        double value = slope * state - 1;
        state += 1;
        return value;
    }
}
