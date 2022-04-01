package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

class FirebaseClient extends FirebaseMessagingService implements INetworkClient {
    private static final String TAG = "Firebase client";
    private Queue<Map<String, String>> notificationQueue = new PriorityQueue<>();
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
        notificationQueue.add(data);
    }

    @Override
    public Map<String, String> receive() {
        return notificationQueue.poll();
    }

    @Override
    public boolean send(String url, Map<String, String> data) {
        throw new UnsupportedOperationException();
    }
}