package de.hglabor.utils.noriskutils.queue.hg;

public class HGGameInfo {
    private int maxPlayers;
    private int onlinePlayers;
    private int serverPort;
    private int timeInSeconds;
    private String gameState;
    private String serverName;

    public HGGameInfo() {
    }

    public HGGameInfo(int maxPlayers, int onlinePlayers, int timeInSeconds, String gameState, String serverName) {
        this.maxPlayers = maxPlayers;
        this.onlinePlayers = onlinePlayers;
        this.timeInSeconds = timeInSeconds;
        this.gameState = gameState;
        this.serverName = serverName;
    }

    public HGGameInfo(int maxPlayers, int onlinePlayers, int serverPort, int timeInSeconds, String gameState) {
        this.maxPlayers = maxPlayers;
        this.onlinePlayers = onlinePlayers;
        this.serverPort = serverPort;
        this.timeInSeconds = timeInSeconds;
        this.gameState = gameState;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getGameState() {
        return gameState;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getOnlinePlayers() {
        return onlinePlayers;
    }

    public void setOnlinePlayers(int onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
    }

    public int getTimeInSeconds() {
        return timeInSeconds;
    }

    public void setTimeInSeconds(int timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
    }
}
