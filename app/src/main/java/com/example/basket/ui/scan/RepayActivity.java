package com.example.basket.ui.scan;

import android.content.Intent;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.example.basket.R;
import com.example.basket.ui.basket.CouponChoice;
import com.example.basket.ui.basket.ListViewAdapterCustomer;
import com.example.basket.ui.basket.PaymentActivity;
import com.example.basket.util.VolleyCallback;
import com.example.basket.util.VolleyQueueProvider;
import com.example.basket.vo.MemberDTO;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.basket.ui.basket.BasketFragment.basketMoney;

public class RepayActivity extends AppCompatActivity {
    public static final String TAG = "RepayActivity";

    ListView lv_rpa_listView = null;
    ScrollView sv_repay = null;
    //쿠폰
    public static TextView tv_rpa_couponChoiceInfo = null;
    //바스켓머니
    TextView tv_rpa_basketmoney = null;
    //총금액
    TextView tv_rpa_total_pro__price = null;
    public static int rpa_total_pro__price = 0;
    //할인금액
    public static TextView tv_rpa_discountMoney = null;
    public static int rpa_discountMoney = 0;
    //총 결재금액
    public static TextView tv_rpa_total_pay = null;
    public static int rpa_total_pay = 0;

    Button btn_rpa_couponChoice = null;
    Button btn_rpa_payment_payment = null;
    Button btn_rpa_payment_cancel = null;
    List<Map<String, Object>> repayList = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repay_activity);
        if(getIntent().getStringExtra("repayData")!=null) {
            //리스트로 온다 여기부터 작업
            repayList = new Gson().fromJson(getIntent().getStringExtra("repayData"), List.class);
            int i=1;
            for(Map<String, Object> responseMap : repayList){
                responseMap.put("pro_price", (int)(Math.round((double)responseMap.get("pro_price"))));
                responseMap.put("pro_code", (int)(Math.round((double)responseMap.get("pro_code"))));
                responseMap.put("pay_ea", (int)(Math.round((double)responseMap.get("pay_ea"))));
                rpa_total_pro__price += ((int)responseMap.get("pro_price") * (int)responseMap.get("pay_ea"));

            }
        } else {
            Toast.makeText(RepayActivity.this, "서버오류", Toast.LENGTH_SHORT).show();
            return;
        }
        sv_repay = findViewById(R.id.sv_repay);
        lv_rpa_listView = findViewById(R.id.lv_rpa_listView);
        btn_rpa_couponChoice = findViewById(R.id.btn_rpa_couponChoice);
        tv_rpa_basketmoney = findViewById(R.id.tv_rpa_basketmoney);
        tv_rpa_total_pro__price = findViewById(R.id.tv_rpa_total_pro__price);
        tv_rpa_discountMoney = findViewById(R.id.tv_rpa_discountMoney);
        tv_rpa_total_pay = findViewById(R.id.tv_rpa_total_pay);
        btn_rpa_payment_payment = findViewById(R.id.btn_rpa_payment_payment);
        btn_rpa_payment_cancel = findViewById(R.id.btn_rpa_payment_cancel);
        tv_rpa_couponChoiceInfo = findViewById(R.id.tv_rpa_couponChoiceInfo);
        Log.i(TAG, "■■b : " + basketMoney);
        tv_rpa_basketmoney.setText(new DecimalFormat("###,###").format(basketMoney) + "원");
        tv_rpa_total_pro__price.setText(new DecimalFormat("###,###").format(rpa_total_pro__price) + "원");
        tv_rpa_discountMoney.setText(new DecimalFormat("###,###").format(rpa_discountMoney) + "원");
        rpa_total_pay = rpa_total_pro__price - rpa_discountMoney;
        tv_rpa_total_pay.setText(new DecimalFormat("###,###").format(rpa_total_pay) + "원");

        ArrayAdapter repayListViewAdapter = new ListViewAdapterCustomer(RepayActivity.this, RepayActivity.this, R.layout.listview_payment_info_item, repayList, RepayActivity.TAG);
        lv_rpa_listView.setAdapter(repayListViewAdapter);
        lv_rpa_listView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                sv_repay.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        btn_rpa_couponChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadEventCouponItemsFromServerDB();
            }
        });

        btn_rpa_payment_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> paymentMap = new HashMap<>();
                paymentMap.put("mem_code", Integer.toString(MemberDTO.getInstance().getMem_code()));
                paymentMap.put("sto_code", Integer.toString(MemberDTO.getInstance().getSto_code()));
                paymentMap.put("eve_code", Integer.toString(MemberDTO.getInstance().getEve_code()));
                for(int i=0; i<repayList.size(); i++) {

                    paymentMap.put("pro_code" + (i+1), repayList.get(i).get("pro_code").toString());
                    paymentMap.put("pay_ea" + (i+1),  repayList.get(i).get("pay_ea").toString());
                    Log.i(TAG, paymentMap.get("pro_code" + (i+1)));
                    Log.i(TAG, paymentMap.get("pay_ea" + (i+1)));
                }

                VolleyQueueProvider.callbackVolley(new VolleyCallback() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Map<String, Object> rMap = new Gson().fromJson(response, Map.class);
                            Intent intent = new Intent(RepayActivity.this, PaymentActivity.class);
                            intent.putExtra("pay_code", rMap.get("pay_code").toString().split("\\.")[0]);
                            intent.putExtra("txId", rMap.get("txId").toString());
                            intent.putExtra("repayData", getIntent().getStringExtra("repayData"));
                            startActivity(intent);
                            finish();
                            //제이슨을 받아서 txid 제너레이트 시그니쳐아이디에 넣으면 바이트어레이나옴
                        } catch (Exception e) {
                            Toast.makeText(RepayActivity.this, "fromJson 실패: " + response, Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.toString());
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.i(TAG, "error : " + error.toString());
                    }
                },"chain/pay_insert" ,paymentMap);
            }
        });

        btn_rpa_payment_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                rpa_total_pro__price = 0;
                rpa_discountMoney = 0;
                rpa_total_pay = 0;
            }
        });
    }

    public boolean loadEventCouponItemsFromServerDB() {
        Map<String, String> pMap = new HashMap<>();
        pMap.put("sto_code", Integer.toString(MemberDTO.getInstance().getSto_code()));
        pMap.put("total_price", Integer.toString(rpa_total_pro__price));
        VolleyQueueProvider.callbackVolley(new VolleyCallback() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "response : " + response);
                if(response!=null){
                    Intent intent = new Intent(RepayActivity.this, CouponChoice.class);
                    intent.putExtra("couponData", response);
                    intent.putExtra("type", RepayActivity.TAG);
                    startActivityForResult(intent, 1);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "error : " + error.toString());

            }
        },"payment/find_event", pMap);
        return true;
    }
}
