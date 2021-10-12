package de.hglabor.utils.noriskutils.scoreboard.board

import net.axay.kspigot.runnables.KSpigotRunnable
import net.axay.kspigot.runnables.task
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.Team

class Board(val player: Player, period: Long) {
    private val scoreboardLines = mutableListOf<ScoreboardLine>()
    var title = ""
    val scoreboard = Bukkit.getScoreboardManager().newScoreboard
    val objective: Objective = scoreboard.registerNewObjective("aaa", "bbb", title)
    var currentScore = 0
    var runnable: KSpigotRunnable? = null

    init {
        objective.displaySlot = DisplaySlot.SIDEBAR

        runnable = task(true, 0, period) {
            scoreboardLines.filterIsInstance<DynamicLine>().forEach { it.update() }
        }
    }

    fun setScoreboard(): Board {
        player.scoreboard = scoreboard
        return this
    }

    fun addLine(line: ScoreboardLine) {
        line.add()
        scoreboardLines += line
    }

    fun getLine(index: Int): ScoreboardLine? = scoreboardLines.firstOrNull { it.index == index }

    abstract inner class ScoreboardLine(line: Int = -1) {
        var index = if (line == -1) currentScore++ else line
        lateinit var team: Team
        lateinit var entry: String

        init {
            register()
        }

        fun register() {
            team = scoreboard.registerNewTeam(index.toString())
            entry = entry(index)
            team.addEntry(entry)
        }

        fun add() {
            team.prefix = text
            objective.getScore(entry).score = index
        }

        val text: String
            get() = when (this) {
                is StaticLine -> staticText
                is DynamicLine -> textCallback.invoke()
                else -> ""
            }
    }

    inner class DynamicLine(
        index: Int = -1,
        var textCallback: () -> String,
    ) : ScoreboardLine(index) {

        fun set(newTextCallback: () -> String) {
            textCallback = newTextCallback
            team.prefix = text
        }

        fun update() {
            team.prefix = textCallback.invoke()
        }
    }

    inner class StaticLine(
        index: Int = -1,
        var staticText: String
    ) : ScoreboardLine(index) {

        fun set(newText: String) {
            staticText = newText
            team.prefix = text
        }
    }

    private fun entry(index: Int) = "${org.bukkit.ChatColor.values()[index]}${org.bukkit.ChatColor.WHITE}"
}

/* Example:
object JoinListener {
    init {
        listen<PlayerJoinEvent> {
            it.player.setScoreboard {
                title = "HGLabor :3"
                content {
                    +"Number won't change: ${(0..10).random()}"
                    +{ "Number will change: ${(0..10).random()}" }
                }
            }
        }
    }
}
*/

