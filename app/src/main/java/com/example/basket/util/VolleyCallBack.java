package com.example.basket.util;

import com.android.volley.VolleyError;

public interface VolleyCallBack {
    void onResponse(String response);
    void onErrorResponse(VolleyError error);
}