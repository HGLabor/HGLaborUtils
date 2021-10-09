package de.hglabor.utils.noriskutils

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Biome
import org.bukkit.generator.ChunkGenerator
import java.util.*

class VoidGenerator(private val biome: Biome = Biome.PLAINS) : ChunkGenerator() {
    override fun generateChunkData(
        world: World,
        random: Random,
        chunkX: Int,
        chunkZ: Int,
        biome: BiomeGrid,
    ): ChunkData {
        val chunkData = super.createChunkData(world)
        for (x in 0..15) {
            for (z in 0..15) {
                for (y in 0..15) {
                    biome.setBiome(x, y, z, this.biome)
                }
            }
        }
        return chunkData
    }

    override fun canSpawn(world: World, x: Int, z: Int): Boolean = true
    override fun getFixedSpawnLocation(world: World, random: Random): Location = Location(world, 0.0, 128.0, 0.0)
}
