package com.example.basket.ui.scan;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.basket.R;
import com.example.basket.vo.ProductOneDTO;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProInfoActivity extends Activity {
    public static final String TAG = "ProInfoActivity";
    ImageView iv_pro_img = null;
    TextView tv_pro_name = null;
    TextView tv_pro_price = null;
    TextView tv_pro_stock_ea = null;
    Button btn_sto_minus = null;
    TextView tv_desired_stock_count = null;
    Button btn_sto_plus = null;
    Button btn_basket_cancel = null;
    Button btn_basket_dunk = null;
    int desired_stock_count = 0;


    List<ProductOneDTO> proList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pro_info);
        Log.i(TAG, "data : " + getIntent().getStringExtra("data"));
        iv_pro_img = findViewById(R.id.iv_pro_img);
        tv_pro_name = findViewById(R.id.tv_pro_name);
        tv_pro_price = findViewById(R.id.tv_pro_price);
        tv_pro_stock_ea = findViewById(R.id.tv_pro_stock_ea);
        btn_sto_minus = findViewById(R.id.btn_sto_minus);
        tv_desired_stock_count = findViewById(R.id.tv_desired_stock_count);
        btn_sto_plus = findViewById(R.id.btn_sto_plus);
        btn_basket_cancel = findViewById(R.id.btn_basket_cancel);
        btn_basket_dunk = findViewById(R.id.btn_basket_dunk);
        Map<String, Object> proResultMap = (Map<String, Object>)((List<Map<String, Object>>)(new Gson().fromJson(getIntent().getStringExtra("data"), List.class))).get(0);
        ProductOneDTO productOneDTO = new ProductOneDTO();
        if(proResultMap.get("PRO_CODE")!=null){
            productOneDTO.setPro_code(proResultMap.get("PRO_CODE").toString());
        }
        if(proResultMap.get("PRO_IMG")!=null){
            productOneDTO.setPro_code(proResultMap.get("PRO_IMG").toString());
            Glide.with(this).load(proResultMap.get("PRO_IMG").toString()).into(iv_pro_img);
        }
        if(proResultMap.get("PRO_STOCK_EA")!=null){
            productOneDTO.setPro_stock_ea(proResultMap.get("PRO_STOCK_EA").toString());
            tv_pro_stock_ea.setText(proResultMap.get("PRO_STOCK_EA").toString());

        }
        if(proResultMap.get("PRO_NAME")!=null){
            productOneDTO.setPro_name(proResultMap.get("PRO_NAME").toString());
            tv_pro_name.setText(proResultMap.get("PRO_NAME").toString()+"원");

        }
        if(proResultMap.get("PRO_PRICE")!=null){
            productOneDTO.setPro_price(proResultMap.get("PRO_PRICE").toString());
            tv_pro_price.setText("재고수량 : " + proResultMap.get("PRO_PRICE").toString());
        }
        proList.add(productOneDTO);
        Log.i(TAG, "proList ALL SHOW START");
        for(int i=0; i<proList.size(); i++){
            Log.i(TAG, "proList.get("+i+").toString() : " + proList.get(i).toString());
        }
        Log.i(TAG, "proList ALL SHOW  DONE ");
    }

    public void pro_info_onClick(View view) {
        switch(view.getId()){
            case R.id.btn_sto_minus : {
                int pro_stock_ea = Integer.parseInt(tv_pro_stock_ea.getText().toString());
                desired_stock_count = Integer.parseInt(tv_desired_stock_count.getText().toString());
                if(desired_stock_count==1&&desired_stock_count!=0){
                    Toast.makeText(this, "주문수량은 최소 1개 입니다..", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    tv_desired_stock_count.setText(--desired_stock_count);
                    return;
                }
            }
            case R.id.btn_sto_plus : {
                int pro_stock_ea = Integer.parseInt(tv_pro_stock_ea.getText().toString());
                desired_stock_count = Integer.parseInt(tv_desired_stock_count.getText().toString());
                if(desired_stock_count<pro_stock_ea){
                    tv_desired_stock_count.setText(++desired_stock_count);
                    return;
                } else {
                    Toast.makeText(this, "주문수량이 재고수량보다 많습니다.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            case R.id.btn_basket_cancel : {
                finish();
            }
            case R.id.btn_basket_dunk : {
                /*************************************
                 *               SQLite
                 *
                 *
              
                 ***********************************/



            }
        }

    }
}