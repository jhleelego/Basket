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
           /* wb_pro_img.setWebViewClient(new WebViewClient());                           // 클릭시 새창 안뜨게
            mWebSettings = wb_pro_img.getSettings();                                    //세부 세팅 등록
            mWebSettings.setJavaScriptEnabled(false);                                   // 웹페이지 자바스클비트 허용 여부
            mWebSettings.setSupportMultipleWindows(false);                              // 새창 띄우기 허용 여부
            mWebSettings.setJavaScriptEnabled(false);                                   // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
            mWebSettings.setSupportZoom(false);                                         // 화면 줌 허용 여부
            mWebSettings.setBuiltInZoomControls(false);                                 // 화면 확대 축소 허용 여부
            mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈 맞추기
            mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);                       // 브라우저 캐시 허용 여부
            mWebSettings.setDomStorageEnabled(false);                                   // 로컬저장소 허용 여부
            mWebSettings.setUseWideViewPort(true);       // wide viewport를 사용하도록 설정, 화면 사이즈 맞추기 허용 여부
            mWebSettings.setLoadWithOverviewMode(true);  // 컨텐츠가 웹뷰보다 클 경우 스크린 크기에 맞게 조정, 메타태그 허용 여부*/

            tv_pro_name = itemView.findViewById(R.id.pro_name) ;
            tv_pro_price = itemView.findViewById(R.id.pro_price) ;
            tv_sto_name = itemView.findViewById(R.id.sto_name) ;
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    ProductInfoAdapter(List<Map<String, Object>> list, Context context) {
        this.mData = list ;
        this.mContext = context;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public ProductInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.product_recyclerview_item, parent, false) ;
        ProductInfoAdapter.ViewHolder vh = new ProductInfoAdapter.ViewHolder(view) ;

        return vh ;
    }
    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
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
