package de.hglabor.utils.noriskutils;

import net.minecraft.core.BlockPosition;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.Chunk;
import net.minecraft.world.level.chunk.ChunkSection;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.util.CraftMagicNumbers;

public final class BlockPlacer {
    /**
     * 80-90K blocks per second (+150%)
     * <p>
     * This method updates block natively by calling setTypeAndData in NMS world.
     * The final effect is the same as the effect yielded by World#setBlock, but faster.
     * The reason why this method is slower than other methods is that this method still performs light update,
     * which use extensive resource.
     */
    public static void setBlockInNativeWorld(World world, int x, int y, int z, org.bukkit.Material material, boolean applyPhysics) {
        WorldServer nmsWorld = ((CraftWorld) world).getHandle();
        BlockPosition bp = new BlockPosition(x, y, z);
        Block block = CraftMagicNumbers.getBlock(material);
        nmsWorld.a(bp, block.n(), applyPhysics ? 3 : 2);
    }

    /**
     * 2.19M blocks per second (+3650%)
     * <p>
     * This methods updates block directly in the NMS chunk. It is significantly faster, but it also brings cons:
     * <p>
     * Does not perform light update
     * Does not send updated blocks to player (requires a relog)
     * Does not work if specified coordinate is in a not loaded chunk
     * <p>
     * This method is best for tasks that don't need to be displayed to players instantly e.g. minigame regeneration.
     */
    public static void setBlockInNativeChunk(World world, int x, int y, int z, org.bukkit.Material material, boolean applyPhysics) {
        try {
            WorldServer nmsWorld = ((CraftWorld) world).getHandle();
            Chunk nmsChunk = nmsWorld.getChunkIfLoaded(x >> 4, z >> 4);
            BlockPosition bp = new BlockPosition(x, y, z);
            Block block = CraftMagicNumbers.getBlock(material);
            nmsChunk.a(bp, block.n(), applyPhysics);
        } catch (Exception ignore) {
        }
    }
}
