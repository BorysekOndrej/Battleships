package no.ntnu.tdt4240.y2022.group23.battleshipsserver.server;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class HealthCheck {
    private static final JedisPool pool = RedisStorage.getInstance().pool;
    private final Map<String, String> result = new HashMap<>();

    public HealthCheck(){
        if (!redisConnection()){
            return;
        }
        matchmaking_queues();
        // idea: check the number of free invite codes?
    }

    private boolean redisConnection(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        try (Jedis jedis = pool.getResource()) {
            jedis.set("healthcheck_timestamp", timestamp.toString());
        } catch (JedisConnectionException e){
            result.put("error_redis", e.toString());
            return false;
        }

        return true;
    }


    private void matchmaking_queues(){
        try (Jedis jedis = pool.getResource()) {
            for (String queue: RedisStorage.matchmakingQueues) {
                long queueLen = jedis.llen(queue);
                result.put(queue+"_count", String.valueOf(queueLen));

                if (queueLen > 2){
                    result.put("warning_"+queue, "This queue is longer than 2, which should happen only temporarily.");
                }
            }
        }
    }


    public Map<String, String> getResult(){
        return result;
    }
}
