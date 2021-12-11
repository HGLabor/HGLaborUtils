package de.hglabor.utils.noriskutils.workload

import de.hglabor.utils.noriskutils.BlockPlacer
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import java.util.*

data class PlaceableBlock(
    val worldId: UUID,
    val x: Int,
    val y: Int,
    val z: Int,
    val type: Material,
    val method: String,
) : Workload {
    override fun execute() {
        val world = Bukkit.getWorld(worldId)
        when (method) {
            "vanilla" -> world?.getBlockAt(x, y, z)?.type = type //38s
            "setBlockInNativeWorld" -> BlockPlacer.setBlockInNativeWorld(world, x, y, z, type, true) //31s
            "setBlockInNativeChunk" -> BlockPlacer.setBlockInNativeChunk(world, x, y, z, type, true) //fast af but needs relog
        }
    }

    val location: Location get() = Location(Bukkit.getWorld(worldId), x.toDouble(), y.toDouble(), z.toDouble())
}
