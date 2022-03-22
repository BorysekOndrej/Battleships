package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network;

import android.content.Context;

import java.util.Map;

public class NetworkClient implements INetworkClient {
    private final FirebaseClient firebase;
    private final HttpsClient httpsClient;
    private static NetworkClient INSTANCE;

    private NetworkClient(Context ctx){
        httpsClient = new HttpsClient(ctx);
        firebase = new FirebaseClient();
        firebase.injectHttpsClient(httpsClient);
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

}
