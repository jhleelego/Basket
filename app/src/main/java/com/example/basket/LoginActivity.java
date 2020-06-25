package com.example.basket;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.basket.nil.OAuthFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LoginActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private OAuthFragment oAuthFragment;
    private FragmentTransaction transaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

     /*   oAuthFragment = new OAuthFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().commitAllowingStateLoss();*/

        BottomNavigationView bnv_nil = findViewById(R.id.bnv_loginActivity_nil);
        fragmentManager = getSupportFragmentManager();
        oAuthFragment = new OAuthFragment();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.bnv_loginActivity_nil, oAuthFragment).commitAllowingStateLoss();
    }
}