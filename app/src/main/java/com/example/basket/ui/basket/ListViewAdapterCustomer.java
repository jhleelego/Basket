package com.example.basket.ui.basket;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.example.basket.R;
import com.example.basket.ui.menu.favevent.FavCouponActivity;
import com.example.basket.ui.menu.favstore.FavStoreActivity;
import com.example.basket.ui.menu.favstore.FavStoreDelActivity;
import com.example.basket.ui.menu.paymenthistory.PaymentHistoryActivity;
import com.example.basket.ui.menu.paymenthistory.PaymentInfoActivity;
import com.example.basket.ui.scan.RepayActivity;
import com.example.basket.ui.search.StoreMapInfoDialog;
import com.example.basket.util.VolleyCallback;
import com.example.basket.util.VolleyQueueProvider;
import com.example.basket.vo.MemberDTO;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.basket.ui.basket.BasketFragment.discountMoney;
import static com.example.basket.ui.basket.BasketFragment.total_pay;
import static com.example.basket.ui.basket.BasketFragment.total_pro_price;
import static com.example.basket.ui.basket.BasketFragment.tv_couponChoiceInfo;
import static com.example.basket.ui.basket.BasketFragment.tv_discountMoney;
import static com.example.basket.ui.basket.BasketFragment.tv_total_pay;
import static com.example.basket.ui.basket.BasketFragment.tv_total_pro__price;
import static com.example.basket.ui.basket.BasketFragment.tv_total_pro_price;
import static com.example.basket.ui.scan.RepayActivity.rpa_discountMoney;
import static com.example.basket.ui.scan.RepayActivity.rpa_total_pay;
import static com.example.basket.ui.scan.RepayActivity.rpa_total_pro__price;
import static com.example.basket.ui.scan.RepayActivity.tv_rpa_couponChoiceInfo;
import static com.example.basket.ui.scan.RepayActivity.tv_rpa_discountMoney;
import static com.example.basket.ui.scan.RepayActivity.tv_rpa_total_pay;
import static com.example.basket.util.SqliteTable.basket;


public class ListViewAdapterCustomer extends ArrayAdapter implements View.OnClickListener  {
    public static final String TAG = "ListViewBasketAdapter";
    String mTag = null;

    @Override
    public void onClick(View v) {}

    List<Map<String, Object>> itemList = null;

    int resourceId ;
    private Context mContext = null;
    private Activity mActivity = null;

    public ListViewAdapterCustomer(Activity activity, Context context, int resource, List<Map<String, Object>> list, String tag) {
        super(context, resource, list) ;
        this.mTag = tag;
        this.itemList = list;
        this.mContext = context;
        this.resourceId = resource;
        this.mActivity = activity;
    }
  

