package de.hglabor.utils.noriskutils;

import net.minecraft.server.v1_16_R3.Block;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.ChunkSection;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftMagicNumbers;

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
        net.minecraft.server.v1_16_R3.World nmsWorld = ((CraftWorld) world).getHandle();
        BlockPosition bp = new BlockPosition(x, y, z);
        Block block = CraftMagicNumbers.getBlock(material);
        nmsWorld.setTypeAndData(bp, block.getBlockData(), applyPhysics ? 3 : 2);
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
            net.minecraft.server.v1_16_R3.World nmsWorld = ((CraftWorld) world).getHandle();
            net.minecraft.server.v1_16_R3.Chunk nmsChunk = nmsWorld.getChunkAt(x >> 4, z >> 4);
            BlockPosition bp = new BlockPosition(x, y, z);
            Block block = CraftMagicNumbers.getBlock(material);
            nmsChunk.setType(bp, block.getBlockData(), applyPhysics);
        } catch (Exception ignore) {
        }
    }

    /**
     * 7.9M blocks per second (+13166%)
     * <p>
     * This method has all cons that method #2 has.
     * Sometimes it produces unstable results (e.g. lose some blocks). Still finding reason.
     * <p>
     * Use for fun.
     */
    public static void setBlockInNativeChunkSection(World world, int x, int y, int z, org.bukkit.Material material) {
        net.minecraft.server.v1_16_R3.World nmsWorld = ((CraftWorld) world).getHandle();
        net.minecraft.server.v1_16_R3.Chunk nmsChunk = nmsWorld.getChunkAt(x >> 4, z >> 4);
        Block block = CraftMagicNumbers.getBlock(material);

        ChunkSection cs = nmsChunk.getSections()[y >> 4];
        if (cs == nmsChunk.a()) {
            cs = new ChunkSection(y >> 4 << 4);
            nmsChunk.getSections()[y >> 4] = cs;
        }
        cs.setType(x & 15, y & 15, z & 15, block.getBlockData());
    }

    /**
     * 13.8M-14M blocks per second (+23333%)
     * <p>
     * This method directly changes block data in DataPalette, the instance that Minecraft uses to store block data in byte. It has all the cons that #2 and #3 has and since Chunks are loaded from DataPalette, this method would not change Chunks in memory unless you reload the chunk (e.g. by restarting server.)
     * <p>
     * Maybe useful for massive minigame map pregeneration.
     */
    public static void setBlockInNativeDataPalette(World world, int x, int y, int z, org.bukkit.Material material, boolean applyPhysics) {
        net.minecraft.server.v1_16_R3.World nmsWorld = ((CraftWorld) world).getHandle();
        net.minecraft.server.v1_16_R3.Chunk nmsChunk = nmsWorld.getChunkAt(x >> 4, z >> 4);
        Block block = CraftMagicNumbers.getBlock(material);

        ChunkSection cs = nmsChunk.getSections()[y >> 4];
        if (cs == nmsChunk.a()) {
            cs = new ChunkSection(y >> 4 << 4);
            nmsChunk.getSections()[y >> 4] = cs;
        }

        if (applyPhysics)
            cs.getBlocks().setBlock(x & 15, y & 15, z & 15, block.getBlockData());
        else
            cs.getBlocks().b(x & 15, y & 15, z & 15, block.getBlockData());
    }
}
