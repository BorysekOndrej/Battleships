package no.ntnu.tdt4240.y2022.group23.battleshipsserver.server;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.Serializable;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.ShipPlacements;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.StringSerializer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisStorage {
    private static RedisStorage INSTANCE;
    public JedisPool pool;

    private RedisStorage(){
        String redisLocation = System.getenv("REDIS_LOCATION");
        if (redisLocation == null){
            redisLocation = "localhost";
        }

        pool = new JedisPool(redisLocation, 6379);
    }

    // todo: set TTL

    void addUserToMatchmakingQueue(String userID, String queue){
        if (userID.isEmpty()){
            return;
        }
        try (Jedis jedis = pool.getResource()) {
            jedis.rpush(queue, userID);
        }
    }

    ImmutablePair<String, String> getTwoUsersFromMatchmaking(String queue){
        try (Jedis jedis = pool.getResource()) {
            if (jedis.llen(queue) < 2) {
                return null;
            }

            String user1 = jedis.lpop(queue);
            String user2 = jedis.lpop(queue);

            if (user1 == null || user2 == null) {
                if (user1 == null) { addUserToMatchmakingQueue(user2, queue); }
                if (user2 == null) { addUserToMatchmakingQueue(user1, queue); }
                return null;
            }

            return new ImmutablePair<>(user1, user2);
        }
    }


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

    void setNewUserToken(String userID, String token){
        String oldToken = getUserTokenByID(userID);
        if (token.equals(oldToken)){
            return;
        }

        try (Jedis jedis = pool.getResource()) {
            jedis.set("user_id_" + userID, token);
            jedis.set("user_token_" + token, userID);

            if (oldToken != null){
                jedis.del("user_token_" + oldToken);
            }
        }
    }

    private <T extends Serializable> T get(String key) {
        try (Jedis jedis = pool.getResource()) {
            if (!jedis.exists(key))
                return null;
            return StringSerializer.fromString(jedis.get(key));
        }
    }

    private <T extends Serializable> void set(String key, T val) {
        try (Jedis jedis = pool.getResource()) {
            jedis.set(key, StringSerializer.toString(val));
        }
    }

    GameBoard getUserGameBoard(String userID) {
        return get("game_board_" + userID);
    }

    void setUserGameBoard(String userID, GameBoard board) {
        set("game_board_" + userID, board);
    }

    ShipPlacements getUserShipPlacements(String userID) {
        return get("ship_placements_" + userID);
    }

    void setShipPlacements(String userID, ShipPlacements placements) {
        set("ship_placements_" + userID, placements);
    }

    String getOpponentId(String userID) {
        Lobby lobby = Lobby.getUsersGame(userID); // this creates circular dependency
        if (lobby == null){
            return null; // todo: throw exception?
        }
        return lobby.getOpponentID(userID);
    }

    String getELO(String userID) {
        try (Jedis jedis = pool.getResource()) {
            jedis.setnx("elo_"+userID, "1500");
            return jedis.get("elo_"+userID);
        }
    }

    void setELO(String userID, String elo_value) {
        try (Jedis jedis = pool.getResource()) {
            jedis.set("elo_"+userID, elo_value);
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
