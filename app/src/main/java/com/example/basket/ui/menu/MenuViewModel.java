package com.example.basket.ui.menu;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.lifecycle.ViewModel;


public class MenuViewModel extends ViewModel {
    public String[] listMenu = null;


    public MenuViewModel() {
        listMenu = new String[] { "마이페이지", "구매내역", "내쿠폰", "이벤트", "나의 매장 선택", "고객센터"};

    }
    public ArrayAdapter getList(Context context) {
        return new ArrayAdapter(context, android.R.layout.simple_list_item_1, listMenu);
    }
}