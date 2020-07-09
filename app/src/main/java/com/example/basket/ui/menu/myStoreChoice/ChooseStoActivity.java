package com.example.basket.ui.menu.myStoreChoice;

import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.basket.R;

public class ChooseStoActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sto);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //R.id.textView는 텍스트뷰 의 첫번째값 매장정보 <<
        //UI 객체생성
    }
}