package com.example.basket.ui.menu.paymenthistory;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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

public class PaymentHistoryActivity extends AppCompatActivity {
    public static final String TAG ="PaymentHistoryActivity";
    private Activity mActivity = null;
    private Context mContext = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);
        mActivity = (Activity)this;
        mContext = (Context)this;
        Map<String, String> favRequestMap = new HashMap<>();
        favRequestMap.put("mem_code", Integer.toString(MemberDTO.getInstance().getMem_code()));
        VolleyQueueProvider.callbackVolley(new VolleyCallback() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "response : " + response);
                if(response.length()==0){
                    Toast.makeText(PaymentHistoryActivity.this, "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Map<String, Object>> paymentHistoryItemList = new Gson().fromJson(response, List.class);
                Log.i(TAG, "paymentHistoryItemList.size() : " + paymentHistoryItemList.size());
                ArrayAdapter paymentHistroyAdapter = new ListViewAdapterCustomer(mActivity, mContext, R.layout.listview_payment_item, paymentHistoryItemList, PaymentHistoryActivity.TAG);
                ListView paymentHistoryAdapter = findViewById(R.id.lv_paymentHistory);
                paymentHistoryAdapter.setAdapter(paymentHistroyAdapter);
            }
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.i(TAG, "error : " + error.toString());
            }
        },"payment/show_pay" ,favRequestMap);
    }
}