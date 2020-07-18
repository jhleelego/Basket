package com.example.basket.ui.menu.favstore;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.basket.R;
import com.example.basket.util.VolleyCallback;
import com.example.basket.util.VolleyQueueProvider;

import java.util.HashMap;
import java.util.Map;

public class FavStoreDelActivity extends Activity {
    public static final String TAG = "FavStoreDelActivity";
    Button btn_fav_store_del = null;
    Button btn_fav_store_del_cancel = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_fav_store_del);
        Map<String, String> favRequestMap = new HashMap<>();
        favRequestMap.put("sto_code", getIntent().getStringExtra("sto_code"));
        favRequestMap.put("mem_code", getIntent().getStringExtra("mem_code"));
        btn_fav_store_del = findViewById(R.id.btn_fav_store_del);
        btn_fav_store_del_cancel = findViewById(R.id.btn_fav_store_del_cancel);
        btn_fav_store_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VolleyQueueProvider.callbackVolley(new VolleyCallback() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "response : " + response);
                        if(response.length()==0){
                            Toast.makeText(FavStoreDelActivity.this, "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            Toast.makeText(FavStoreDelActivity.this, "관심매장에서 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.i(TAG, "error : " + error.toString());
                    }
                },"store/del_favsto" ,favRequestMap);
            }
        });
        btn_fav_store_del_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}