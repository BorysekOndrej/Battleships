package no.ntnu.tdt4240.y2022.group23.battleshipsserver.server;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.ServerClientMessage;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.StringSerializer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class Lobby implements Serializable {
    protected String gameID;
    protected List<String> users = new ArrayList<>();
    protected boolean isPrivate = false;

    private static final JedisPool pool = RedisStorage.getInstance().pool;

    public Lobby(boolean isPrivate) {
        this.isPrivate = isPrivate;

        // find empty lobby id
        do {
            this.gameID = RandomStringUtils.randomAlphanumeric(32);
        }while(getLobby(gameID) != null);
        save();
    }

    public static Lobby getLobby(String gameID){
        try (Jedis jedis = pool.getResource()) {
            String lobbySerialized = jedis.get("lobby_" + gameID);
            if (lobbySerialized == null){
                return null;
            }

            return (Lobby) StringSerializer.fromString(lobbySerialized);
        }
    }

    public static String findGameID(String userID){
        try (Jedis jedis = pool.getResource()) {
            return jedis.get("user_active_in_lobby_"+userID);
        }
    }

    public void addUser(String userID){
        if (users.size() >= 2){
            return; // todo: raise exception?
        }
        if (users.contains(userID)){
            return; // todo: raise exception?
        }
        users.add(userID);
        save();
        FirebaseMessenger.sendMessage(
                userID,
                ServerClientMessage.JOINED_LOBBY_WITH_ID,
                Map.of("id", gameID)
        );

        if (users.size() == 2){
            this.startGame();
        }
    }

    private void save(){
        try (Jedis jedis = pool.getResource()) {
            jedis.set("lobby_"+gameID, StringSerializer.toString(this));

            for (String userID: users) {
                jedis.set("user_active_in_lobby_"+userID, gameID);
            }
        }
    }

    private void startGame(){
        for (String userID: users) {
            FirebaseMessenger.sendMessage(userID, ServerClientMessage.PLACEMENT_START, null);
        }
    }

    public void endCommunication(String userIDWhichCanceled){
        for (String userID: users) {
            if (userID.equals(userIDWhichCanceled)){
                continue;
            }
            FirebaseMessenger.sendMessage(userID, ServerClientMessage.OTHER_ENDED_COMMUNICATION,null);
        }
    }

}
