package com.example.basket.ui.scan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.basket.R;
import com.example.basket.vo.ProductOneDTO;

public class ProInfoActivity extends AppCompatActivity {
    ImageView s_pro_img = null;
    TextView lb_pro_name = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pro_info);

        Intent intent = getIntent();
        ProductOneDTO productOneDTO = (ProductOneDTO) intent.getSerializableExtra("data");
        s_pro_img = (ImageView) findViewById(R.id.s_pro_img);
        lb_pro_name = (TextView) findViewById(R.id.lb_pro_name);
        lb_pro_name.setText(productOneDTO.getPRO_NAME());





    }
}