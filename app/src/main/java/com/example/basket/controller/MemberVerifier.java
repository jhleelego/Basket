package com.example.basket.controller;

import android.app.Activity;

import java.util.Map;


public interface MemberVerifier {
    public static final String TAG = null;
    void loginProgress(Map<String, Object> profileMap);
    void logoutProgress(Activity activity);
}
