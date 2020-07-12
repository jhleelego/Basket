package com.example.basket.ui.basket;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.basket.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import static com.example.basket.ui.basket.BasketFragment.discountMoney;
import static com.example.basket.ui.basket.BasketFragment.total_pay;
import static com.example.basket.ui.basket.BasketFragment.total_pro_price;
import static com.example.basket.ui.basket.BasketFragment.tv_total_pay;
import static com.example.basket.ui.basket.BasketFragment.tv_total_pro_price;
import static com.example.basket.util.SqliteTable.basket;


public class ListViewBasketAdapter extends ArrayAdapter implements View.OnClickListener  {
    public static final String TAG = "ListViewBasketAdapter";
    public interface ListBtnClickListener {
        void onListBtnClick(int position) ;
    }
    ArrayList<ListViewBasketItem> itemList = null;

    // 생성자로부터 전달된 resource id 값을 저장.
    int resourceId ;
    // 생성자로부터 전달된 ListBtnClickListener  저장.
    private ListBtnClickListener listBtnClickListener ;
    private Context mContext = null;


    // ListViewBtnAdapter 생성자. 마지막에 ListBtnClickListener 추가.
    //ListViewBasketAdapter(Context context, int resource, ArrayList<ListViewBasketItem> list, ListBtnClickListener clickListener) {
    ListViewBasketAdapter(Context context, int resource, ArrayList<ListViewBasketItem> list) {
        super(context, resource, list) ;
        this.mContext = context;
        this.itemList = list;

        // resource id 값 복사. (super로 전달된 resource를 참조할 방법이 없음.)
        this.resourceId = resource;
      //  this.listBtnClickListener = clickListener;
    }

    // 새롭게 만든 Layout을 위한 View를 생성하는 코드
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(TAG, "position : " + position);
        final int pos = position ;
        final Context context = parent.getContext();
        final int pro_code = itemList.get(pos).getPro_code();

        // 생성자로부터 저장된 resourceId(listview_btn_item)에 해당하는 Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resourceId/*R.layout.listview_btn_item*/, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)로부터 위젯에 대한 참조 획득
        ImageView iv_pro_img = (ImageView) convertView.findViewById(R.id.iv_pro_img);
        TextView tv_pro_name = (TextView) convertView.findViewById(R.id.tv_pro_name);
        Button btn_sto_minus = (Button) convertView.findViewById(R.id.btn_sto_minus);
        TextView tv_desired_stock_count = (TextView) convertView.findViewById(R.id.tv_desired_stock_count_adapted);
        TextView tv_pro_price = (TextView) convertView.findViewById(R.id.tv_pro_price);
        Button btn_sto_plus = (Button) convertView.findViewById(R.id.btn_sto_plus);
        Button btn_basket_one_delete = (Button) convertView.findViewById(R.id.btn_basket_one_delete);
        Glide.with(convertView).load(itemList.get(pos).getPro_img()).into(iv_pro_img);
        tv_pro_name.setText(itemList.get(pos).getPro_name());
        tv_desired_stock_count.setText(Integer.toString(itemList.get(pos).getDesired_stock_count()));
        tv_pro_price.setText(itemList.get(pos).getPro_price() + "원");


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
                    tv_total_pro_price.setText("총 금액 : " + c.getInt(0) + " 원");
                    total_pro_price = c.getInt(0);
                    tv_total_pay.setText(" = " + (total_pro_price - discountMoney) + "원");
                    total_pay = total_pro_price - discountMoney;
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
                    c.moveToPosition(0);
                    tv_total_pro_price.setText("총 금액 : " + c.getInt(0) + " 원");
                    total_pro_price = c.getInt(0);
                    tv_total_pay.setText(" = " + (total_pro_price - discountMoney) + "원");
                    total_pay = total_pro_price - discountMoney;
                }
            }
        });
        btn_basket_one_delete.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Snackbar.make(v, "해당 품목을 삭제하시겠습니까?", 10000).setActionTextColor(Color.parseColor("#FF0000"))
                        .setAction("삭제", new View.OnClickListener(){
                            @Override
                            public void onClick(View v)
                            {
                                basket.delete("pro_code = " + pro_code);
                                itemList.remove(pos);
                                String[] columns = {"sum(pro_price * pay_ea)"};
                                Cursor c = basket.selects(columns, null);
                                if(c.getCount()!=0){
                                    c.moveToPosition(0);
                                    Log.i(TAG, "■■■■■ 총금액 : " + c.getInt(0));
                                    tv_total_pro_price.setText("총 금액 : " + c.getInt(0) + " 원");
                                    total_pro_price = c.getInt(0);
                                    tv_total_pay.setText(" = " + (total_pro_price - discountMoney) + "원");
                                    total_pay = total_pro_price - discountMoney;
                                }
                                ListViewBasketAdapter.this.notifyDataSetInvalidated();
                            }
                        }).show();
            }
        });


        return convertView;
    }

    // button2가 눌려졌을 때 실행되는 onClick함수.
    public void onClick(View v) {
        // ListBtnClickListener(MainActivity)의 onListBtnClick() 함수 호출.
        if (this.listBtnClickListener != null) {
            this.listBtnClickListener.onListBtnClick((int)v.getTag()) ;
        }
    }
}