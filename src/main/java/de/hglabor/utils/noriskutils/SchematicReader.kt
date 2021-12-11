package de.hglabor.utils.noriskutils

import de.hglabor.utils.noriskutils.data.SimpleLoc
import net.minecraft.nbt.NBTCompressedStreamTools
import org.bukkit.Material
import java.io.InputStream

object SchematicReader {
    fun load(inputStream: InputStream, ignoreAir: Boolean = true): Map<SimpleLoc, Material> {
        val map = mutableMapOf<SimpleLoc, Material>()

        val nbt = NBTCompressedStreamTools.a(inputStream)
        val width = nbt.g("Width")
        val height = nbt.g("Height")
        val length = nbt.g("Length")
        val blocks = nbt.m("BlockData")
        val palette = nbt.p("Palette")

        val blockPalette = mutableMapOf<Int, String>()
        //TODO 1.18.1 not sure if d() and p() is correct
        palette.d().forEach { blockPalette[palette.h(it)] = it }

        for (i in blocks.indices) {
            val block = blocks[i]
            val x = i % (width * length) % width
            val y = i / (width * length)
            val z = i % (width * length) / width
            //TODO remove [ as we dont check different variants of block
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
