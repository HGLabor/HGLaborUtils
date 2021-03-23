package de.hglabor.utils.noriskutils;

public final class TimeConverter {
    private TimeConverter() {
    }

    public static String stringify(int totalSecs) {
        if (totalSecs > 3600) {
            return stringify(totalSecs,"%02d:%02d:%02d");
        }
        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public static String stringify(int totalSecs, String format) {
        int hours = totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;
        return String.format(format, hours, minutes, seconds);
    }
}
