package weighted.random;

import org.junit.jupiter.api.RepeatedTest;
import rng.RNG;

import java.util.Arrays;
import java.util.Map;
import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WeightedRandomBagTest {
    RNG rng = new RNG();

    @RepeatedTest(100)
    public void testFairCoin() {
        Map<String, Double> fairCoin = Map.of("HEAD", 0.5, "TAIL", 0.5);

        WeightedRandomBag<String> weightedRandomBag = new WeightedRandomBag<>(fairCoin, new RNG());

        final int COIN_TOSSES = 10_000;
        final int SIZE = fairCoin.size();
        int[] headTailsCount = new int[SIZE];

        for (int i = 0; i < COIN_TOSSES; i++) {
            switch (weightedRandomBag.peek()) {
                case "HEAD" -> headTailsCount[0]++;
                case "TAIL" -> headTailsCount[1]++;
            }
        }

        assertEquals(COIN_TOSSES, headTailsCount[0] + headTailsCount[1], "Expected heads and tails sum to equal all coin tosses!");

        double pValue = headTailsCount[Math.abs(rng.nextInt()) % SIZE] * 1.0 / COIN_TOSSES;
        double zConfInterval95 = 1.96;
        double maxError = zConfInterval95 / (2.0 * Math.sqrt(COIN_TOSSES));

        assert (pValue - maxError > 0.47 && pValue - maxError < 0.53);
    }

    @RepeatedTest(100)
    public void testFairDice() {
        Map<Integer, Double> fairDice = Map.of(1, 1.0 / 6, 2, 1.0 / 6, 3, 1.0 / 6, 4, 1.0 / 6, 5, 1.0 / 6, 6, 1.0 / 6);

        WeightedRandomBag<Integer> weightedRandomBag = new WeightedRandomBag<>(fairDice, new RNG());

        final int DICE_THROWS = 10_000;
        final int SIZE = fairDice.size();
        int[] dicePointsHitCount = new int[SIZE];

        for (int i = 0; i < DICE_THROWS; i++) {
            switch (weightedRandomBag.peek()) {
                case 1 -> dicePointsHitCount[0]++;
                case 2 -> dicePointsHitCount[1]++;
                case 3 -> dicePointsHitCount[2]++;
                case 4 -> dicePointsHitCount[3]++;
                case 5 -> dicePointsHitCount[4]++;
                case 6 -> dicePointsHitCount[5]++;
            }
        }

        int sumAllPointsHits = Arrays.stream(dicePointsHitCount).sum();

        assertEquals(DICE_THROWS, sumAllPointsHits, "Expected all hits sum to be equal to all dice throws!");

        double pValue = dicePointsHitCount[Math.abs(rng.nextInt()) % SIZE] * 1.0 / DICE_THROWS;
        double zConfInterval95 = 1.96;
        double maxError = zConfInterval95 / (2.0 * Math.sqrt(DICE_THROWS));

        assert (pValue - maxError > 0.135 && pValue - maxError < 0.175);
    }

    @RepeatedTest(100)
    public void testWeightedChanePeek() {
        double[] tilesTheoreticalPercentage = new double[]{35.0, 20.0, 15.0, 12.0, 8.0, 5.0, 3.0, 2.0};

        Map<Tile, Double> tiles = Map.of(Tile.CHERRY, tilesTheoreticalPercentage[0], Tile.LEMON, tilesTheoreticalPercentage[1], Tile.ORANGE, tilesTheoreticalPercentage[2], Tile.PLUM, tilesTheoreticalPercentage[3], Tile.WATERMELON, tilesTheoreticalPercentage[4], Tile.GRAPES, tilesTheoreticalPercentage[5], Tile.SEVEN, tilesTheoreticalPercentage[6], Tile.STAR, tilesTheoreticalPercentage[7]);

        WeightedRandomBag<Tile> weightedRandomBag = new WeightedRandomBag<>(tiles, new RNG());

        final int PICKS = 100_000;
        final int SIZE = tiles.size();
        int[] tilesHitCount = new int[SIZE];

        for (int i = 0; i < PICKS; i++) {
            switch (weightedRandomBag.peek()) {
                case CHERRY -> tilesHitCount[0]++;
                case LEMON -> tilesHitCount[1]++;
                case ORANGE -> tilesHitCount[2]++;
                case PLUM -> tilesHitCount[3]++;
                case WATERMELON -> tilesHitCount[4]++;
                case GRAPES -> tilesHitCount[5]++;
                case SEVEN -> tilesHitCount[6]++;
                case STAR -> tilesHitCount[7]++;
            }
        }

        int sumAllPointsHits = Arrays.stream(tilesHitCount).sum();

        assertEquals(PICKS, sumAllPointsHits, "Expected all hits sum to be equal to all tile picks!");

        double[] tilesPercentage = new double[tiles.size()];
        for (int i = 0; i < SIZE; i++) {
            tilesPercentage[i] = tilesHitCount[i] * 1.0 / PICKS * 100.0;
        }

        BiPredicate<Double, Double> checkInInterval = (x, y) -> y - 1 < x && x < y + 1;

        for (int i = 0; i < SIZE; i++) {
            assertTrue(checkInInterval.test(tilesPercentage[i], tilesTheoreticalPercentage[i]), "Expected hit rate percentage to be in confidence interval of theoretical percentage +-1");
        }
    }

    @RepeatedTest(10)
    public void testWeightedChanePull() {
        double[] tilesTheoreticalPercentage = new double[]{10.0, 10.0, 10.0};

        Map<Tile, Double> tiles = Map.of(Tile.CHERRY, tilesTheoreticalPercentage[0], Tile.LEMON, tilesTheoreticalPercentage[1], Tile.ORANGE, tilesTheoreticalPercentage[2]);

        WeightedRandomBag<Tile> weightedRandomBag = new WeightedRandomBag<>(tiles, new RNG());

        final int PICKS = 100;
        Tile pulledTile = weightedRandomBag.pull();

        for (int i = 0; i < PICKS; i++) {
            switch (weightedRandomBag.peek()) {
                case CHERRY -> assertNotEquals(pulledTile, Tile.CHERRY);
                case LEMON -> assertNotEquals(pulledTile, Tile.LEMON);
                default -> assertNotEquals(pulledTile, Tile.ORANGE);
            }
        }
    }
}
