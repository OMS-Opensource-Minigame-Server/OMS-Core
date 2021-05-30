package fun.reallyisnt.oms.common;

public enum Config {

    REDIS_ENABLED("redis.enabled"),
    REDIS_HOST("redis.host"),
    REDIS_PASSWORD("redis.password"),

    AGONES_ENABLED("agones.enabled");

    private final String path;

    Config(String s) {
        this.path = s;
    }

    public String getPath() {
        return path;
    }
}
