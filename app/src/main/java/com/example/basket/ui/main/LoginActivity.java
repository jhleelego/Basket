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
import com.example.basket.vo.MemberDTO;


public class LoginActivity extends AppCompatActivity {
    public static final String TAG ="LoginActivity";
    EditText et_inputID = null;
    EditText et_inputPW = null;
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
        if(v.getId() == R.id.btn_bil){
            if(et_inputID.getText().toString().length()==0){
                Toast.makeText(this, "아이디를 입력하여 주세요.", Toast.LENGTH_LONG).show();
            } else if(et_inputPW.getText().toString().length()==0){
                Toast.makeText(this, "비밀번호를 입력하여 주세요.", Toast.LENGTH_LONG).show();
            } else if((et_inputID.getText().toString()!=null&&et_inputID.getText().toString().length()>0)
                &&(et_inputPW.getText().toString()!=null&&et_inputPW.getText().toString().length()>0)){
                fragmentTaking(v);
            }
        } else {
            fragmentTaking(v);
        }
    }

    public void plazaEnterActivity() {
        Log.i(TAG, "plazaEnterActivity()");
        startActivity(new Intent(LoginActivity.this, PlazaActivity.class));
    }

    public void btn_signUpClick(View view) {
        startActivity(new Intent(this, TermsActivity.class));
    }

    public void fragmentTaking(View v){
        FragmentsFactory.removeInstance();
        FragmentsFactory.newInstance(v);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add((Fragment)FragmentsFactory.getInstance(), FragmentsFactory.getInstance().TAG).commit();
    }
    public void logOutActive(){
        Log.i(TAG, "logOutActive()");
        MemberDTO.getInstance().removeInfo();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove((Fragment)FragmentsFactory.getInstance()).commit();
        FragmentsFactory.removeInstance();
    }
}
