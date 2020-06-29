package com.example.basket.controller;

import java.util.Map;

public interface MemberVerifier {
    public void loginProgress(Map<String, Object> profileMap);
    public void logoutProgress();
}
