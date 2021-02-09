package de.hglabor.utils.noriskutils;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import de.hglabor.utils.noriskutils.feast.Feast;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public final class WorldEditUtils {
    private WorldEditUtils() {
    }

    public static void pasteSchematic(World world, Location startLocation, File file) {
        ClipboardFormat format = ClipboardFormats.findByFile(file);
        if (format == null) {
            return;
        }
        try {
            ClipboardReader reader = format.getReader(new FileInputStream(file));
            Clipboard clipboard = reader.read();
            paste(world, startLocation, clipboard);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static void paste(World world, Location startLocation, Clipboard clipboard) {
        try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(BukkitAdapter.adapt(world), -1)) {
            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(BukkitAdapter.asBlockVector(startLocation))
                    .ignoreAirBlocks(false)
                    .build();
            Operations.complete(operation);
        } catch (WorldEditException e) {
            e.printStackTrace();
        }
    }

    public static void createCylinder(World world, Location startLocation, int radius, boolean filled, int height, Material block) {
        try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(BukkitAdapter.adapt(world), -1)) {
            editSession.setFastMode(true);
            editSession.makeCylinder(BukkitAdapter.asBlockVector(startLocation), BukkitAdapter.asBlockState(new ItemStack(block)), radius, height, filled);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
