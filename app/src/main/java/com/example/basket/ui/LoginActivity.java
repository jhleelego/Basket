package com.example.basket.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.basket.R;
import com.example.basket.controller.MemberVerifier;
import com.example.basket.factory.FragmentsFactory;
import com.example.basket.ui.main.PlazaActivity;
import com.example.basket.ui.main.TermsActivity;

import java.lang.reflect.Member;


public class LoginActivity extends AppCompatActivity {
    public static final String TAG ="LoginActivity";
    public static FragmentTransaction fragmentTransaction = null;
    public static MemberVerifier memberVerifier = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
    }

    public void btn_signInClick(View v) {
        Log.i(TAG, v.toString());
        MemberVerifier memberVerifier = FragmentsFactory.newInstance(v);
        fragmentTransaction.add((Fragment)memberVerifier, memberVerifier.TAG);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void PlazaEnterActivity() {
        Log.i(TAG, "PlazaEnterActivity()");
        memberVerifier.loginProgress();
        startActivity(new Intent(LoginActivity.this, PlazaActivity.class));

    }

    public void btn_signUpClick(View view) {
        startActivity(new Intent(this, TermsActivity.class));
    }
}