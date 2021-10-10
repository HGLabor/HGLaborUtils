package de.hglabor.utils.noriskutils.scoreboard

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.Team

class MultiLineBuilder {
    val lines = ArrayList<String>()
    operator fun String.unaryPlus() {
        lines += this
    }
}

class KBoard(val player: Player, title: String) {
    val scoreboard = Bukkit.getScoreboardManager().newScoreboard
    val objective = scoreboard.registerNewObjective("aaa", "bbb", title)
    var currentLines = mutableListOf<String>()

    init {
        objective.displaySlot = DisplaySlot.SIDEBAR
        player.scoreboard = scoreboard
    }

    inline fun updateLines(topDown: Boolean = true, forceClear: Boolean = true, builder: MultiLineBuilder.() -> Unit) {
        val lines = if (topDown)
            MultiLineBuilder().apply(builder).lines.reversed().toMutableList()
        else
            MultiLineBuilder().apply(builder).lines

        if (forceClear) {
            currentLines.forEachIndexed { index, _ ->
                val entry = entry(index)
                player.scoreboard.getEntryTeam(entry)?.unregister()
                player.scoreboard.resetScores(entry)
            }
        }

        lines.forEachIndexed { index, text -> createLine(index, text) }

        currentLines = lines
    }

    fun createLine(index: Int, text: String) {
        val entry = entry(index)
        val team: Team = player.scoreboard.registerNewTeam(index.toString())
        team.addEntry(entry)
        team.prefix = text
        objective.getScore(entry).score = index
    }

    fun addLine(line: Int = -1, text: String) {
        val newLines = currentLines.toMutableList()
        if (newLines.isEmpty()) {
            newLines.add(text)
        } else if (line != -1) {
            val index = newLines.size - (line - 1)
            if (index < 0) newLines.add(text) else newLines.add(index, text)
        } else {
            newLines.add(text)
        }
        updateLines(topDown = false) { newLines.forEach { +it } }
    }

    fun removeLine(line: Int) {
        val newLines = currentLines.toMutableList()
        val index = newLines.size - (line)
        if (newLines.isEmpty() || index < 0) return
        newLines.remove(newLines[index])
        updateLines(topDown = false) { newLines.forEach { +it } }
    }

    fun updateLine(line: Int, text: String, topDown: Boolean = true) {
        val index = if (topDown) currentLines.size - (line) else line + 1
        val team: Team = player.scoreboard.getTeam(index.toString()) ?: return
        team.prefix = text
    }

    fun entry(index: Int) = "${ChatColor.values()[index]}${ChatColor.WHITE}"
}
