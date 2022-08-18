package rng;

import org.apache.commons.math3.random.MersenneTwister;

public class RNG implements IRNG {
    private final MersenneTwister mt;

    public RNG() {
        mt = new MersenneTwister();
    }

    public RNG(final long SEED) {
        mt = new MersenneTwister(SEED);
    }

    @Override
    public double get(double l, double r) {
        return l + (r - l) * mt.nextDouble();
    }

    public int nextInt() {
        return mt.nextInt();
    }
}
