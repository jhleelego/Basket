package com.example.basket.ui.menu;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.lifecycle.ViewModel;


public class MenuViewModel extends ViewModel {
    public String[] listMenu = null;


    public MenuViewModel() {
        listMenu = new String[] { "마이페이지", "구매내역", "나의 관심매장 이벤트쿠폰", "나의 관심 매장", "고객센터"};

    }
    public ArrayAdapter getList(Context context) {
        return new ArrayAdapter(context, android.R.layout.simple_list_item_1, listMenu);
    }
}