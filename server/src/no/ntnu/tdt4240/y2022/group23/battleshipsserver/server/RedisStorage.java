package no.ntnu.tdt4240.y2022.group23.battleshipsserver.server;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisStorage {
    private static RedisStorage INSTANCE;
    public JedisPool pool;

    private RedisStorage(){
         pool = new JedisPool("localhost", 6379);
    }

    // todo: set TTL

    String getUserIDByToken(String token){
        try (Jedis jedis = pool.getResource()) {
            return jedis.get("user_token_" + token);
        }
    }

    String getUserTokenByID(String userID){
        try (Jedis jedis = pool.getResource()) {
            return jedis.get("user_id_" + userID);
        }
    }

    void deleteUser(String userID){
        try (Jedis jedis = pool.getResource()) {
            String userToken = jedis.get("user_id_" + userID);
            jedis.del("user_id_" + userID);
            jedis.del("user_token_" + userToken);
        }
    }

    void setNewUserToken(String userID, String token){
        deleteUser(userID);

        try (Jedis jedis = pool.getResource()) {
            jedis.set("user_id_" + userID, token);
            jedis.set("user_token_" + token, userID);
        }
    }


    // --- SINGLETON STUFF ---

    public static RedisStorage getInstance(){
        if (RedisStorage.INSTANCE == null){
            RedisStorage.INSTANCE = new RedisStorage();
        }
        return RedisStorage.INSTANCE;
    }


}
