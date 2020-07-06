package com.example.basket.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.basket.R;
import com.example.basket.factory.FragmentsFactory;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.example.basket.ui.main.LoginActivity.fragmentTransaction;

public class PlazaActivity extends AppCompatActivity {

    public static final String TAG = "PlazaActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plaza);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public void btn_logoutClick(View v) {
        logOutActive();
    }

    public void LoginEnterActivity() {
        startActivity(new Intent(PlazaActivity.this, LoginActivity.class));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        logOutActive();
    }

    public void logOutActive(){
        fragmentTransaction.remove((Fragment)FragmentsFactory.getInstance());
        FragmentsFactory.getInstance().logoutProgress(this);
        FragmentsFactory.removeInstance();
    }




}