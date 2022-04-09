package no.ntnu.tdt4240.y2022.group23.battleshipsserver.server;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.ClientServerMessage;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.ServerClientMessage;

public class FirebaseMessenger {
    private static final Logger logger = LogManager.getLogger(FirebaseMessenger.class);
    private static FirebaseMessenger INSTANCE;

    private FirebaseMessenger(){
        try {
            FileInputStream serviceAccount = new FileInputStream("config/java_server/firebaseSecretAccountKey.json");
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp.initializeApp(options);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void sendMessage(String userID, ServerClientMessage msgType, Map<String, String> args){
        getInstance(); // this is used to make sure FirebaseMessenger is initialized

        RedisStorage redisStorage = RedisStorage.getInstance();

        String firebaseToken = redisStorage.getUserTokenByID(userID);
        if (firebaseToken == null){
            logger.warn("Tried to send firebase message to user "+userID+" which doesn't have registered firebase token. Skipping.");
            return;
        }

        // See documentation on defining a message payload.
        Message.Builder messageBuilder = Message.builder()
                .putData("type", msgType.name());

        if (args != null){
            messageBuilder = messageBuilder.putAllData(args);
        }
        Message message = messageBuilder
            .setToken(firebaseToken)
            .build();

        // Send a message to the device corresponding to the provided registration token.
        try {
            String response = FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            logger.warn(e.getStackTrace());
        }

    }

    // --- SINGLETON STUFF ---

    public static FirebaseMessenger getInstance(){
        if (FirebaseMessenger.INSTANCE == null){
            FirebaseMessenger.INSTANCE = new FirebaseMessenger();
        }
        return FirebaseMessenger.INSTANCE;
    }


}
