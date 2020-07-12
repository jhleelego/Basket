package com.example.basket.ui.basket;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.basket.R;
import com.example.basket.vo.EventCouponOneDTO;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.example.basket.ui.basket.BasketFragment.discountMoney;
import static com.example.basket.ui.basket.BasketFragment.total_pay;
import static com.example.basket.ui.basket.BasketFragment.total_pro_price;
import static com.example.basket.ui.basket.BasketFragment.tv_couponChoiceInfo;
import static com.example.basket.ui.basket.BasketFragment.tv_discountMoney;
import static com.example.basket.ui.basket.BasketFragment.tv_total_pay;

public class ListViewEventCouponAdapter extends ArrayAdapter implements View.OnClickListener  {
    public static final String TAG = "LVEventCouponAdapter";
    @Override
    public void onClick(View v) {

    }

    public interface ListBtnClickListener {
        void onListBtnClick(int position) ;
    }

    ArrayList<EventCouponOneDTO> itemList = null;
    private Activity mActivity = null;

    // 생성자로부터 전달된 resource id 값을 저장.
    int resourceId ;
    // 생성자로부터 전달된 ListBtnClickListener  저장.
    private ListBtnClickListener listBtnClickListener ;


    // ListViewBtnAdapter 생성자. 마지막에 ListBtnClickListener 추가.
    //ListViewBasketAdapter(Context context, int resource, ArrayList<ListViewBasketItem> list, ListBtnClickListener clickListener) {
    ListViewEventCouponAdapter(Activity activity, Context context, int resource, ArrayList<EventCouponOneDTO> list) {
        super(context, resource, list) ;
        this.mActivity = activity;
        this.itemList = list;

        // resource id 값 복사. (super로 전달된 resource를 참조할 방법이 없음.)
        this.resourceId = resource;
      //  this.listBtnClickListener = clickListener;
    }

    // 새롭게 만든 Layout을 위한 View를 생성하는 코드
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position ;
        final Context context = parent.getContext();
        Log.i(TAG, "■■■■pos : " + pos);

        // 생성자로부터 저장된 resourceId(listview_btn_item)에 해당하는 Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resourceId, parent, false);
        }



        // 화면에 표시될 View(Layout이 inflate된)로부터 위젯에 대한 참조 획득
        LinearLayout ll_field = (LinearLayout) convertView.findViewById(R.id.ll_field);
        TextView tv_dis_price = (TextView) convertView.findViewById(R.id.tv_dis_price);
        TextView tv_sto_name = (TextView) convertView.findViewById(R.id.tv_sto_name);
        TextView tv_discount_for = (TextView) convertView.findViewById(R.id.tv_discount_for);

        tv_dis_price.setText(Integer.toString(itemList.get(pos).getDis_price()));
        tv_sto_name.setText(itemList.get(pos).getSto_name());
        tv_discount_for.setText(Integer.toString(itemList.get(pos).getDiscount_for()) + "원 이상");

        ll_field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_dis_price.setText(Integer.toString(itemList.get(pos).getDis_price()));
                tv_couponChoiceInfo.setText(itemList.get(pos).getSto_name()
                        + "  "
                        + itemList.get(pos).getDis_price()
                        + "원 할인 쿠폰 적용중");

                discountMoney = itemList.get(pos).getDis_price();
                tv_discountMoney.setText(" - " + itemList.get(pos).getDis_price() + "원");
                total_pay = total_pro_price - discountMoney;
                tv_total_pay.setText(" = " + new DecimalFormat("###,###").format(total_pay) + "원");


                mActivity.finish();
            }
        });
        return convertView;
    }
}