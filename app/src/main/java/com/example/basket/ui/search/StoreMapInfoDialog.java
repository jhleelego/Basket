package com.example.basket.ui.search;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.basket.R;
import com.example.basket.ui.basket.ListViewAdapterCustomer;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StoreMapInfoDialog extends Activity {
    public static final String TAG ="StoreMapInfoDialog";

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_store_map_info_dialog);
        mContext = (Context)this;
        Log.i(TAG, "■■■■■■■■■■■■■■■■■■■■■■ " + getIntent().getStringExtra("storeData"));
        List<Map<String, Object>> storeInfoList = (ArrayList<Map<String, Object>>) new Gson().fromJson(getIntent().getStringExtra("storeData"), List.class);
        Log.i(TAG, "storeInfoList.size() : " + storeInfoList.size());
        ArrayAdapter storeMapInfoAdapter = new ListViewAdapterCustomer(this,this, R.layout.listview_sto_item, storeInfoList, StoreMapInfoDialog.TAG);
        ListView storeMapInfoListView = (ListView)findViewById(R.id.lv_store);
        storeMapInfoListView.setAdapter(storeMapInfoAdapter);

    }
}