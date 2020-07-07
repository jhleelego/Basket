package com.example.basket.ui.search.productSearch;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.example.basket.R;
import com.example.basket.util.VolleyCallback;
import com.example.basket.util.VolleyQueueProvider;
import com.example.basket.vo.MemberDTO;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductSearchActivity extends AppCompatActivity {
    public static final String TAG = "ProductSearchActivity";
    SearchView sv_product = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_search);
        sv_product = findViewById(R.id.sv_product);
        sv_product.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                sv_product.setIconified(false);
                sv_product.setQueryHint("검색어를 입력하세요.");
            }
        });

        sv_product.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Map<String, String> pMap = new HashMap<>();
                pMap.put("p_word", query);
                pMap.put("sto_code", MemberDTO.getInstance().getSto_code());
                VolleyQueueProvider.initRequestQueue(ProductSearchActivity.this);
                searchProduct(pMap);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i(TAG, "query of onQueryTextChange : " + newText);
                return false;
            }
        });


    }

    private void searchProduct(Map<String, String> pMap) {
        Log.i(TAG, "searchProduct()");
        VolleyQueueProvider.callbackVolley(new VolleyCallback() {
            @Override
            public void onResponse(String response) { //resonse : JSONArray
                Log.i(TAG, "response : " + response);
                List<Map<String, Object>> pList = new Gson().fromJson(response, List.class);
                // 리사이클러뷰에 표시할 데이터 리스트 생성.
                // 리사이클러뷰에 LinearLayoutManager 객체 지정.
                RecyclerView recyclerView = findViewById(R.id.recycler1) ;
                recyclerView.setLayoutManager(new LinearLayoutManager(ProductSearchActivity.this)) ;

                // 리사이클러뷰에 ProductInfoAdapter 객체 지정.
                ProductInfoAdapter adapter = new ProductInfoAdapter(pList, ProductSearchActivity.this) ;
                recyclerView.setAdapter(adapter) ;
            }
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "error : " + error.toString());
            }
        }, "product/search_pro", pMap);
    }
}