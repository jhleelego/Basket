package com.example.basket.ui.search.productSearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.basket.R;

import java.util.List;
import java.util.Map;

public class ProductInfoAdapter extends RecyclerView.Adapter<ProductInfoAdapter.ViewHolder> {

    private List<Map<String, Object>> mData = null;
    private Context mContext = null;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_pro_img = null;
        TextView tv_pro_name = null;
        TextView tv_pro_price = null;
        TextView tv_sto_name = null;
        WebSettings mWebSettings = null;


        ViewHolder(View itemView) {
            super(itemView) ;
            // 뷰 객체에 대한 참조. (hold strong reference)
            iv_pro_img = itemView.findViewById(R.id.pro_img) ;
            tv_pro_name = itemView.findViewById(R.id.pro_name) ;
            tv_pro_price = itemView.findViewById(R.id.pro_price) ;
            tv_sto_name = itemView.findViewById(R.id.sto_name) ;
        }
    }

    ProductInfoAdapter(List<Map<String, Object>> list, Context context) {
        this.mData = list ;
        this.mContext = context;
    }

    @Override
    public ProductInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.product_recyclerview_item, parent, false) ;
        ProductInfoAdapter.ViewHolder vh = new ProductInfoAdapter.ViewHolder(view) ;

        return vh;
    }
    @Override
    public void onBindViewHolder(ProductInfoAdapter.ViewHolder holder, int position) {
        //웹뷰설정
            for(Map.Entry resultProMap : mData.get(position).entrySet()){
                if(resultProMap.getKey().equals("PRO_IMG")){Glide.with(mContext).load(resultProMap.getValue().toString()).into(holder.iv_pro_img);}
                if(resultProMap.getKey().equals("PRO_NAME")){holder.tv_pro_name.setText(resultProMap.getValue().toString());}
                if(resultProMap.getKey().equals("PRO_PRICE")){holder.tv_pro_price.setText(resultProMap.getValue().toString());}
                if(resultProMap.getKey().equals("STO_CODE")){holder.tv_pro_price.setText(resultProMap.getValue().toString());}
        }
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}
