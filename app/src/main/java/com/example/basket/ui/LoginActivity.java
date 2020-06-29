package com.example.basket.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.basket.R;
import com.example.basket.factory.FragmentsFactory;


public class LoginActivity extends AppCompatActivity {
    public static final String TAG ="LoginActivity";
    FragmentTransaction fragmentTransaction = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
    }

    public void btn_loginClick(View v) {
        Log.i(TAG, v.toString());
        FragmentsFactory.getInstance(fragmentTransaction, v);
    }
}