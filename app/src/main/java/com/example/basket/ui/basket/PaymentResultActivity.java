package com.example.basket.ui.basket;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.basket.R;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.Map;

import static com.example.basket.ui.basket.BasketFragment.discountMoney;
import static com.example.basket.ui.basket.BasketFragment.total_pro_price;
import static com.example.basket.util.SqliteTable.basket;

public class PaymentResultActivity extends AppCompatActivity {
    public static final String TAG = "PaymentResultDialog";
    private TextView tv_resultPay_status = null;
    private TextView tv_pror_name = null;
    private TextView tv_payr_code  = null;
    private TextView tv_payr_date  = null;
    private TextView tv_payr_total = null;
    private TextView tv_payr_discount = null;
    private TextView tv_payr_value = null;
    private Button btn_payment_ok = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_result);
        tv_resultPay_status = findViewById(R.id.tv_resultPay_status);
        tv_pror_name = findViewById(R.id.tv_pror_name);
        tv_payr_code = findViewById(R.id.tv_payr_code);
        tv_payr_date = findViewById(R.id.tv_payr_date);
        tv_payr_total = findViewById(R.id.tv_payr_total);
        tv_payr_discount = findViewById(R.id.tv_payr_discount);
        tv_payr_value = findViewById(R.id.tv_payr_value);
        btn_payment_ok = findViewById(R.id.btn_payment_ok);

        Map<String, Object> resultMap = new Gson().fromJson(getIntent().getStringExtra("response"), Map.class);
        if(resultMap.get("PAY_STATUS")!=null){
            if(resultMap.get("PAY_STATUS").equals("결제완료")){
                tv_resultPay_status.setText(resultMap.get("PAY_STATUS").toString() + "되었습니다!");
            } else if(resultMap.get("PAY_STATUS").equals("결제실패")){
                tv_resultPay_status.setText(resultMap.get("PAY_STATUS").toString() + "하였습니다.");
            }
        }
        if(resultMap.get("PRO_NAME")!=null){
            tv_pror_name.setText(resultMap.get("PRO_NAME").toString());
        }
        if(resultMap.get("PAY_CODE")!=null){
            tv_payr_code.setText(Integer.toString((int)Math.round((double)resultMap.get("PAY_CODE"))));
        }
        if(resultMap.get("PAY_DATE")!=null){
            tv_payr_date.setText(resultMap.get("PAY_DATE").toString());
        }
        if(resultMap.get("PAY_VALUE")!=null){
            tv_payr_value.setText(new DecimalFormat("###,###").format((Integer.valueOf((int)Math.round((double)resultMap.get("PAY_VALUE"))))) + "원");
        }
        tv_payr_total.setText(new DecimalFormat("###,###").format(total_pro_price) + "원");
        tv_payr_discount.setText(new DecimalFormat("###,###").format(discountMoney) + "원");

        btn_payment_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                basket.delete(null);
                total_pro_price = 0;
                discountMoney = 0;
                finish();
            }
        });
    }
}