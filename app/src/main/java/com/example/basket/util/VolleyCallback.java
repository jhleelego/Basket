package com.example.basket.util;

import com.android.volley.VolleyError;

public interface VolleyCallback {
    void onResponse(String response);
    void onErrorResponse(VolleyError error);
}
