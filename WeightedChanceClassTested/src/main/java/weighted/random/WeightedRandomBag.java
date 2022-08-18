package weighted.random;

import rng.RNG;

import java.util.Map;
import java.util.TreeMap;

public class WeightedRandomBag<E> implements IWeightedRandomBag<E> {
    private final TreeMap<Double, E> weightItemsBag;
    private final RNG rng;
    private double total;

    {
        total = 0.0;
        weightItemsBag = new TreeMap<>();
    }

    public WeightedRandomBag(Map<E, Double> items, RNG rng) {
        this.rng = rng;
        for (Map.Entry<E, Double> entry : items.entrySet()) {
            E key = entry.getKey();
            Double val = entry.getValue();

            total += val;
            weightItemsBag.put(total, key);
        }
    }


    @Override
    public E peek() {
        if (total == 0) {
            return null;
        }

        double rand = rng.get(0, total);
        return weightItemsBag.higherEntry(rand).getValue();
    }

    @Override
    public E pull() {
        if (total == 0) {
            return null;
        }

        double rand = rng.get(0, total);
        Map.Entry<Double, E> pulledEntry = weightItemsBag.higherEntry(rand);
        Double pulledKey = pulledEntry.getKey();

        weightItemsBag.remove(pulledKey);
        Double toRemoveFromTotal = weightItemsBag.lowerKey(pulledKey);

        if (toRemoveFromTotal != null) {
            total -= toRemoveFromTotal;
        } else {
            total -= pulledKey;
        }

        return pulledEntry.getValue();
    }
};