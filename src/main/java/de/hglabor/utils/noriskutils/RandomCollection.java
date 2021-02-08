package de.hglabor.utils.noriskutils;

import java.util.*;

public final class RandomCollection<E> {
    private final NavigableMap<Double, E> map = new TreeMap<Double, E>();
    private final Map<E, String> names = new HashMap<>();
    private final Random random = new Random();
    private double total = 0;

    public void add(double weight, E result) {
        if (weight <= 0) return;
        total += weight;
        map.put(total, result);
    }

    public void add(String name, double weight, E result) {
        if (weight <= 0) return;
        total += weight;
        map.put(total, result);
        names.put(result, name);
    }

    public String getName(E key) {
        return names.getOrDefault(key, "");
    }

    public E getRandom() {
        double value = random.nextDouble() * total;
        return map.higherEntry(value).getValue();
    }
}
