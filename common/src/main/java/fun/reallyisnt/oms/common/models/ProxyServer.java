package fun.reallyisnt.oms.common.models;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class ProxyServer {

    private final String name;
    private final int playerCount;

    public ProxyServer(String name, int playerCount) {
        this.name = name;
        this.playerCount = playerCount;
    }

    public String getName() {
        return name;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public static ProxyServer deserialize(Map<String, String> map) {
        return new ProxyServer(map.get("name"), Integer.parseInt(map.get("playercount")));
    }

    public Map<String, String> serialize() {
        return ImmutableMap.<String, String>builder()
                .put("name", name)
                .put("playercount", String.valueOf(playerCount))
                .build();
    }
}
