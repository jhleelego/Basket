package com.example.basket.ui;

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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.basket.R;
import com.example.basket.factory.FragmentsFactory;
import com.example.basket.ui.search.EventPopupActivity;
import com.example.basket.vo.MemberDTO;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PlazaActivity extends AppCompatActivity {
    Dialog dialog = null;
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