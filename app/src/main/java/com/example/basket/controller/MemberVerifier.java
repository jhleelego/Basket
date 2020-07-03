package com.example.basket.controller;

import android.app.Activity;


public interface MemberVerifier {
    public static final String TAG = null;
    void loginProgress();
    void logoutProgress(Activity activity);
}
