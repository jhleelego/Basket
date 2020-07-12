package com.example.basket.ui.basket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.example.basket.R;
import com.example.basket.util.SqliteTable;
import com.example.basket.util.VolleyCallback;
import com.example.basket.util.VolleyQueueProvider;
import com.example.basket.vo.MemberDTO;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.basket.util.SqliteTable.basket;

public class BasketFragment extends Fragment {
    public static final String TAG = "BasketFragment";
    private Context mContext = null;
    private Activity mActivity = null;

    //바스켓머니
    public static TextView tv_basketMoney = null;
    public static int basketMoney = 0;
    //총 금액
    public static TextView tv_total_pro_price = null;
    public static int total_pro_price = 0;
    //쿠폰상세
    public static TextView tv_couponChoiceInfo = null;
    //할인 금액
    public static TextView tv_discountMoney = null;
    public static int discountMoney = 0;
    //총 결제 금액
    public static TextView tv_total_pay = null;
    public static int total_pay = 0;
    //결제 버튼
    public Button btn_payment = null;

    public static BasketFragment newInstance() {
        return new BasketFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
        this.mActivity = getActivity();


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_basket, container, false);
        ArrayList<ListViewBasketItem> basketItems = new ArrayList<>();
        loadBasketItemsFromDB(basketItems);
        Log.i(TAG, "basketItems.size() : " + basketItems.size());
        ListViewBasketAdapter basketAdapter = new ListViewBasketAdapter(mContext, R.layout.listview_basket_item, basketItems);
        ListView basketListview = (ListView) root.findViewById(R.id.lv_basket);
        basketListview.setAdapter(basketAdapter);

        tv_basketMoney = root.findViewById(R.id.tv_basketMoney);
        tv_total_pro_price = root.findViewById(R.id.tv_total_pro__prise);
        tv_total_pay = root.findViewById(R.id.tv_total_pay);
        tv_couponChoiceInfo = root.findViewById(R.id.tv_couponChoiceInfo);
        tv_discountMoney = root.findViewById(R.id.tv_discountMoney);
        btn_payment = root.findViewById(R.id.btn_payment);

        String[] columns = {"sum(pro_price * pay_ea)"};
        Cursor c = basket.selects(columns, null);
        if(c.getCount()!=0){
            c.moveToPosition(0);
            tv_total_pro_price.setText("총 금액 : " + c.getInt(0) + " 원");
            total_pro_price = c.getInt(0);
            tv_total_pay.setText(" = " + (total_pro_price - discountMoney) + "원");
            total_pay = total_pro_price - discountMoney;
        }
        tv_total_pay.setText(" = " + (total_pro_price - discountMoney) + "원");
        total_pay = total_pro_price - discountMoney;

        btn_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor rs = SqliteTable.basket.select(null);
                if (rs.getCount() > 0) {
                    Log.i(TAG, "mem_code : " + MemberDTO.getInstance().getMem_code());
                    Log.i(TAG, "sto_code : " + MemberDTO.getInstance().getSto_code());
                    Map<String, String> paymentMap = new HashMap<>();
                    paymentMap.put("mem_code", MemberDTO.getInstance().getMem_code());
                    paymentMap.put("sto_code", MemberDTO.getInstance().getSto_code());
                    for (int i = 0; i < rs.getCount(); i++) {
                        rs.moveToPosition(i);
                        paymentMap.put("pro_code" + (i + 1), String.valueOf(rs.getInt(2)));
                        paymentMap.put("pay_ea" + (i + 1), String.valueOf(rs.getInt(3)));
                    }
//                paymentMap.put("total_pay", Integer.toString(total_pay));
                    VolleyQueueProvider.callbackVolley(new VolleyCallback() {
                        @Override
                        public void onResponse(String response) {
                            Map<String, String> rMap = new Gson().fromJson(response, Map.class);
//                            rMap.put("pay_code", pay_code);
//                            rMap.put("pay_value", totalPrice + "원");
//                            rMap.put("mem_code", Integer.parseInt((String) pMap.get("mem_code")));
//                            rMap.put("sto_code", Integer.parseInt((String) pMap.get("sto_code")));
//                            rMap.put("txId", payTx.txId);
                            Intent intent = new Intent(mActivity, PaymentDialog.class);
                            intent.putExtra("pay_code", rMap.get("pay_code"));
                            intent.putExtra("txId", rMap.get("txId"));
                            startActivityForResult(intent, 1);

                            //제이슨을 받아서 txid 제너레이트 시그니쳐아이디에 넣으면 바이트어레이나옴
                        }

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Log.i(TAG, "error : " + error.toString());
                        }
                    },"chain/pay_insert" ,paymentMap);
                } else {
                    Toast.makeText(mContext, "장바구니가 비어있습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return root;
    }

    public boolean loadBasketItemsFromDB(ArrayList<ListViewBasketItem> list) {
        Log.i(TAG, "loadBasketItemsFromDB()");
        ListViewBasketItem item;
        if (list == null) {
            list = new ArrayList<ListViewBasketItem>();
        }
        Cursor c = basket.select(null);
        Log.i(TAG, "loadBasketItemsFromDB() : c.getCount() : " + c.getCount());
        for (int i = 0; i < c.getCount(); i++) {
            c.moveToPosition(i);

            Log.i(TAG, "■■■■■■■■■■■■■■" +c.getInt(0));
            Log.i(TAG, "■■■■■■■■■■■■■■" +c.getString(1));
            Log.i(TAG, "■■■■■■■■■■■■■■" +c.getInt(2));
            Log.i(TAG, "■■■■■■■■■■■■■■" +c.getString(3));
            Log.i(TAG, "■■■■■■■■■■■■■■" +c.getInt(4));

            item = new ListViewBasketItem();
            item.setPro_img(c.getString(0));
            item.setPro_price( c.getInt(1));
            item.setPro_code(c.getInt(2));
            item.setDesired_stock_count(c.getInt(3));
            item.setPro_name(c.getString(4));
            list.add(item);
        }
        Log.i(TAG, "list.size() : " + list.size());
        c.moveToFirst();

        return true;
    }





}