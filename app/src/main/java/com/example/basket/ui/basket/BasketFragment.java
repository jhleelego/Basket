package com.example.basket.ui.basket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
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

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.security.PublicKey;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    public static TextView tv_total_pro__price = null;
    public static int total_pro_price = 0;
    //쿠폰상세
    public static TextView tv_couponChoiceInfo = null;
    //할인 금액
    public static TextView tv_discountMoney = null;
    public static int discountMoney = 0;
    //총 결제 금액
    public static TextView tv_total_pay = null;
    public static int total_pay = 0;

    //쿠폰 버튼
    public Button btn_couponChoice = null;

    //결제 버튼
    public Button btn_payment = null;

    public ScrollView sv_basket = null;
    public static BasketFragment newInstance() {
        return new BasketFragment();
    }
    public View root = null;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
        this.mActivity = getActivity();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_basket, container, false);
        sv_basket = root.findViewById(R.id.sv_basket);
        btn_payment = root.findViewById(R.id.btn_payment);
        adapting();
        Map<String, String> pMap = new HashMap<>();
        try {
            PublicKey myKey = MemberDTO.getInstance().getMem_wallet().publicKey;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(baos));
            oos.writeObject(myKey);
            oos.close();
            pMap.put("myKey", new String(baos.toByteArray(), "ISO-8859-1"));
        } catch (Exception e) {
            Log.e(TAG, "publicKey into Map error");
        }
        VolleyQueueProvider.callbackVolley(new VolleyCallback() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "asdasdasdasdasdasdasdasdasdasdasd");
                Log.i(TAG, response);
                if(response.length()==0){
                    Toast.makeText(mActivity, "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String[] basketDouble = response.split("\\(");
                Log.i(TAG, Integer.toString((int)(Double.parseDouble(basketDouble[0]))));
                Log.i(TAG, "■■■");
                Log.i(TAG, "■■■" + basketMoney);
                basketMoney = (int)(Double.parseDouble(basketDouble[0]));
                Log.i(TAG, "■■■" + basketMoney);
                Log.i(TAG, "■■■");
                tv_basketMoney.setText(new DecimalFormat("###,###").format(basketMoney) + "원");
                Log.i(TAG, "asdasdasdasdasdasdasdasdasdasdasd");
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        }, "chain/show_me_the_money7", pMap);

        btn_couponChoice = root.findViewById(R.id.btn_couponChoice);
        btn_couponChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadEventCouponItemsFromServerDB();
            }
        });
        btn_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor rs = SqliteTable.basket.select(null);
                if (rs.getCount() > 0) {
                    Log.i(TAG, "mem_code : " + MemberDTO.getInstance().getMem_code());
                    Log.i(TAG, "sto_code : " + MemberDTO.getInstance().getSto_code());
                    Log.i(TAG, "eve_code : " + MemberDTO.getInstance().getEve_code());
                    Map<String, String> paymentMap = new HashMap<>();
                    paymentMap.put("mem_code", Integer.toString(MemberDTO.getInstance().getMem_code()));
                    paymentMap.put("sto_code", Integer.toString(MemberDTO.getInstance().getSto_code()));
                    paymentMap.put("eve_code", Integer.toString(MemberDTO.getInstance().getEve_code()));
                    for (int i = 0; i < rs.getCount(); i++) {
                        rs.moveToPosition(i);
                        paymentMap.put("pro_code" + (i + 1), String.valueOf(rs.getInt(2)));
                        paymentMap.put("pay_ea" + (i + 1), String.valueOf(rs.getInt(3)));
                    }
                    VolleyQueueProvider.callbackVolley(new VolleyCallback() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Map<String, Object> rMap = new Gson().fromJson(response, Map.class);
                                Intent intent = new Intent(mActivity, PaymentActivity.class);
                                intent.putExtra("pay_code", rMap.get("pay_code").toString().split("\\.")[0]);
                                intent.putExtra("txId", rMap.get("txId").toString());
                                startActivity(intent);
                                //제이슨을 받아서 txid 제너레이트 시그니쳐아이디에 넣으면 바이트어레이나옴
                            } catch (Exception e) {
                                Toast.makeText(mContext, "fromJson 실패: " + response, Toast.LENGTH_SHORT).show();
                                Log.e(TAG, e.toString());
                            }
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

    public boolean loadBasketItemsFromDB(List<Map<String, Object>> list) {
        Log.i(TAG, "loadBasketItemsFromDB()");
        Map<String, Object> basketMap;
        if (list == null) {
            list = new ArrayList<>();
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
            basketMap = new HashMap<>();
            basketMap.put("PRO_IMG", c.getString(0));
            basketMap.put("PRO_PRICE", c.getInt(1));
            basketMap.put("PRO_CODE", c.getInt(2));
            basketMap.put("DESIRED_STOCK_COUNT", c.getInt(3));
            basketMap.put("PRO_NAME", c.getString(4));
            list.add(basketMap);
        }
        Log.i(TAG, "list.size() : " + list.size());
        c.moveToFirst();
        return true;
    }
    public boolean loadEventCouponItemsFromServerDB() {
        Map<String, String> pMap = new HashMap<>();
        pMap.put("sto_code", Integer.toString(MemberDTO.getInstance().getSto_code()));
        pMap.put("total_price", Integer.toString(total_pro_price));
        VolleyQueueProvider.callbackVolley(new VolleyCallback() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "response : " + response);
                if(response!=null){
                    Intent intent = new Intent(getActivity(), CouponChoice.class);
                    intent.putExtra("couponData", response);
                    intent.putExtra("type", BasketFragment.TAG);
                    startActivityForResult(intent, 1);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "error : " + error.toString());

            }
        },"payment/find_event", pMap);
        return true;
    }


    public void adapting() {
        List<Map<String, Object>> basketItemList = new ArrayList<>();
        loadBasketItemsFromDB(basketItemList);
        Log.i(TAG, "basketItemList.size() : " + basketItemList.size());
        ArrayAdapter basketAdapter = new ListViewAdapterCustomer(mActivity, mContext, R.layout.listview_basket_item, basketItemList, BasketFragment.TAG);
        ListView basketListview = (ListView) root.findViewById(R.id.lv_basket);
        basketListview.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                sv_basket.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        tv_basketMoney = root.findViewById(R.id.tv_basketMoney);
        tv_couponChoiceInfo = root.findViewById(R.id.tv_couponChoiceInfo);
        tv_discountMoney = root.findViewById(R.id.tv_discountMoney);
        tv_total_pro_price = root.findViewById(R.id.tv_total_pro_price);
        tv_total_pro__price = root.findViewById(R.id.tv_total_pro__price);
        tv_total_pay = root.findViewById(R.id.tv_total_pay);


        String[] columns = {"sum(pro_price * pay_ea)"};
        Cursor c = basket.selects(columns, null);
        if(c.getCount()!=0){
            c.moveToPosition(0);
            tv_total_pro_price.setText("총 금액 : " + new DecimalFormat("###,###").format(c.getInt(0)) + "원");
            tv_total_pro__price.setText(new DecimalFormat("###,###").format(c.getInt(0)) + "원");
            total_pro_price = c.getInt(0);
            total_pay = total_pro_price - discountMoney;
            tv_total_pay.setText(" = " + new DecimalFormat("###,###").format(total_pay) + "원");
        }
        total_pay = total_pro_price - discountMoney;
        tv_total_pay.setText(" = " + new DecimalFormat("###,###").format(total_pay) + "원");
        if(discountMoney==0){
            tv_couponChoiceInfo.setText("");
            tv_discountMoney.setText(" - " + discountMoney + "원");
        } else {
            tv_couponChoiceInfo.setText(MemberDTO.getInstance().getSto_name()
                    + "  "
                    + new DecimalFormat("###,###").format(discountMoney)
                    + "원 할인 쿠폰 적용중");
        }
        basketListview.setAdapter(basketAdapter);
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "■■■■■■■■■■■■■■■■■■■■■■■onStart");
        adapting();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "■■■■■■■■■■■■■■■■■■■■■■■onDestroy");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "■■■■■■■■■■■■■■■■■■■■■■■onCreate");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "■■■■■■■■■■■■■■■■■■■■■■■onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}