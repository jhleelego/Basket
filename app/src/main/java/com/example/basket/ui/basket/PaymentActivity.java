package com.example.basket.ui.basket;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.example.basket.R;
import com.example.basket.util.VolleyCallback;
import com.example.basket.util.VolleyQueueProvider;
import com.example.basket.vo.MemberDTO;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import blockchain.ChainUtil;

import static com.example.basket.ui.basket.BasketFragment.basketMoney;
import static com.example.basket.ui.basket.BasketFragment.discountMoney;
import static com.example.basket.ui.basket.BasketFragment.total_pay;
import static com.example.basket.ui.basket.BasketFragment.total_pro_price;
import static com.example.basket.ui.scan.RepayActivity.rpa_discountMoney;
import static com.example.basket.ui.scan.RepayActivity.rpa_total_pay;
import static com.example.basket.ui.scan.RepayActivity.rpa_total_pro__price;
import static com.example.basket.util.SqliteTable.basket;

public class PaymentActivity extends AppCompatActivity {
    public static final String TAG = "PaymentActivity";

    public ScrollView sv_pay = null;
    public ListView lv_pa_listView = null;
    public TextView tv_pa_basketmoney = null;
    public TextView tv_pa_total_pro__price = null;
    public TextView  tv_pa_discountMoney = null;
    public TextView tv_pa_total_pay = null;

    public Button btn_payment_payment = null;
    public Button btn_payment_cancel = null;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_activity);

        sv_pay = findViewById(R.id.sv_pay);
        lv_pa_listView = findViewById(R.id.lv_pa_listView);
        tv_pa_basketmoney = findViewById(R.id.tv_pa_basketmoney);
        tv_pa_total_pro__price = findViewById(R.id.tv_pa_total_pro__price);
        tv_pa_discountMoney = findViewById(R.id.tv_pa_discountMoney);
        tv_pa_total_pay = findViewById(R.id.tv_pa_total_pay);
        btn_payment_payment = findViewById(R.id.btn_payment_payment);
        btn_payment_cancel = findViewById(R.id.btn_payment_cancel);

        List<Map<String, Object>> paList = new ArrayList<>();
        if(getIntent().getStringExtra("repayData")!=null) {
            //리스트로 온다 여기부터 작업
            paList = new Gson().fromJson(getIntent().getStringExtra("repayData"), List.class);
            int i=1;
            for(Map<String, Object> responseMap : paList) {
                responseMap.put("PRO_IMG", responseMap.get("pro_img").toString());
                responseMap.put("PRO_NAME", responseMap.get("pro_name").toString());
                responseMap.put("PRO_PRICE", (int) (Math.round((double) responseMap.get("pro_price"))));
                responseMap.put("PRO_CODE", (int) (Math.round((double) responseMap.get("pro_code"))));
                responseMap.put("DESIRED_STOCK_COUNT", (int) (Math.round((double) responseMap.get("pay_ea"))));
            }
            tv_pa_basketmoney.setText(new DecimalFormat("###,###").format(basketMoney) + "원");
            tv_pa_total_pro__price.setText(new DecimalFormat("###,###").format(rpa_total_pro__price) + "원");
            tv_pa_discountMoney.setText(" - " + new DecimalFormat("###,###").format(rpa_discountMoney) + "원");
            tv_pa_total_pay.setText(" = " + new DecimalFormat("###,###").format(rpa_total_pay) + "원");
        } else {
            loadPaItemsFromDB(paList);
            tv_pa_basketmoney.setText(new DecimalFormat("###,###").format(basketMoney) + "원");
            tv_pa_total_pro__price.setText(new DecimalFormat("###,###").format(total_pro_price) + "원");
            tv_pa_discountMoney.setText(" - " + new DecimalFormat("###,###").format(discountMoney) + "원");
            tv_pa_total_pay.setText(" = " + new DecimalFormat("###,###").format(total_pay) + "원");
        }

        ArrayAdapter paListViewAdapter = new ListViewAdapterCustomer(PaymentActivity.this, PaymentActivity.this, R.layout.listview_payment_info_item, paList, PaymentActivity.TAG);
        lv_pa_listView.setAdapter(paListViewAdapter);
        lv_pa_listView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                sv_pay.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });




    }

    public void btn_paymentDialog(View view) {
        switch (view.getId()){
            case R.id.btn_payment_cancel : {
                this.finish();

            } break;
            case R.id.btn_payment_payment : {
                Map<String, String> pMap = new HashMap<>();
                pMap.put("pay_code", getIntent().getExtras().getString("pay_code"));
                pMap.put("sign", ChainUtil.generateSignature(MemberDTO.getInstance().getMem_wallet().privateKey, getIntent().getExtras().getString("txId")));
                VolleyQueueProvider.callbackVolley(new VolleyCallback() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "response : " + response);
                        if(response.length()==0){
                            Toast.makeText(PaymentActivity.this, "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                        Intent intent = new Intent(PaymentActivity.this, PaymentResultActivity.class);
                        intent.putExtra("response", response);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        finish();
                        Log.e(TAG, "error : " + error.toString());
                    }
                },"chain/send_funds" ,pMap);
            } break;
            default : break;
        }
    }

    public boolean loadPaItemsFromDB(List<Map<String, Object>> list) {
        Log.i(TAG, "loadBasketItemsFromDB()");
        Map<String, Object> basketMap;
        if (list == null) {
            list = new ArrayList<>();
        }
        Cursor c = basket.select(null);
        Log.i(TAG, "loadBasketItemsFromDB() : c.getCount() : " + c.getCount());
        for (int i = 0; i < c.getCount(); i++) {
            c.moveToPosition(i);
            Log.i(TAG, "■■■■■■■■■■■■■■" +c.getInt(0));
            Log.i(TAG, "■■■■■■■■■■■■■■" +c.getString(1));
            Log.i(TAG, "■■■■■■■■■■■■■■" +c.getInt(2));
            Log.i(TAG, "■■■■■■■■■■■■■■" +c.getString(3));
            Log.i(TAG, "■■■■■■■■■■■■■■" +c.getInt(4));
            basketMap = new HashMap<>();
            basketMap.put("PRO_IMG", c.getString(0));
            basketMap.put("PRO_PRICE", c.getInt(1));
            basketMap.put("PRO_CODE", c.getInt(2));
            basketMap.put("DESIRED_STOCK_COUNT", c.getInt(3));
            basketMap.put("PRO_NAME", c.getString(4));
            list.add(basketMap);
        }
        Log.i(TAG, "list.size() : " + list.size());
        c.moveToFirst();
        return true;
    }


}