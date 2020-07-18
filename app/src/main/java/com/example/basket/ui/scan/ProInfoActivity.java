package com.example.basket.ui.scan;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.basket.util.SqliteTable.basket;

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
    int desired_stock_count = 1;
    int pro_stock_ea = 1;
    private ProductOneDTO productOneDTO = null;

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
        productOneDTO = new ProductOneDTO();
        if(proResultMap.get("PRO_CODE")!=null){
            productOneDTO.setPro_code((int) Math.round((double)proResultMap.get("PRO_CODE")));
        }
        if(proResultMap.get("PRO_IMG")!=null){
            productOneDTO.setPro_img(proResultMap.get("PRO_IMG").toString());

            Glide.with(this).load(proResultMap.get("PRO_IMG")).into(iv_pro_img);
        }
        if(proResultMap.get("PRO_STOCK_EA")!=null){
            productOneDTO.setPro_stock_ea((int) Math.round((double)proResultMap.get("PRO_STOCK_EA")));
            pro_stock_ea = (int) Math.round((double)proResultMap.get("PRO_STOCK_EA"));
            tv_pro_stock_ea.setText("재고수량 : " + pro_stock_ea + "개");
        }
        if(proResultMap.get("PRO_NAME")!=null){
            productOneDTO.setPro_name(proResultMap.get("PRO_NAME").toString());
            tv_pro_name.setText(proResultMap.get("PRO_NAME").toString());

        }
        if(proResultMap.get("PRO_PRICE")!=null){
            productOneDTO.setPro_price((int) Math.round((double)proResultMap.get("PRO_PRICE")));
            tv_pro_price.setText(new DecimalFormat("###,###").format(Math.round((double)proResultMap.get("PRO_PRICE")))+"원");
        }
        proList.add(productOneDTO);
        Log.i(TAG, "proList ALL SHOW START");
        for(int i=0; i<proList.size(); i++){
            Log.i(TAG, "proList.get("+i+") : " + proList.get(i));
        }
        Log.i(TAG, "proList ALL SHOW  DONE ");
    }

    public void pro_info_onClick(View view) {
        switch(view.getId()){
            case R.id.btn_sto_minus : {
                if(desired_stock_count==1){
                    Toast.makeText(this, "주문수량은 최소 1개 입니다..", Toast.LENGTH_SHORT).show();
                } else {
                    tv_desired_stock_count.setText(String.valueOf(Integer.valueOf(--desired_stock_count)));
                }
            } break;
            case R.id.btn_sto_plus : {
                Log.i(TAG, String.valueOf(Integer.valueOf(desired_stock_count)));
                Log.i(TAG, String.valueOf(Integer.valueOf(pro_stock_ea)));
                if(desired_stock_count<pro_stock_ea){
                    tv_desired_stock_count.setText(String.valueOf(Integer.valueOf(++desired_stock_count)));
                } else {
                    Toast.makeText(this, "주문수량이 재고수량보다 많습니다.", Toast.LENGTH_SHORT).show();
                }
            } break;
            case R.id.btn_basket_cancel : {
                finish();
            } break;
            case R.id.btn_basket_dunk : {
                Cursor c = basket.select("pro_code = " + productOneDTO.getPro_code());
                ContentValues row = new ContentValues();
                if (c.getCount() == 0) {
                    row.put("pro_image", productOneDTO.getPro_img());
                    row.put("pro_price", productOneDTO.getPro_price());
                    row.put("pro_code", productOneDTO.getPro_code());
                    row.put("pay_ea", desired_stock_count);
                    row.put("pro_name", productOneDTO.getPro_name());
                    basket.insert(row);
                } else {
                    c.moveToFirst();
                    int ea = c.getInt(3);
                    Log.i(TAG, "■■■■■■ EA : " + ea);
                    Log.i(TAG, "■■■■■■ desired_stock_count : " + desired_stock_count);
                    int eades = ea + desired_stock_count;
                    Log.i(TAG, "■■■■■■ ea + desired_stock_count : " + eades);
                    Log.i(TAG, Integer.toString(eades));
                    row.put("pay_ea", ea + desired_stock_count);
                    basket.update(row, "pro_code = " + productOneDTO.getPro_code());
                }
                Toast.makeText(this, "장바구니에 추가되었습니다.", Toast.LENGTH_SHORT).show();
                String last_barcode = getIntent().getStringExtra("last_barcode");
                last_barcode = null;
                finish();
            } break;
            default : break;
        }

    }
}