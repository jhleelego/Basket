package com.example.basket.ui.basket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.basket.R;

import java.util.ArrayList;

public class ListViewBasketAdapter extends ArrayAdapter implements View.OnClickListener  {
    public interface ListBtnClickListener {
        void onListBtnClick(int position) ;
    }
    ArrayList<ListViewBasketItem> itemList = null;

    // 생성자로부터 전달된 resource id 값을 저장.
    int resourceId ;
    // 생성자로부터 전달된 ListBtnClickListener  저장.
    private ListBtnClickListener listBtnClickListener ;


    // ListViewBtnAdapter 생성자. 마지막에 ListBtnClickListener 추가.
    //ListViewBasketAdapter(Context context, int resource, ArrayList<ListViewBasketItem> list, ListBtnClickListener clickListener) {
    ListViewBasketAdapter(Context context, int resource, ArrayList<ListViewBasketItem> list) {
        super(context, resource, list) ;
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

        // 생성자로부터 저장된 resourceId(listview_btn_item)에 해당하는 Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resourceId/*R.layout.listview_btn_item*/, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)로부터 위젯에 대한 참조 획득
        ImageView iv_pro_img = (ImageView) convertView.findViewById(R.id.iv_pro_img);
        TextView tv_pro_name = (TextView) convertView.findViewById(R.id.tv_pro_name);
        //Button btn_sto_minus = (Button) convertView.findViewById(R.id.btn_sto_minus);
        TextView tv_desired_stock_count = (TextView) convertView.findViewById(R.id.tv_desired_stock_count);
        TextView tv_pro_price = (TextView) convertView.findViewById(R.id.tv_pro_price);
        //Button btn_sto_plus = (Button) convertView.findViewById(R.id.btn_sto_plus);
        //Button btn_basket_one_delete = (Button) convertView.findViewById(R.id.btn_basket_one_delete);


        Glide.with(convertView).load(itemList.get(position).getPro_img()).into(iv_pro_img);
        tv_pro_name.setText(itemList.get(position).getPro_name());
        tv_desired_stock_count.setText(itemList.get(position).getDesired_stock_count());
        tv_pro_price.setText(itemList.get(position).getPro_price() + "원");

        /*ImageView.*/


        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        //final ListViewBtnItem listViewItem = basketSQLite.sele getItem(position);

        // 아이템 내 각 위젯에 데이터 반영
        /*iconImageView.setImageDrawable(listViewItem.getIcon());
        textTextView.setText(listViewItem.getText());*/

        // button1 클릭 시 TextView(textView1)의 내용 변경.

        // button2의 TAG에 position값 지정. Adapter를 click listener로 지정.

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