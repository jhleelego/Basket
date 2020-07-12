package com.example.basket.ui.basket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;

import com.example.basket.R;
import com.example.basket.vo.EventCouponOneDTO;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CouponChoice extends Activity {

    public static final String TAG = "CouponChoice";
    public static final int COUPON_PICK = 1004;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_coupon_choice);
        List<Map<String, Object>> resultList = (ArrayList<Map<String, Object>>) new Gson().fromJson(getIntent().getStringExtra("couponData"), List.class);
        ArrayList<EventCouponOneDTO> list = new ArrayList<>();
        for(int i=0; i<resultList.size(); i++) {
            EventCouponOneDTO eventCouponOneDTO = new EventCouponOneDTO();
            Map<String, Object> pMap = resultList.get(i);
            eventCouponOneDTO.setEve_code(pMap.get("EVE_CODE").toString());
            eventCouponOneDTO.setDiscount_for(Integer.valueOf((int) Math.round((double) pMap.get("DISCOUNT_FOR"))));
            eventCouponOneDTO.setDis_price(Integer.valueOf((int) Math.round((double) pMap.get("DISCOUNT_PRICE"))));
            eventCouponOneDTO.setSto_name(pMap.get("STO_NAME").toString());
            list.add(eventCouponOneDTO);
        }
        Log.i(TAG, "eventCouponItems.size() : " + list.size());
        ListViewEventCouponAdapter eventCouponAdapter = new ListViewEventCouponAdapter(this,this, R.layout.listview_eventcoupon_item, list);
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