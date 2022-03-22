package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;

public class HttpsClient extends Application implements INetworkClient {
    private static final String TAG = "MyActivity";
    RequestQueue queue;

    public HttpsClient(Context ctx) {
        queue = Volley.newRequestQueue(ctx);
        queue.start();
    }


    @Override
    public Map<String, String> receive() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean send(String url, Map<String, String> data) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    Log.d(TAG, "Response is: " + response);
                },
                error -> {
                    Log.d(TAG, "That didn't work!");
                }
        );
        queue.add(stringRequest);

        return true;
    }
}
