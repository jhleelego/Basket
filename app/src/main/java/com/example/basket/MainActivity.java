package com.example.basket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.basket.nil.OAuthFragment;
import com.example.basket.ui.menu.MenuFragment;
import com.example.basket.ui.scan.ScanFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    String nickName = null;
    TextView tv_userNick = null;

    private FragmentManager fragmentManager;
    private OAuthFragment oAuthFragment;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        BottomNavigationView bnv_nil = findViewById(R.id.bnv_mainActivity_nil);
        fragmentManager = getSupportFragmentManager();
        oAuthFragment = new OAuthFragment();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.bnv_mainActivity_nil, oAuthFragment).commitAllowingStateLoss();



        Intent intent = getIntent();
        if(intent.getStringExtra("nickname")!=null) {
            nickName = intent.getStringExtra("nickname");
        }
        Log.i(TAG, nickName);

        MenuFragment menuFragment = new MenuFragment(); // Fragment 생성
        ScanFragment scanFragment = new ScanFragment(); // Fragment 생성
        Bundle bundle = new Bundle(1); // 파라미터는 전달할 데이터 개수
        bundle.putString("nickname", nickName); // key , value
        scanFragment.setArguments(bundle);

    }

   /* @Override
    public void onBackPressed() {

        if(nickName!=null){
            Log.i("nickname", nickName);
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("nickname", nickName);
            startActivity(intent);
        }

        super.onBackPressed();
    }*/


}