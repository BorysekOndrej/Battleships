package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;
import java.util.concurrent.TimeUnit;

class HttpsClient extends Application implements INetworkClient {
    private static final String TAG = "HttpsClient";
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

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(TAG, "HTTPS (DEBUG) response is: " + response);
                },
                error -> {
                    Log.w(TAG, "HTTPS request failed " + error);
                }
        ){
            @Override
            protected Map<String, String> getParams(){
                return data;
            }
        };

        if ("/token".equals(url)){
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    5,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            );
        }else{
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    (int) TimeUnit.SECONDS.toMillis(10),
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            );
        }

        queue.add(stringRequest);

        return true;
    }
}
