package com.example.basket.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.basket.R;
import com.example.basket.factory.FragmentsFactory;
import com.example.basket.ui.basket.BasketFragment;
import com.example.basket.ui.menu.MenuFragment;
import com.example.basket.ui.scan.CustomScannerActivity;
import com.example.basket.ui.search.SearchFragment;
import com.example.basket.ui.search.productSearch.ProductSearchActivity;
import com.example.basket.vo.MemberDTO;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PlazaActivity extends AppCompatActivity {
    public static final String TAG = "PlazaActivity";
    private Activity mActivity = null;
    private SearchFragment searchFragment = new SearchFragment();
    private MenuFragment menuFragment = new MenuFragment();
    private  BasketFragment basketFragment = new BasketFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plaza);
        mActivity = (Activity)this;
        if(MemberDTO.getInstance().getMem_entrance()==null){
            MemberDTO.getInstance().removeInfo();
            logOutActive();
            loginEnterActivity();
        } else {
            BottomNavigationView navView = findViewById(R.id.nav_view);
            navView.setOnNavigationItemSelectedListener(menuItem -> {
                if(menuItem.getItemId() == R.id.navigation_search){
                    Log.i(TAG, "onNavigationItemSelected()");
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, searchFragment).commit();
                    return true;
                }
                if(menuItem.getItemId() == R.id.navigation_basket){
                    Log.i(TAG, "onNavigationItemSelected()");
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, basketFragment).commit();
                    return true;

                }
                if(menuItem.getItemId() == R.id.navigation_scan){
                    Log.i(TAG, "onNavigationItemSelected()");
                    startActivity(new Intent(PlazaActivity.this, CustomScannerActivity.class));
                    return false;
                }
                if(menuItem.getItemId() == R.id.navigation_menu){
                    Log.i(TAG, "onNavigationItemSelected()");
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, menuFragment).commit();
                    return true;
                }
                return false;
            });
            /*NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupWithNavController(navView, navController);*/
        }
    }

    public void btn_logoutClick(View v) {
        logOutActive();
    }

    public void loginEnterActivity() {
        MemberDTO.getInstance().removeInfo();
        startActivity(new Intent(PlazaActivity.this, LoginActivity.class));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        logOutActive();
    }

    public void logOutActive(){
        Log.i(TAG, "logOutActive()");
        //fragmentTransaction.remove((Fragment)FragmentsFactory.getInstance());
        FragmentsFactory.getInstance().logoutProgress(this);
        FragmentsFactory.removeInstance();
        MemberDTO.getInstance().removeInfo();
    }


    public void iv_goToProSearch(View view) {
        Intent intent = new Intent(PlazaActivity.this, ProductSearchActivity.class);
        startActivity(intent);
    }
}