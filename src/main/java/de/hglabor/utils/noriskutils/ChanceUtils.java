package de.hglabor.utils.noriskutils;

import java.util.Random;

public final class ChanceUtils {
    private static final Random random = new Random();

    private ChanceUtils() {
    }

    public static boolean roll(int maximalChance) {
        return random.nextInt(99) + 1 <= maximalChance;
    }

    public static int getRandomNumber(int max, int min) {
        return random.nextInt(max - min) + min;
    }
}
