package fun.reallyisnt.oms.common.models;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.UUID;

public class ServerData {

    private final String name;
    private final String ipAddress;
    private final int playerCount;
    private final int maxPlayerCount;

    public ServerData(String name, String ipAddress, int playerCount, int maxPlayerCount) {
        this.name = name;
        this.ipAddress = ipAddress;
        this.playerCount = playerCount;
        this.maxPlayerCount = maxPlayerCount;
    }

    public String getName() {
        return name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public int getMaxPlayerCount() {
        return maxPlayerCount;
    }

    public static ServerData deserialize(Map<String, String> map) {
        return new ServerData(map.get("name"), map.get("address"), Integer.parseInt(map.get("playercount")), Integer.parseInt(map.get("maxplayercount")));
    }

    public Map<String, String> serialize() {
        return ImmutableMap.<String, String>builder()
                .put("name", name)
                .put("address", ipAddress)
                .put("playercount", String.valueOf(playerCount))
                .put("maxplayercount", String.valueOf(maxPlayerCount))
                .build();
    }
}
