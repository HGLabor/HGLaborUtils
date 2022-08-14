package de.hglabor.utils.noriskutils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.util.CraftMagicNumbers;

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
        Level nmsWorld = ((CraftWorld) world).getHandle();
        BlockPos bp = new BlockPos(x, y, z);
        Block block = CraftMagicNumbers.getBlock(material);
        nmsWorld.setBlock(bp, block.defaultBlockState(), applyPhysics ? 3 : 2);
    }

    public static void setBlockInNativeWorld(World world, int x, int y, int z, BlockState state, boolean applyPhysics) {
        Level nmsWorld = ((CraftWorld) world).getHandle();
        BlockPos bp = new BlockPos(x, y, z);
        nmsWorld.setBlock(bp, state, applyPhysics ? 3 : 2);
    }

    public static void setBlockInNativeChunk(World world, int x, int y, int z, BlockState state, boolean applyPhysics) {
        Level nmsWorld = ((CraftWorld) world).getHandle();
        LevelChunk chunk = nmsWorld.getChunk(x >> 4, z >> 4);
        BlockPos bp = new BlockPos(x, y, z);
        chunk.setBlockState(bp, state, applyPhysics);
    }
}
