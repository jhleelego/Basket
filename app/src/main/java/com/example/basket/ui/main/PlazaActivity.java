package com.example.basket.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.example.basket.R;
import com.example.basket.factory.FragmentsFactory;
import com.example.basket.ui.basket.BasketFragment;
import com.example.basket.ui.basket.CouponChoice;
import com.example.basket.ui.menu.MenuFragment;
import com.example.basket.ui.scan.CustomScannerActivity;
import com.example.basket.ui.search.SearchFragment;
import com.example.basket.ui.search.productSearch.ProductSearchActivity;
import com.example.basket.util.VolleyCallback;
import com.example.basket.util.VolleyQueueProvider;
import com.example.basket.vo.MemberDTO;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

import static com.example.basket.ui.basket.BasketFragment.total_pro_price;

public class PlazaActivity extends AppCompatActivity {
    public static final String TAG = "PlazaActivity";
    MenuFragment menuFragment = MenuFragment.newInstance();
    SearchFragment searchFragment = SearchFragment.newInstance();
    BasketFragment basketFragment = BasketFragment.newInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plaza);
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, basketFragment).commit();
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
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, menuFragment).commit();
                    startActivity(new Intent(PlazaActivity.this, CustomScannerActivity.class));
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
        menuFragment = null;
        searchFragment = null;
        basketFragment = null;
        logOutActive();
    }

    public void logOutActive(){
        Log.i(TAG, "logOutActive()");
        FragmentsFactory.getInstance().logoutProgress(this);
        FragmentsFactory.removeInstance();
        MemberDTO.getInstance().removeInfo();
    }


    public void iv_goToProSearch(View view) {
        Intent intent = new Intent(PlazaActivity.this, ProductSearchActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, basketFragment).commit();
    }

    public void couponChoice(View view) {
        loadEventCouponItemsFromServerDB();
    }

    public boolean loadEventCouponItemsFromServerDB() {
        Map<String, String> pMap = new HashMap<>();
        pMap.put("sto_code", MemberDTO.getInstance().getSto_code());
        pMap.put("total_price", Integer.toString(total_pro_price));
        VolleyQueueProvider.callbackVolley(new VolleyCallback() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "response : " + response);
                if(response!=null){
                    Intent intent = new Intent(PlazaActivity.this, CouponChoice.class);
                    intent.putExtra("couponData", response);
                    startActivityForResult(intent, 1);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "error : " + error.toString());

            }
        },"payment/find_event", pMap);
        return true;
    }



}





