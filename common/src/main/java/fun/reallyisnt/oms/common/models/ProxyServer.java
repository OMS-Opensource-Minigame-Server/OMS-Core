package fun.reallyisnt.oms.common.models;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public record ProxyServer(String name, int playerCount) {

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
