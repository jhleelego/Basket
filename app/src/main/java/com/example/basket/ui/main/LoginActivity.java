package com.example.basket.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.basket.R;
import com.example.basket.factory.FragmentsFactory;
import com.example.basket.grobal.BackKeyHandler;


public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LoginActivity";
    EditText et_inputID = null;
    EditText et_inputPW = null;
    BackKeyHandler backKeyHandler = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_inputID = findViewById(R.id.et_inputID);
        et_inputPW = findViewById(R.id.et_inputPW);
    }

    public void btn_signInClick(View v) {
        Log.i(TAG, v.toString());
        if (v.getId() == R.id.btn_bil) {
            if (et_inputID.getText().toString().length() == 0) {
                Toast.makeText(this, "아이디를 입력하여 주세요.", Toast.LENGTH_SHORT).show();
            } else if (et_inputPW.getText().toString().length() == 0) {
                Toast.makeText(this, "비밀번호를 입력하여 주세요.", Toast.LENGTH_SHORT).show();
            } else if ((et_inputID.getText().toString() != null && et_inputID.getText().toString().length() > 0)
                    && (et_inputPW.getText().toString() != null && et_inputPW.getText().toString().length() > 0)) {
                fragmentConnecting(v);
            }
        } else {
            fragmentConnecting(v);
        }
    }

    public void plazaEnterActivity() {
        Log.i(TAG, "plazaEnterActivity()");
        startActivity(new Intent(LoginActivity.this, PlazaActivity.class));
        logOutActive();
    }

    public void btn_signUpClick(View view) {
        startActivity(new Intent(this, TermsActivity.class));
    }

    public void fragmentConnecting(View v) {
        FragmentsFactory.removeInstance();
        FragmentsFactory.newInstance(v);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add((Fragment) FragmentsFactory.getInstance(), FragmentsFactory.getInstance().TAG).commit();
    }

    public void logOutActive() {
        Log.i(TAG, "logOutActive()");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove((Fragment) FragmentsFactory.getInstance()).commit();
        FragmentsFactory.removeInstance();
    }

    /* 다음 뒤로가기 키 입력시간에 따라서 종료할지, 유지할지 결정*/
    /* 2000 = 2초 입니다.*/

    @Override
    public void onBackPressed(){
        backKeyHandler = new BackKeyHandler(this);
        /*backKeyHandler.onBackPressed();*/
    }
}