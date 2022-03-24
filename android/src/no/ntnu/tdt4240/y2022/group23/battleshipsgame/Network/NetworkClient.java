package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network;

import android.content.Context;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

interface IFirebaseTokenUpdate {
    void sendFirebaseToken(String originalToken, String newToken);
}

public class NetworkClient implements INetworkClient, IFirebaseTokenUpdate {
    private final FirebaseClient firebase;
    private final HttpsClient httpsClient;
    private static NetworkClient INSTANCE;
    private final String firebaseTokenSubmissionURL = "https://envojlo4sdzr8.x.pipedream.net/token";


    private NetworkClient(Context ctx){
        httpsClient = new HttpsClient(ctx);
        firebase = new FirebaseClient();
        firebase.injectFirebaseUpdateCallback(this);
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            sendFirebaseToken(token, token);
        });
    }

    @Override
    public Map<String, String> receive() {
        return firebase.receive();
    }

    @Override
    public boolean send(String url, Map<String, String> data) {
        return httpsClient.send(url, data);
    }

    // --- SINGLETON STUFF ---

    public static NetworkClient getInstance(Context context){
        // todo: this will fail, if we first call get instance without the context
        if (NetworkClient.INSTANCE == null){
            NetworkClient.INSTANCE = new NetworkClient(context);
        }
        return NetworkClient.INSTANCE;
    }

    public static NetworkClient getInstance(){
        return getInstance(null);
    }

    // Firebase token submission

    public void sendFirebaseToken(String oldToken, String newToken){
        Map<String, String> tokenMsg = new HashMap<>();
        tokenMsg.put("oldToken", oldToken);
        tokenMsg.put("newToken", newToken);
        send(firebaseTokenSubmissionURL, tokenMsg);
    }

}
