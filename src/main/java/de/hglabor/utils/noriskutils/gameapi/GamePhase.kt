package de.hglabor.utils.noriskutils.gameapi

import org.bukkit.event.HandlerList
import org.bukkit.event.Listener

abstract class GamePhase(private val gamePhaseManager: IGamePhaseManager) {
    val listeners = mutableListOf<Listener>()
    val tickable = mutableListOf<Tickable>()
    abstract fun tick(tick: Int)
    abstract fun nextPhase(): GamePhase

    protected fun startNextPhase() {
        listeners.forEach { HandlerList.unregisterAll(it) }
        gamePhaseManager.phase = nextPhase()
    }

    protected fun executeTickables() = tickable.forEach { it.onTick() }
}
