package de.hglabor.utils.noriskutils.scoreboard.board

import org.bukkit.entity.Player

class BoardBuilder(val kBoard: Board) {
    var title: String
        set(value) {
            kBoard.title = value
            kBoard.objective.displayName = value
        }
        get() = kBoard.title
    var lineBuilder = LineBuilder()

    inline fun content(crossinline callback: LineBuilder.() -> Unit) {
        lineBuilder = LineBuilder().apply(callback)
    }

    fun invoke(reverse: Boolean) {
        if (reverse) reverseLines()
        lineBuilder.lines.forEach { kBoard.addLine(it) }
    }

    private fun reverseLines() {
        lineBuilder.lines.forEach {
            it.index = lineBuilder.lines.size - it.index
        }
    }

    inner class LineBuilder {
        val lines = mutableListOf<Board.ScoreboardLine>()

        operator fun String.unaryPlus() {
            lines += kBoard.StaticLine(-1, this)
        }

        operator fun (() -> String).unaryPlus() {
            lines += kBoard.DynamicLine(-1, this)
        }
    }
}

inline fun Player.setScoreboard(updatingPeriod: Long = 20, bottomToTop: Boolean = true, crossinline builder: BoardBuilder.() -> Unit): Board {
    return Board(this, updatingPeriod).apply {
        BoardBuilder(this).apply(builder).invoke(bottomToTop)
    }.setScoreboard()
}