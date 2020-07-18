package com.example.basket.ui.menu.serviceCenter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.basket.R;

import static android.content.Intent.ACTION_DIAL;

public class ServiceCenterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_center);
    }
    public void call(View view) {
        Intent tt = new Intent(ACTION_DIAL, Uri.parse("tel:00000000"));
        startActivity(tt);
    }
}