package com.example.basket.ui.menu.favevent;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.example.basket.R;
import com.example.basket.ui.basket.ListViewAdapterCustomer;
import com.example.basket.util.VolleyCallback;
import com.example.basket.util.VolleyQueueProvider;
import com.example.basket.vo.MemberDTO;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavCouponActivity extends AppCompatActivity {

    public static final String TAG= "EventCouponActivity";
    private Activity mActivity = null;
    private Context mContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_event_coupon);
        mActivity = (Activity)this;
        mContext = (Context)this;
        Map<String, String> pMap = new HashMap<>();
        pMap.put("mem_code", Integer.toString(MemberDTO.getInstance().getMem_code()));
        Log.i(TAG, "mem_code : " + MemberDTO.getInstance().getMem_code());
        VolleyQueueProvider.callbackVolley(new VolleyCallback() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "response : " + response);
                List<Map<String, Object>> favCouponList = new Gson().fromJson(response, List.class);
                ArrayAdapter eventCouponAdapter = new ListViewAdapterCustomer(mActivity,mContext, R.layout.listview_eventcoupon_item, favCouponList, FavCouponActivity.TAG);
                ListView favEventCouponListview = (ListView)findViewById(R.id.lv_fav_event_coupon);
                favEventCouponListview.setAdapter(eventCouponAdapter);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "error : " + error.toString());
                error.printStackTrace();
            }
        },"store/fav_event", pMap);
    }
}