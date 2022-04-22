package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

interface IFirebaseTokenUpdate {
    void sendFirebaseToken(String newToken);
}

public class NetworkClient implements INetworkClient, IFirebaseTokenUpdate {
    private final FirebaseClient firebase;
    private final HttpsClient httpsClient;
    private static NetworkClient INSTANCE;
    private final String baseURL = System.getenv("BASE_URL") != null ?
            System.getenv("BASE_URL") :
            "https://battleships.borysek.eu";
    private String userID;
    Queue<Map<String, String>> notificationQueue = new LinkedList<>();

    private NetworkClient(Context ctx){
        httpsClient = new HttpsClient(ctx);
        firebase = new FirebaseClient();
        firebase.injectFirebaseUpdateCallback(this);

        LocalBroadcastManager.getInstance(ctx).registerReceiver(firebaseNotificationListener,
                new IntentFilter("firebaseNewNotification"));


        // todo: This is one giant race condition waiting to happen
        FirebaseInstallations.getInstance().getId()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful()) {
                            userID = task.getResult();
                            Log.d("Installations", "Installation ID: " + userID);

                            FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
                                sendFirebaseToken(token);
                            });
                        } else {
                            Log.e("Installations", "Unable to get Installation ID");
                            // todo: solve this
                        }
                    }
                });

    }

    @Override
    public Map<String, String> receive() {
        return notificationQueue.poll();
    }

    @Override
    public boolean send(String url, Map<String, String> data) {
        if (url.startsWith("/")){
            url = baseURL + url;
        }

        HashMap<String, String> dataShallowCopy = new HashMap<>();
        dataShallowCopy.put("userID", userID);
        if (data != null){
            dataShallowCopy.putAll(data);
        }
        return httpsClient.send(url, dataShallowCopy);
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

    public void sendFirebaseToken(String newToken){
        Map<String, String> tokenMsg = new HashMap<>();
        tokenMsg.put("token", newToken);
        send("/token", tokenMsg);
    }

    private BroadcastReceiver firebaseNotificationListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            notificationQueue.add((Map<String, String>) extras.get("notification"));
        }
    };

}
