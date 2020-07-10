package com.example.basket.ui.basket;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.basket.R;
import com.example.basket.vo.DBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BasketFragment extends Fragment {
    public static final String TAG ="BasketFragment";
    private Context mContext = null;

    public static BasketFragment newInstance() {
        return new BasketFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_basket, container, false);
        ArrayList<ListViewBasketItem> items = new ArrayList<>() ;
        loadItemsFromDB(items) ;
        ListViewBasketAdapter adapter = new ListViewBasketAdapter(mContext, R.layout.listview_basket_item, items);
        ListView listview = (ListView) root.findViewById(R.id.lv_basket);
        listview.setAdapter(adapter);
        return root;
    }

    public boolean loadItemsFromDB(ArrayList<ListViewBasketItem> list) {
        ListViewBasketItem item;
        if (list == null) {
            list = new ArrayList<ListViewBasketItem>();
        }
        DBHelper dbHelper = new DBHelper(this.getContext());
        List<Map<String, String>> basketList = dbHelper.getResult();
        for(int i=0; i<basketList.size(); i++){
            item = new ListViewBasketItem();
            Map<String, String> basketOneRowMap = basketList.get(i);
            item.setPro_img(basketOneRowMap.get("PRO_IMG"));
            item.setDesired_stock_count(basketOneRowMap.get("PRO_STOCK_EA"));
            item.setPro_name(basketOneRowMap.get("PRO_NAME"));
            item.setPro_price( basketOneRowMap.get("PRO_PRICE"));
            list.add(item);
        }
        return true;
    }
}