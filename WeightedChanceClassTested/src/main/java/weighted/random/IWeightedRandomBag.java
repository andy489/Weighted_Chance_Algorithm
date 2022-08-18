package weighted.random;

public interface IWeightedRandomBag<E> {
    /**
     * @return random element from weighted chance bag without removing it
     */
    E peek();

    /**
     * @return random element from weighted chance bag and remove it from bag
     */
    E pull();
}

