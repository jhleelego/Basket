package com.example.basket.ui.menu.paymenthistory;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.example.basket.R;
import com.example.basket.ui.basket.ListViewAdapterCustomer;
import com.example.basket.util.VolleyCallback;
import com.example.basket.util.VolleyQueueProvider;
import com.example.basket.vo.MemberDTO;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentInfoActivity extends AppCompatActivity {
    public static final String TAG = "PaymentInfoActivity";
    Activity mActivity = null;
    Context mContext = null;
    public TextView tv_pi_pay_status = null;
    public TextView tv_pi_pay_date = null;
    public TextView tv_pi_pay_code = null;
    public TextView tv_pi_sto_name = null;
    public TextView tv_pi_total_prise = null;
    public TextView tv_pi_discountMoney = null;
    public TextView tv_pi_total_pay = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_payment_info);
        Log.i(TAG, getIntent().getStringExtra("paymentChoice"));
        Log.i(TAG, getIntent().getStringExtra("paymentInfodata"));
        Map<String, Object> paymentChoiceMap = new Gson().fromJson(getIntent().getStringExtra("paymentChoice"), Map.class);
        List<Map<String, Object>> paymentInfoList = new Gson().fromJson(getIntent().getStringExtra("paymentInfodata"), List.class);
        mActivity = (Activity)this;
        mContext = (Context)this;

        tv_pi_pay_status = findViewById(R.id.tv_pi_pay_status);
        tv_pi_pay_date = findViewById(R.id.tv_pi_pay_date);
        tv_pi_pay_code = findViewById(R.id.tv_pi_pay_code);
        tv_pi_sto_name = findViewById(R.id.tv_pi_sto_name);
        tv_pi_total_prise = findViewById(R.id.tv_pi_total_prise);
        tv_pi_total_pay = findViewById(R.id.tv_pi_total_pay);
        tv_pi_pay_status.setText(paymentChoiceMap.get("PAY_STATUS").toString());
        tv_pi_pay_date.setText(paymentChoiceMap.get("PAY_DATE").toString());
        tv_pi_pay_code.setText("주문번호  " + ((int)Math.round((double)paymentChoiceMap.get("PAY_CODE"))));
        tv_pi_sto_name.setText("매장명 : " + paymentChoiceMap.get("STO_NAME").toString());
        int pi_pay_total_pay = (int)Math.round((double)paymentChoiceMap.get("PAY_VALUE"));
        if(paymentChoiceMap.get("DISCOUNT_PRICE")!=null){
            int pi_pay_discount_price = (int)Math.round((double)paymentChoiceMap.get("DISCOUNT_PRICE"));
            tv_pi_discountMoney = findViewById(R.id.tv_pi_discountMoney);
            tv_pi_discountMoney.setText((new DecimalFormat("###,###").format(pi_pay_discount_price)));
            tv_pi_total_prise.setText(new DecimalFormat("###,###").format(pi_pay_total_pay+pi_pay_discount_price) + "원");
        } else {
            tv_pi_total_prise.setText("= " + new DecimalFormat("###,###").format(pi_pay_total_pay) + "원");
        }
        tv_pi_total_pay.setText("= " + new DecimalFormat("###,###").format(pi_pay_total_pay) + "원");
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("mem_code", Integer.toString(MemberDTO.getInstance().getMem_code()));
        requestMap.put("pay_code", paymentChoiceMap.get("PAY_CODE").toString());
        VolleyQueueProvider.callbackVolley(new VolleyCallback() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "response : " + response);
                List<Map<String, Object>> paymentInfoItemList = new Gson().fromJson(response, List.class);
                Log.i(TAG, "paymentInfoItemList.size() : " + paymentInfoItemList.size());
                ArrayAdapter paymentInfoAdapter = new ListViewAdapterCustomer(mActivity, mContext, R.layout.listview_payment_info_item, paymentInfoItemList, PaymentInfoActivity.TAG);
                ListView paymentInfoListView = findViewById(R.id.lv_payInfo);
                paymentInfoListView.setAdapter(paymentInfoAdapter);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "error : " + error);
                error.printStackTrace();
            }
        },"payment/detail_pay", requestMap);
    }
}