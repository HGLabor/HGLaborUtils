package de.hglabor.utils.noriskutils.tick;

public interface TickHook extends AutoCloseable {

    /**
     * Starts the hook
     */
    void start();

    /**
     * Stops the hook
     */
    @Override
    void close();

    /**
     * Gets the current tick number
     *
     * @return the current tick
     */
    int getCurrentTick();

    /**
     * Adds a callback to be called each time the tick increments
     *
     * @param runnable the task
     */
    void addCallback(Callback runnable);

    /**
     * Removes a callback
     *
     * @param runnable the callback
     */
    void removeCallback(Callback runnable);

    interface Callback {
        void onTick(int currentTick);
    }

}
