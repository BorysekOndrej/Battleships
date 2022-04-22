package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network;

import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class FirebaseClient extends FirebaseMessagingService implements INetworkClient {
    private static final String TAG = "Firebase client";
    private String firebaseToken;
    private IFirebaseTokenUpdate firebaseUpdateCallback = null;

    public FirebaseClient() {}

    public void injectFirebaseUpdateCallback(IFirebaseTokenUpdate firebaseUpdateCallback){
        this.firebaseUpdateCallback = firebaseUpdateCallback;
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        firebaseToken = token;
        if (firebaseUpdateCallback != null){
            firebaseUpdateCallback.sendFirebaseToken(firebaseToken);
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        HashMap<String, String> data = new HashMap<>();

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            data.putAll(remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            data.put("notificationsMsg", remoteMessage.getNotification().getBody());
        }

        Intent intent = new Intent("firebaseNewNotification");
        intent.putExtra("notification", data);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

    }

    @Override
    public Map<String, String> receive() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean send(String url, Map<String, String> data) {
        throw new UnsupportedOperationException();
    }
}