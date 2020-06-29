package com.example.basket.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.basket.R;
import com.example.basket.loginFragment.BilFragment;
import com.example.basket.loginFragment.KilFragment;
import com.example.basket.loginFragment.NilFragment;
import com.example.basket.ui.menu.MenuFragment;
import com.example.basket.ui.scan.ScanFragment;
import com.example.basket.vo.MemberDTO;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PlazaActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    String nickName = null;
    private FragmentTransaction fragmentTransaction;
    private Button btn_logout = null;
    String mem_Entrance = null;

    NilFragment oAuthFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        mem_Entrance = MemberDTO.getInstance().getMem_Entrance();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(mem_Entrance!=null&&mem_Entrance.length()>0){
            if(mem_Entrance.equals(BilFragment.TAG)){
                fragmentTransaction.remove(NilFragment.getInstance());
                fragmentTransaction.remove(KilFragment.getInstance());
            } else if(mem_Entrance.equals(NilFragment.TAG)){
                fragmentTransaction.remove(BilFragment.getInstance());
                fragmentTransaction.remove(KilFragment.getInstance());
            } else if(mem_Entrance.equals(KilFragment.TAG)){
                fragmentTransaction.remove(BilFragment.getInstance());
                fragmentTransaction.remove(NilFragment.getInstance());
            }
            fragmentTransaction.commitAllowingStateLoss();
        }

        MenuFragment menuFragment = new MenuFragment(); // Fragment 생성
        ScanFragment scanFragment = new ScanFragment(); // Fragment 생성
        Bundle bundle = new Bundle(1); // 파라미터는 전달할 데이터 개수
        bundle.putString("nickname", nickName); // key , value
        scanFragment.setArguments(bundle);
    }

    public void btn_logoutClick(View view) {
        if(mem_Entrance.equals(BilFragment.TAG)){
            BilFragment.getInstance().logoutProgress();
        } else if(mem_Entrance.equals(NilFragment.TAG)){
            NilFragment.getInstance();
        } else if(mem_Entrance.equals(KilFragment.TAG)){
            KilFragment.getInstance();
        }





/*
        if(loginType.equals("nil")){
        } else if(loginType.equals("basketidlogin")){
        }
*/


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