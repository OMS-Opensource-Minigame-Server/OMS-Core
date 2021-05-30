package fun.reallyisnt.oms.common.models;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.UUID;


public class OnlinePlayer {

    private final String name;
    private final UUID uuid;
    private final String server;
    private final String proxy;

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

    public OnlinePlayer(String name, UUID uuid, String server, String proxy) {
        this.name = name;
        this.uuid = uuid;
        this.server = server;
        this.proxy = proxy;
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getServer() {
        return server;
    }

    public String getProxy() {
        return proxy;
    }
}
