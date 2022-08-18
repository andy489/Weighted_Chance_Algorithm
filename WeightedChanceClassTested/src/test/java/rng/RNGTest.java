package rng;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class RNGTest {
    @Test
    public void testMersenneTwisterNextDoubleInRange() {
        final double LEFT_BOUND = 2.0;
        final double RIGHT_BOUND = 3.0;

        final int SAMPLES = 1_000;

        RNG rng = new RNG();

        for (long i = 0; i < SAMPLES; i++) {
            double rand = rng.get(LEFT_BOUND, RIGHT_BOUND);
            assertFalse(rand <= LEFT_BOUND || rand >= RIGHT_BOUND);
        }
    }
}
