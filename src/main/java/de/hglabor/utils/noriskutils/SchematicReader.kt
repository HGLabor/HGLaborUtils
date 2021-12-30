package de.hglabor.utils.noriskutils

import de.hglabor.utils.noriskutils.data.SimpleLoc
import net.minecraft.nbt.NbtIo
import org.bukkit.Material
import java.io.InputStream

object SchematicReader {
  fun parseSchematic(inputStream: InputStream, ignoreAir: Boolean = true): Map<SimpleLoc, Material> {
    val map = mutableMapOf<SimpleLoc, Material>()

    val nbt = NbtIo.readCompressed(inputStream)
    val width = nbt.getInt("Width")
    val height = nbt.getInt("Height")
    val length = nbt.getInt("Length")
    val blocks = nbt.getByteArray("BlockData")
    val palette = nbt.getCompound("Palette")

    val blockPalette = mutableMapOf<Int, String>()
    palette.allKeys.forEach { blockPalette[palette.getInt(it)] = it }

    for (i in blocks.indices) {
      val block = blocks[i]
      val x = i % (width * length) % width
      val y = i / (width * length)
      val z = i % (width * length) / width
      //TODO remove [ as we dont check different variants of block (yet)
      val string = (blockPalette[block.toInt()] ?: "AIR")
        .replace("minecraft:".toRegex(), "")
        .uppercase()
        .substringBefore("[")
      val material = Material.valueOf(string)
      if (ignoreAir && material.isAir) continue
      map[SimpleLoc(x, y, z)] = material
    }
    return map
  }
}
