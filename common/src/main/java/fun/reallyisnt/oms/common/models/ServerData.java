package fun.reallyisnt.oms.common.models;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.UUID;

public record ServerData(String name, String ipAddress, int playerCount, int maxPlayerCount) {

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
