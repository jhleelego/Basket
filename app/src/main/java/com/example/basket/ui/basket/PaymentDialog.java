package com.example.basket.ui.basket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.android.volley.VolleyError;
import com.example.basket.R;
import com.example.basket.util.VolleyCallback;
import com.example.basket.util.VolleyQueueProvider;
import com.example.basket.vo.MemberDTO;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import blockchain.ChainUtil;

public class PaymentDialog extends Activity {
    public static final String TAG = "PaymentDialog";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_payment_dialog);
    }

    public void btn_paymentDialog(View view) {
        switch (view.getId()){
            case R.id.btn_payment_cancel : {
                this.finish();

            } break;
            case R.id.btn_payment_payment : {
                Map<String, String> pMap = new HashMap<>();
                pMap.put("pro_code", getIntent().getExtras().getString("pro_code"));
                pMap.put("sign", ChainUtil.generateSignature(MemberDTO.getInstance().getMem_wallet().privateKey, getIntent().getExtras().getString("txId")));
                VolleyQueueProvider.callbackVolley(new VolleyCallback() {
                    @Override
                    public void onResponse(String response) {
                        String[] sa = response.split("#");
                        if ("success".equals(sa[0])) {
                            //성공
                            Log.i(TAG, sa[1]);
                        } else {
                            //실패
                            Log.i(TAG, sa[1]);
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.i(TAG, "error : " + error.toString());
                    }
                },"chain/send_funds" ,pMap);
            } break;
            default : break;
        }
    }
}