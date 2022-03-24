package no.ntnu.tdt4240.y2022.group23.battleshipsserver.server;

import redis.clients.jedis.JedisPool;

public class RedisStorage {
    private static RedisStorage INSTANCE;
    public JedisPool pool;

    private RedisStorage(){
         pool = new JedisPool("localhost", 6379);
    }


    // --- SINGLETON STUFF ---

    public static RedisStorage getInstance(){
        if (RedisStorage.INSTANCE == null){
            RedisStorage.INSTANCE = new RedisStorage();
        }
        return RedisStorage.INSTANCE;
    }


}
