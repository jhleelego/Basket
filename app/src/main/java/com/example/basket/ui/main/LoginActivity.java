package com.example.basket.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.basket.R;
import com.example.basket.controller.MemberVerifier;
import com.example.basket.factory.FragmentsFactory;


public class LoginActivity extends AppCompatActivity {
    public static final String TAG ="LoginActivity";
    public static FragmentTransaction fragmentTransaction = null;
    public MemberVerifier memberVerifier = null;
    public EditText et_inputID = null;
    public EditText et_inputPW = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
    }

    public void btn_signInClick(View v) {
        Log.i(TAG, v.toString());
        FragmentsFactory.removeInstance();
        FragmentsFactory.newInstance(v, et_inputID, et_inputPW, this);
        fragmentTransaction.add((Fragment)FragmentsFactory.getInstance(), FragmentsFactory.getInstance().TAG);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void PlazaEnterActivity() {
        Log.i(TAG, "PlazaEnterActivity()");
        startActivity(new Intent(LoginActivity.this, PlazaActivity.class));

    }

    public void btn_signUpClick(View view) {
        startActivity(new Intent(this, TermsActivity.class));
    }
}