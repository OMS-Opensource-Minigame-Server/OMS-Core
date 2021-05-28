package fun.reallyisnt.oms.core.modules.dms.redis;

import fun.reallyisnt.oms.core.OMSCore;
import redis.clients.jedis.JedisPubSub;

public class RedisListener extends JedisPubSub {

    public void onMessage(String channel, String message) {
        String[] channelSplit = channel.split(":");

        if (channelSplit.length != 1 || !channelSplit[0].equals("notifications")) {

        }
    }
}
