package fun.reallyisnt.oms.common.models;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.UUID;

public record OnlinePlayer(String name, UUID uuid, String server, String proxy) {

    public static OnlinePlayer deserialize(Map<String, String> map) {
        return new OnlinePlayer(map.get("name"), UUID.fromString(map.get("uuid")), map.get("server"), map.get("proxy"));
    }

    public Map<String, String> serialize() {
        return ImmutableMap.<String, String>builder()
                .put("name", name)
                .put("uuid", uuid.toString())
                .put("server", server)
                .put("proxy", proxy)
                .build();
    }
}
