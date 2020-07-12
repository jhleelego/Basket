package com.example.basket.ui.basket;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.example.basket.R;

public class PaymentDialog extends Activity {

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
                /***********************************
                 *
                 * 슈퍼 특급 최종 결제
                 *
                 ***********************************/

            } break;
            default : break;
        }
    }
}