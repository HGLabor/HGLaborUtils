package de.hglabor.utils.noriskutils;

public final class TimeConverter {
    private TimeConverter() {
    }

    public static String stringify(int totalSecs) {
        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
