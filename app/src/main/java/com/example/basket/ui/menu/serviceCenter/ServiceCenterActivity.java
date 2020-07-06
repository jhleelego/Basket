package com.example.basket.ui.menu.serviceCenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.basket.R;

public class ServiceCenterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_center);


    }

    public void chatMove(View view) {
        Intent intent = new Intent(ServiceCenterActivity.this,ChattingActivity.class);
        startActivity(intent);
    }
}