    // 새롭게 만든 Layout을 위한 View를 생성하는 코드
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(TAG, "position : " + position);
        final int pos = position ;
        final Context context = parent.getContext();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resourceId, parent, false);
        }
        if(mTag.equals(BasketFragment.TAG)){
            final int pro_code = (int)itemList.get(pos).get("PRO_CODE");
            // 화면에 표시될 View(Layout이 inflate된)로부터 위젯에 대한 참조 획득
            ImageView iv_pro_img = (ImageView) convertView.findViewById(R.id.iv_pro_img);
            TextView tv_pro_name = (TextView) convertView.findViewById(R.id.tv_pro_name);
            Button btn_sto_minus = (Button) convertView.findViewById(R.id.btn_sto_minus);
            TextView tv_desired_stock_count = (TextView) convertView.findViewById(R.id.tv_desired_stock_count_adapted);
            TextView tv_pro_price = (TextView) convertView.findViewById(R.id.tv_pro_price);
            Button btn_sto_plus = (Button) convertView.findViewById(R.id.btn_sto_plus);
            Button btn_basket_one_delete = (Button) convertView.findViewById(R.id.btn_basket_one_delete);
            Glide.with(convertView).load(itemList.get(pos).get("PRO_IMG")).into(iv_pro_img);
            tv_pro_name.setText(itemList.get(pos).get("PRO_NAME").toString());
            tv_desired_stock_count.setText(itemList.get(pos).get("DESIRED_STOCK_COUNT").toString());
            tv_pro_price.setText(new DecimalFormat("###,###").format(itemList.get(pos).get("PRO_PRICE")) + "원");
            btn_sto_minus.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    int desired_stock_count = Integer.valueOf(tv_desired_stock_count.getText().toString());
                    if(desired_stock_count!=1&&desired_stock_count>1){
                        tv_desired_stock_count.setText(Integer.toString(--desired_stock_count));
                        ContentValues row = new ContentValues();
                        row.put("pay_ea",desired_stock_count);
                        basket.update(row, "pro_code = " + pro_code);
                        String[] columns = {"sum(pro_price * pay_ea)"};
                        Cursor c = basket.selects(columns, null);
                        if(c.getCount()!=0){
                            c.moveToPosition(0);
                            Log.i(TAG, "■■■■■ 총금액 : " + c.getInt(0));
                            c.moveToPosition(0);
                            tv_total_pro_price.setText("총 금액 : " + new DecimalFormat("###,###").format(c.getInt(0)) + "원");
                            tv_total_pro__price.setText(new DecimalFormat("###,###").format(c.getInt(0)) + "원");
                            total_pro_price = c.getInt(0);
                            total_pay = total_pro_price - discountMoney;
                            tv_total_pay.setText(" = " + new DecimalFormat("###,###").format(total_pay) + "원");
                        }
                    } else {
                        Toast.makeText(mContext, "주문수량은 최소 1개 입니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            btn_sto_plus.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    int desired_stock_count = Integer.valueOf(tv_desired_stock_count.getText().toString());
                    tv_desired_stock_count.setText(Integer.toString(++desired_stock_count));
                    ContentValues row = new ContentValues();
                    row.put("pay_ea",desired_stock_count);
                    basket.update(row, "pro_code = " + pro_code);
                    String[] columns = {"sum(pro_price * pay_ea)"};
                    Cursor c = basket.selects(columns, null);
                    if(c.getCount()!=0){
                        c.moveToPosition(0);
                        Log.i(TAG, "■■■■■ 총금액 : " + c.getInt(0));
                        tv_total_pro_price.setText("총 금액 : " + new DecimalFormat("###,###").format(c.getInt(0)) + "원");
                        tv_total_pro__price.setText(new DecimalFormat("###,###").format(c.getInt(0)) + "원");
                        total_pro_price = c.getInt(0);
                        total_pay = total_pro_price - discountMoney;
                        tv_total_pay.setText(" = " + new DecimalFormat("###,###").format(total_pay) + "원");
                    }
                }
            });
            btn_basket_one_delete.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    Snackbar.make(v, "해당 품목을 삭제하시겠습니까?", 10000).setActionTextColor(Color.parseColor("#FF0000"))
                            .setAction("삭제", new View.OnClickListener(){
                                @Override
                                public void onClick(View v) {
                                    basket.delete("pro_code = " + pro_code);
                                    itemList.remove(pos);
                                    String[] columns = {"sum(pro_price * pay_ea)"};
                                    Cursor c = basket.selects(columns, null);
                                    if(c.getCount()!=0){
                                        c.moveToPosition(0);
                                        Log.i(TAG, "■■■■■ 총금액 : " + c.getInt(0));
                                        tv_total_pro_price.setText("총 금액 : " + new DecimalFormat("###,###").format(c.getInt(0)) + "원");
                                        tv_total_pro__price.setText(new DecimalFormat("###,###").format(c.getInt(0)) + "원");
                                        total_pro_price = c.getInt(0);
                                        total_pay = total_pro_price - discountMoney;
                                        tv_total_pay.setText(" = " + new DecimalFormat("###,###").format(total_pay) + "원");
                                    }
                                    ListViewAdapterCustomer.this.notifyDataSetInvalidated();
                                }
                            }).show();
                }
            });
        } else if(mTag.equals(CouponChoice.TAGPay)||mTag.equals(FavCouponActivity.TAG)||mTag.equals(CouponChoice.TAGRepay)){
            // 화면에 표시될 View(Layout이 inflate된)로부터 위젯에 대한 참조 획득
            LinearLayout ll_field = (LinearLayout) convertView.findViewById(R.id.ll_field);
            TextView tv_dis_price = (TextView) convertView.findViewById(R.id.tv_dis_price);
            TextView tv_sto_name = (TextView) convertView.findViewById(R.id.tv_sto_name);
            TextView tv_discount_for = (TextView) convertView.findViewById(R.id.tv_discount_for);
            Log.i(TAG, itemList.get(pos).get("DISCOUNT_PRICE").toString());
            tv_dis_price.setText(new DecimalFormat("###,###").format((int)Math.round((double)itemList.get(pos).get("DISCOUNT_PRICE"))));
            tv_sto_name.setText(itemList.get(pos).get("STO_NAME").toString());
            tv_discount_for.setText(new DecimalFormat("###,###").format((int)Math.round((double)itemList.get(pos).get("DISCOUNT_FOR"))) + "원");

            if(mTag.equals(CouponChoice.TAGPay)) {
                ll_field.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MemberDTO.getInstance().setEve_code((int) Math.round((double) itemList.get(pos).get("EVE_CODE")));
                        discountMoney = (int) Math.round((double) itemList.get(pos).get("DISCOUNT_PRICE"));
                        tv_couponChoiceInfo.setText(itemList.get(pos).get("STO_NAME")
                                + "  "
                                + new DecimalFormat("###,###").format(discountMoney)
                                + "원 할인 쿠폰 적용중");
                        tv_discountMoney.setText(" - " + new DecimalFormat("###,###").format(discountMoney) + "원");
                        total_pay = total_pro_price - discountMoney;
                        tv_total_pay.setText(" = " + new DecimalFormat("###,###").format(total_pay) + "원");
                        mActivity.finish();
                    }
                });
            } else if(mTag.equals(CouponChoice.TAGRepay)) {
                ll_field.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MemberDTO.getInstance().setEve_code((int) Math.round((double) itemList.get(pos).get("EVE_CODE")));
                        rpa_discountMoney = (int) Math.round((double) itemList.get(pos).get("DISCOUNT_PRICE"));
                        tv_rpa_couponChoiceInfo.setText(itemList.get(pos).get("STO_NAME")
                                + "  "
                                + new DecimalFormat("###,###").format(rpa_discountMoney)
                                + "원 할인 쿠폰 적용중");
                        tv_rpa_discountMoney.setText(" - " + new DecimalFormat("###,###").format(rpa_discountMoney) + "원");
                        rpa_total_pay = rpa_total_pro__price - rpa_discountMoney;
                        tv_rpa_total_pay.setText(" = " + new DecimalFormat("###,###").format(rpa_total_pay) + "원");
                        mActivity.finish();

                    }
                });
            }
        } else if(mTag.equals(StoreMapInfoDialog.TAG)||mTag.equals(FavStoreActivity.TAG)) {
            ImageView iv_sto_img = convertView.findViewById(R.id.iv_sto_img);
            TextView tv_sto_name = convertView.findViewById(R.id.tv_sto_name);
            TextView tv_sto_tel = convertView.findViewById(R.id.tv_sto_tel);
            TextView tv_sto_addr = convertView.findViewById(R.id.tv_sto_addr);
            TextView tv_sto_time = convertView.findViewById(R.id.tv_sto_time);
            Button btn_attention_indifference = convertView.findViewById(R.id.btn_attention_indifference);
            Button btn_dialog_finish = convertView.findViewById(R.id.btn_dialog_finish);
            if (itemList.get(pos).get("STO_IMG") != null) {
                Glide.with(mContext).load(itemList.get(pos).get("STO_IMG")).into(iv_sto_img);
            }
            if (itemList.get(pos).get("STO_NAME") != null) {
                tv_sto_name.setText(itemList.get(pos).get("STO_NAME").toString());
            }
            if (itemList.get(pos).get("STO_TEL") != null) {
                tv_sto_tel.setText(itemList.get(pos).get("STO_TEL").toString());
            }
            if (itemList.get(pos).get("STO_ADDR") != null) {
                tv_sto_addr.setText(itemList.get(pos).get("STO_ADDR").toString());
            }
            if (itemList.get(pos).get("STO_TIME") != null) {
                tv_sto_time.setText(itemList.get(pos).get("STO_TIME").toString());
            }
            if (mTag.equals(StoreMapInfoDialog.TAG)) {
                btn_dialog_finish.setText("취소");
                tv_sto_tel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + itemList.get(pos).get("STO_TEL").toString()));
                        mActivity.startActivity(dialIntent);
                    }
                });
                btn_attention_indifference.setText("관심");
                btn_attention_indifference.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, String> favRequestMap = new HashMap<>();
                        favRequestMap.put("sto_name", itemList.get(pos).get("STO_NAME").toString());
                        Log.i(TAG, itemList.get(pos).get("STO_NAME").toString());
                        favRequestMap.put("mem_code", Integer.toString(MemberDTO.getInstance().getMem_code()));
                        VolleyQueueProvider.callbackVolley(new VolleyCallback() {
                            @Override
                            public void onResponse(String response) {
                                Log.i(TAG, "response : " + response);
                                if (response.length()==0) {
                                    Toast.makeText(mContext, "이미 관심매장으로 등록되어 있습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mContext, "관심매장으로 등록되었습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                Log.i(TAG, "error : " + error.toString());
                            }
                        }, "store/set_favsto", favRequestMap);
                    }
                });
                btn_dialog_finish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mActivity.finish();
                    }
                });
            } else if (mTag.equals(FavStoreActivity.TAG)) {
                btn_dialog_finish.setVisibility(View.INVISIBLE);
                btn_attention_indifference.setText("삭제");
                btn_attention_indifference.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mActivity, FavStoreDelActivity.class);
                        intent.putExtra("sto_code", Integer.toString((int) Math.round((double) itemList.get(pos).get("STO_CODE"))));
                        intent.putExtra("mem_code", Integer.toString(MemberDTO.getInstance().getMem_code()));
                        mActivity.startActivityForResult(intent, 1);
                    }
                });
            }
        } else if(mTag.equals(PaymentHistoryActivity.TAG)){
                Log.i(TAG, "★★★★★★★★★★★" + mTag);
                LinearLayout ll_payment_info = convertView.findViewById(R.id.ll_payment_info);
                ImageView iv_ph_pro_img = convertView.findViewById(R.id.iv_ph_pro_img);
                TextView tv_ph_pay_code = convertView.findViewById(R.id.tv_ph_pay_code);
                TextView tv_ph_pay_status = convertView.findViewById(R.id.tv_ph_pay_status);
                TextView tv_ph_pay_name = convertView.findViewById(R.id.tv_ph_pay_name);
                TextView tv_sto_name = convertView.findViewById(R.id.tv_sto_name);
                TextView tv_ph_pay_value = convertView.findViewById(R.id.tv_ph_pay_value);
                TextView tv_ph_pay_date = convertView.findViewById(R.id.tv_ph_pay_date);
                ll_payment_info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, String> requestMap = new HashMap<>();
                        requestMap.put("pay_code", Integer.toString((int)((double)itemList.get(pos).get("PAY_CODE"))));
                        Log.i(TAG, requestMap.get("pay_code"));
                        VolleyQueueProvider.callbackVolley(new VolleyCallback() {
                            @Override
                            public void onResponse(String response) {
                                Log.i(TAG, "response : " + response);
                                if(response.length()==0){
                                    Toast.makeText(mActivity, "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Intent intent = new Intent(mActivity, PaymentInfoActivity.class);
                                intent.putExtra("paymentChoice", new Gson().toJson(itemList.get(pos)));
                                intent.putExtra("paymentInfodata", response);
                                mActivity.startActivity(intent);
                            }
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i(TAG, "error : " + error.toString());
                                error.printStackTrace();
                            }
                        },"payment/detail_pay", requestMap);

                    }
                });
                Glide.with(convertView).load(itemList.get(pos).get("PRO_IMG")).into(iv_ph_pro_img);
                tv_ph_pay_code.setText(Integer.toString((int)((double)itemList.get(pos).get("PAY_CODE"))));
                tv_ph_pay_status.setText(itemList.get(pos).get("PAY_STATUS").toString());
                tv_ph_pay_name.setText(itemList.get(pos).get("PRO_NAME").toString());
                tv_sto_name.setText(itemList.get(pos).get("STO_NAME").toString());
                tv_ph_pay_value.setText(new DecimalFormat("###,###").format((int)((double)itemList.get(pos).get("PAY_VALUE"))));
                tv_ph_pay_date.setText(itemList.get(pos).get("PAY_DATE").toString());
        } else if(mTag.equals(PaymentInfoActivity.TAG)){
            ImageView iv_proinfo_pro_img = convertView.findViewById(R.id.iv_proinfo_pro_img);
            TextView tv_pi_pro_name = convertView.findViewById(R.id.tv_pi_pro_name);
            TextView tv_pi_pro_price = convertView.findViewById(R.id.tv_pi_pro_price);
            TextView tv_pi_pro_ea = convertView.findViewById(R.id.tv_pi_pro_ea);
            Glide.with(convertView).load(itemList.get(pos).get("PRO_IMG")).into(iv_proinfo_pro_img);
            tv_pi_pro_name.setText(itemList.get(pos).get("PRO_NAME").toString());
            tv_pi_pro_price.setText((int)Math.round((double)itemList.get(pos).get("PRO_PRICE")) + "원");
            tv_pi_pro_ea.setText((int)Math.round((double)itemList.get(pos).get("PAY_EA")) + "개");
        } else if(mTag.equals(PaymentActivity.TAG)||mTag.equals(RepayActivity.TAG)){
            ImageView iv_proinfo_pro_img = convertView.findViewById(R.id.iv_proinfo_pro_img);
            TextView tv_pi_pro_name = convertView.findViewById(R.id.tv_pi_pro_name);
            TextView tv_pi_pro_price = convertView.findViewById(R.id.tv_pi_pro_price);
            TextView tv_pi_pro_ea = convertView.findViewById(R.id.tv_pi_pro_ea);
            if(mTag.equals(PaymentActivity.TAG)){
                Glide.with(convertView).load(itemList.get(pos).get("PRO_IMG")).into(iv_proinfo_pro_img);
                tv_pi_pro_name.setText(itemList.get(pos).get("PRO_NAME").toString());
                tv_pi_pro_price.setText(itemList.get(pos).get("PRO_PRICE").toString());
                tv_pi_pro_ea.setText(itemList.get(pos).get("DESIRED_STOCK_COUNT").toString());
            } else if(mTag.equals(RepayActivity.TAG)){
                Log.i(TAG, "position : " + pos);
                Glide.with(convertView).load(itemList.get(pos).get("pro_img")).into(iv_proinfo_pro_img);
                tv_pi_pro_name.setText(itemList.get(pos).get("pro_name").toString());
                tv_pi_pro_price.setText(itemList.get(pos).get("pro_price").toString());
                tv_pi_pro_ea.setText(itemList.get(pos).get("pay_ea").toString());
            }
        }
        return convertView;
    }
}