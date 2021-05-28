package fun.reallyisnt.oms.core;

public enum Config {
    REDIS_ENABLED("redis.enabled"),
    REDIS_HOST("redis.host"),
    REDIS_PASSWORD("redis.password");

    public String path;

    Config(String s) {
        this.path = s;
    }
}
