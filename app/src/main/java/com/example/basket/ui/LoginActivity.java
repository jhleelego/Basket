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
import com.example.basket.ui.main.SignUpActivity;
import com.example.basket.ui.main.TermsActivity;


public class LoginActivity extends AppCompatActivity {
    public static final String TAG ="LoginActivity";
    FragmentTransaction fragmentTransaction = null;
    boolean success = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
    }

    public void btn_signInClick(View v) {
        Log.i(TAG, v.toString());
        MemberVerifier memberVerifier = FragmentsFactory.getInstance(v);
        fragmentTransaction.add((Fragment)memberVerifier, memberVerifier.TAG);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void enterActivity() {
        FragmentsFactory.getInstance().loginProgress();
        Intent intent = new Intent(this, PlazaActivity.class);
        startActivity(intent);
    }

    public void btn_signUpClick(View view) {
        startActivity(new Intent(this, TermsActivity.class));
    }
}