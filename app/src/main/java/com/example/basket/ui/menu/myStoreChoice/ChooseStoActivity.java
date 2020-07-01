package com.example.basket.ui.menu.myStoreChoice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.example.basket.R;

public class ChooseStoActivity extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sto);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_choose_sto);
            //R.id.textView는 텍스트뷰 의 첫번째값 매장정보 <<
            //UI 객체생성
        textView = (TextView)findViewById(R.id.textView);

         //데이터 가져오기.
        Intent intent =getIntent();
        String data = intent.getStringExtra("data");
        textView.setText(data);
    }
        //확인버튼 클릭???

}