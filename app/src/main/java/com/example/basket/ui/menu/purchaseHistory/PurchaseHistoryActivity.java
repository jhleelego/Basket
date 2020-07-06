package com.example.basket.ui.menu.purchaseHistory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.basket.R;

import java.util.ArrayList;

public class PurchaseHistoryActivity extends AppCompatActivity {
    private ListView m_oListView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history);
// 데이터 생성.
        String[] strDate = {"2017-01-03", "1965-02-23", "2016-04-13", "2010-01-01", "2017-06-20",
                "2012-07-08", "1980-04-14", "2016-09-26", "2014-10-11", "2010-12-24"};
        int nDatCnt=0;
        ArrayList<ItemData> oData = new ArrayList<>();
        for (int i=0; i<10; ++i)
        {
            ItemData oItem = new ItemData();
            oItem.strTitle = "홈플러스 " + (i+1);
            oItem.strDate = strDate[nDatCnt++];
            oData.add(oItem);
            if (nDatCnt >= strDate.length) nDatCnt = 0;
        }

        // ListView 생성
        m_oListView = (ListView)findViewById(R.id.aph_listview);
        ListAdapter oAdapter = new ListAdapter(oData);
        m_oListView.setAdapter(oAdapter);


    }
    public void move_detail(View view){
        Button aph_btn = findViewById(R.id.aph_btn);
        Intent intent = new Intent(this, PurchaseHistoryDetailActivity.class);
        startActivity(intent);
    }
}