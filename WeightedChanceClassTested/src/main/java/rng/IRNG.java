package rng;

public interface IRNG {
    /**
     * @param l left bound inclusive
     * @param r right bound inclusive
     * @return random number in range (l, r)
     */
    double get(double l, double r);
}