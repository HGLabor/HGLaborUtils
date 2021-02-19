package de.hglabor.utils.noriskutils.queue.hg;

public class HGQueuePlayerInfo {
    private String uuid;
    private String name;
    private int port;

    public HGQueuePlayerInfo(String name, String uuid, int port) {
        this.name = name;
        this.uuid = uuid;
        this.port = port;
    }

    public HGQueuePlayerInfo(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public int getPort() {
        return port;
    }
}
