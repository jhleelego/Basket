package com.example.basket.ui.basket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.basket.R;
import com.example.basket.ui.scan.RepayActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CouponChoice extends Activity {
    public static final String TAG = "CouponChoice";
    public static final String TAGPay = "CouponChoicePay";
    public static final String TAGRepay = "CouponChoiceRepay";
    public static final int COUPON_PICK = 1004;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_coupon_choice);
        List<Map<String, Object>> couponList = (ArrayList<Map<String, Object>>) new Gson().fromJson(getIntent().getStringExtra("couponData"), List.class);
        Log.i(TAG, "couponList.size() : " + couponList.size());
        ArrayAdapter eventCouponAdapter = null;
        if(getIntent().getStringExtra("type").equals(BasketFragment.TAG)){
            eventCouponAdapter = new ListViewAdapterCustomer(this,this, R.layout.listview_eventcoupon_item, couponList, CouponChoice.TAGPay);
        } else if(getIntent().getStringExtra("type").equals(RepayActivity.TAG)){
            eventCouponAdapter = new ListViewAdapterCustomer(this,this, R.layout.listview_eventcoupon_item, couponList, CouponChoice.TAGRepay);
        }

        ListView eventCouponListview = (ListView)findViewById(R.id.lv_event_coupon);
        eventCouponListview.setAdapter(eventCouponAdapter);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if(resultCode == COUPON_PICK){
        }*/
    }
}