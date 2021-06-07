package fun.reallyisnt.oms.common;

public enum Config {

    REDIS_ENABLED("redis.enabled", "true"),
    REDIS_HOST("redis.host"),
    REDIS_PORT("redis.port", "6379"),
    REDIS_PASSWORD("redis.password"),

    AGONES_ENABLED("agones.enabled", "true");

    private final String path;
    private final String defaultValue;

    Config(String path) {
        this.path = path;
        this.defaultValue = null;
    }

    Config(String path, String defaultValue) {
        this.path = path;
        this.defaultValue = defaultValue;
    }

    public String getPath() {
        return path;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
