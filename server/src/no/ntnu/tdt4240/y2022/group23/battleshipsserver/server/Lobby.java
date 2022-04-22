package no.ntnu.tdt4240.y2022.group23.battleshipsserver.server;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.ServerClientMessage;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.StringSerializer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class Lobby implements Serializable {
    private String gameID;
    protected List<String> users = new ArrayList<>();
    protected boolean isPrivate;
    protected String inviteID = null;

    private static final JedisPool pool = RedisStorage.getInstance().pool;

    public Lobby(boolean isPrivate) {
        this.isPrivate = isPrivate;

        // find empty lobby id
        do {
            this.gameID = RandomStringUtils.randomAlphanumeric(32);
        }while(getLobbyByID(gameID) != null);


        if (isPrivate){
            // find empty invite id

            try (Jedis jedis = pool.getResource()) {
                do {
                    this.inviteID = RandomStringUtils.randomNumeric(6);
                }while(jedis.get("invite_" + this.inviteID) != null);

                jedis.setex("invite_" + this.inviteID, 60*60, this.gameID); // invite code has 60 minute timeout
            }
        }

        save();
    }

    private static Lobby getLobbyByID(String gameID){
        if (gameID == null){
            return null;
        }
        try (Jedis jedis = pool.getResource()) {
            String lobbySerialized = jedis.get("lobby_" + gameID);
            if (lobbySerialized == null){
                return null;
            }

            return StringSerializer.fromString(lobbySerialized);
        }
    }

    public static Lobby getLobbyByInvite(String inviteID){
        if (inviteID == null){
            return null;
        }
        try (Jedis jedis = pool.getResource()) {
            return getLobbyByID(jedis.get("invite_" + inviteID));
        }
    }

    public static String findGameID(String userID){
        try (Jedis jedis = pool.getResource()) {
            return jedis.get("user_active_in_lobby_"+userID);
        }
    }

    public static Lobby getUsersGame(String userID){
        return getLobbyByID(findGameID(userID));
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

        if (isPrivate) {
            // don't send this message if the game lobby is started via matchmaking
            HashMap<String, String> map = new HashMap<>();
            map.put("id", inviteID);

            FirebaseMessenger.sendMessage(
                    userID,
                    ServerClientMessage.JOINED_LOBBY_WITH_ID,
                    map
            );
        }

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

        deleteInviteCode();
    }

    private void deleteInviteCode(){
        if (inviteID == null){
            return;
        }

        try (Jedis jedis = pool.getResource()) {
            if (jedis.get("invite_" + this.inviteID) != null){
                jedis.del("invite_" + this.inviteID);
                this.inviteID = null;
                save();
            }
        }
    }

    public void endCommunication(String userIDWhichCanceled){
        deleteInviteCode();

        for (String userID: users) {
            if (userID.equals(userIDWhichCanceled)){
                continue;
            }
            FirebaseMessenger.sendMessage(userID, ServerClientMessage.OTHER_ENDED_COMMUNICATION,null);
        }
    }

    public String getOpponentID(String userID){
        for (String userID2: users) {
            if (!userID2.equals(userID)){
                return userID2;
            }
        }
        return null;
    }

}
