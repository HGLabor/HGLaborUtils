package de.hglabor.utils.noriskutils.tick;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractTickHook implements TickHook {

    private final Set<Callback> tasks = new HashSet<>();
    private int tick = 0;

    protected void onTick() {
        for (Callback r : this.tasks) {
            r.onTick(this.tick);
        }
        this.tick++;
    }

    @Override
    public int getCurrentTick() {
        return this.tick;
    }

    @Override
    public void addCallback(Callback runnable) {
        this.tasks.add(runnable);
    }

    @Override
    public void removeCallback(Callback runnable) {
        this.tasks.remove(runnable);
    }

}
