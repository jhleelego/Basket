package com.example.basket.ui.scan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;

import com.example.basket.R;

public class ProInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_info);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
}