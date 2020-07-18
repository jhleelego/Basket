package com.example.basket.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.basket.R;
import com.example.basket.ui.basket.BasketFragment;
import com.example.basket.ui.menu.MenuFragment;
import com.example.basket.ui.scan.CustomScannerActivity;
import com.example.basket.ui.search.SearchFragment;
import com.example.basket.ui.search.productSearch.ProductSearchActivity;
import com.example.basket.vo.MemberDTO;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PlazaActivity extends AppCompatActivity {
    public static final String TAG = "PlazaActivity";
    Fragment menuFragment = null;
    Fragment searchFragment = null;
    Fragment basketFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plaza);
        Fragment menuFragment = MenuFragment.newInstance();
        Fragment searchFragment = SearchFragment.newInstance();
        Fragment basketFragment = BasketFragment.newInstance();
        if(MemberDTO.getInstance().getMem_entrance()==null){
            MemberDTO.getInstance().removeInfo();
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
                    if(basketFragment!=null){
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, basketFragment).commit();
                    }
                    return true;
                }
                if(menuItem.getItemId() == R.id.navigation_menu){
                    Log.i(TAG, "onNavigationItemSelected()");
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, menuFragment).commit();
                    return true;
                }
                return false;
            });
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, basketFragment).commit();
    }

    public void btn_logoutClick(View v) {
        MemberDTO.getInstance().removeInfo();
        finish();
    }

    public void loginEnterActivity() {
        MemberDTO.getInstance().removeInfo();
        startActivity(new Intent(PlazaActivity.this, LoginActivity.class));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        menuFragment = null;
        searchFragment = null;
        basketFragment = null;
        MemberDTO.getInstance().removeInfo();
        finish();
    }

    public void iv_goToProSearch(View view) {
        Intent intent = new Intent(PlazaActivity.this, ProductSearchActivity.class);
        startActivity(intent);
    }

    public void btn_btn_money_charge(View view) {
        Intent intent = new Intent(PlazaActivity.this, BasketMoneyChargeDialog.class);
        startActivityForResult(intent, 1);
    }
}
