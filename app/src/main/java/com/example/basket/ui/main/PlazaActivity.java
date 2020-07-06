package com.example.basket.ui.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.basket.R;
import com.example.basket.factory.FragmentsFactory;
import com.example.basket.ui.LoginActivity;
import com.example.basket.ui.search.EventPopupActivity;
import com.example.basket.vo.MemberDTO;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.example.basket.ui.LoginActivity.fragmentTransaction;
import static com.example.basket.ui.LoginActivity.memberVerifier;

public class PlazaActivity extends AppCompatActivity {

    public static final String TAG = "PlazaActivity";
    Dialog dialog = null;
    String nickName = null;
    String mem_Entrance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plaza);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

 /*       AlertDialog.Builder ad = new AlertDialog.Builder(PlazaActivity.this);
        ad.setIcon(R.mipmap.ic_launcher);
        ad.setTitle("화가난다");

        ad.setMessage("asdsadsad");

        ad.setPositiveButton("화긴", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(PlazaActivity.this,"asdsad",Toast.LENGTH_LONG).show();
            }
        });
        ad.setNegativeButton("치소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(PlazaActivity.this,"asdsad",Toast.LENGTH_LONG).show();
            }
        });
        ad.show();
    }*/
        if(MemberDTO.getInstance().getMem_code()!=null) {
            Log.i(TAG, "MemberDTO.getInstance().getMem_code() : " + MemberDTO.getInstance().getMem_code());
            Intent intent = new Intent(PlazaActivity.this, EventPopupActivity.class);
            startActivity(intent);
        }
        if(fragmentTransaction!=null){
            Log.i(TAG, fragmentTransaction.toString());
        }
    }

    public void btn_logoutClick(View v) {
        fragmentTransaction.remove((Fragment)memberVerifier);
        memberVerifier.logoutProgress(this);
        FragmentsFactory.removeInstance();
    }

    public void LoginEnterActivity() {
        startActivity(new Intent(PlazaActivity.this, LoginActivity.class));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        fragmentTransaction.remove((Fragment)memberVerifier);
        memberVerifier.logoutProgress(this);
        FragmentsFactory.removeInstance();
    }
}