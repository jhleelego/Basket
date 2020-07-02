package com.example.basket.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.basket.R;
import com.example.basket.factory.FragmentsFactory;
import com.example.basket.ui.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PlazaActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    String nickName = null;
    String mem_Entrance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plaza);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);


    }

    public void btn_logoutClick(View v) {
        Log.i(TAG, "v : " + v);
        FragmentsFactory.getInstance().logoutProgress();
        startActivity(new Intent(PlazaActivity.this, LoginActivity.class));
        Log.i(TAG, "btn 끝");
    }

   /* @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(nickName!=null){
            Log.i("nickname", nickName);
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("nickname", nickName);
            startActivity(intent);
        }
    }*/


}