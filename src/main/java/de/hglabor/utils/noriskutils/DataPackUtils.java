package de.hglabor.utils.noriskutils;

import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public final class DataPackUtils {
    private DataPackUtils() {
    }

    public static void generateNewWorld(String firstPath, String datapackName) {
        try {
            long seed = ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE);
            Path path = Paths.get(firstPath, "schematic", "datapacks", datapackName, "data", "minecraft", "dimension", "overworld.json");
            String text = new String(Files.readAllBytes(path));
            String newText = text.replaceAll("\"seed\":\\s[0-9]+", "\"seed\": " + seed);
            path.toFile().delete();
            Files.write(path, newText.getBytes(), StandardOpenOption.CREATE_NEW);
            Bukkit.createWorld(new WorldCreator("world"));
            Bukkit.unloadWorld("world", true);
            File region = Paths.get("world/region").toFile();
            Arrays.stream(region.listFiles()).forEach(File::delete);
            Bukkit.createWorld(new WorldCreator("world"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
