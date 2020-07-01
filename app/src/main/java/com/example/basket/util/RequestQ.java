package com.example.basket.util;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/* Volley는 Android 앱의 네트워킹을 더 쉽고, 무엇보다도 더 빠르게 하는 HTTP 라이브러리
*
* */
public class RequestQ {
    private static RequestQ instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    private RequestQ(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();

    }

    public static synchronized RequestQ getInstance(Context context) {
        if (instance == null) {
            instance = new RequestQ(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }


}
