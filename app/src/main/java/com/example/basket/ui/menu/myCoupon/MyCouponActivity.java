package com.example.basket.ui.menu.myCoupon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.basket.R;
import com.example.basket.ui.menu.event.EventActivity;

public class MyCouponActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_coupon);

    }

    public void eventMove(View view) {
        Intent intent = new Intent(MyCouponActivity.this,EventActivity.class);
        startActivity(intent);
    }
}