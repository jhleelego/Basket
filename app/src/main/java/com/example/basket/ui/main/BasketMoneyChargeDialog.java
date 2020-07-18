package com.example.basket.ui.main;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.example.basket.R;
import com.example.basket.util.VolleyCallback;
import com.example.basket.util.VolleyQueueProvider;
import com.example.basket.vo.MemberDTO;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.example.basket.ui.basket.BasketFragment.basketMoney;
import static com.example.basket.ui.menu.MenuFragment.tv_menu_basketMoney;

public class BasketMoneyChargeDialog extends AppCompatActivity {
    public static final String TAG ="BasketMoneyChargeDialog";
    Button btn_basketCharge = null;
    Button btn_basketCharge_cancel = null;

    private DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private EditText et_basket;
    private String result="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_money_charge_dialog);
        et_basket = (EditText) findViewById(R.id.et_basket);
        et_basket.addTextChangedListener(watcher);
        btn_basketCharge = findViewById(R.id.btn_basketCharge);
        btn_basketCharge_cancel = findViewById(R.id.btn_basketCharge_cancel);
        btn_basketCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giveMeTheMoney();
            }
        });
        btn_basketCharge_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
    public void giveMeTheMoney() {
        Log.i(TAG, "result : " + result);
        int cash = Integer.parseInt(result.replace(",", ""));
        if(cash==0){
            Toast.makeText(BasketMoneyChargeDialog.this, "충전금을 입력하여주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> pMap = new HashMap<>();
        Log.i(TAG, "mem_code : " + Integer.toString(MemberDTO.getInstance().getMem_code()));
        Log.i(TAG, "cash : " + String.valueOf(cash));
        pMap.put("mem_code", Integer.toString(MemberDTO.getInstance().getMem_code()));
        pMap.put("cash", String.valueOf(cash));
        VolleyQueueProvider.callbackVolley(new VolleyCallback() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "response : " + response);
                if(response.length()==0){
                    Toast.makeText(BasketMoneyChargeDialog.this, "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, Object> rMap = new Gson().fromJson(response, Map.class);
                Log.i(TAG, "cash: " + rMap.get("cash"));
                Log.i(TAG, "total: " + rMap.get("total"));
                basketMoney = Integer.valueOf(rMap.get("total").toString());
                tv_menu_basketMoney.setText(Currency.getInstance(Locale.KOREA).getSymbol() + new DecimalFormat("###,###").format(basketMoney));
                Toast.makeText(BasketMoneyChargeDialog.this, "충전 완료되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                Toast.makeText(BasketMoneyChargeDialog.this, "서버오류", Toast.LENGTH_SHORT).show();
                finish();
            }
        }, "chain/cash", pMap);
    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(!TextUtils.isEmpty(charSequence.toString()) && !charSequence.toString().equals(result)){

                result = decimalFormat.format(Double.parseDouble(charSequence.toString().replaceAll(",","")));
                et_basket.setText(result);
                et_basket.setSelection(result.length());
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


}