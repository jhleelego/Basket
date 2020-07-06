package com.example.basket.ui.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.basket.R;
import com.example.basket.factory.FragmentsFactory;
import com.example.basket.ui.LoginActivity;
import com.example.basket.ui.search.EventPopupActivity;
import com.example.basket.vo.MemberDTO;
import com.example.basket.ui.scan.ProInfoActivity;
import com.example.basket.ui.scan.ScanFragment;
import com.example.basket.util.VolleyCallback;
import com.example.basket.util.VolleyQueueProvider;
import com.example.basket.vo.ProductOneDTO;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.example.basket.ui.main.LoginActivity.fragmentTransaction;

public class PlazaActivity extends AppCompatActivity {
    public static final String TAG = "PlazaActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plaza);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        if(fragmentTransaction!=null){
            Log.i(TAG, fragmentTransaction.toString());
        }

    }

    public void btn_logoutClick(View v) {
        logOutActive();
    }

    public void LoginEnterActivity() {
        startActivity(new Intent(PlazaActivity.this, LoginActivity.class));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        logOutActive();
    }

    public void logOutActive(){
        fragmentTransaction.remove((Fragment)FragmentsFactory.getInstance());
        FragmentsFactory.getInstance().logoutProgress(this);
        FragmentsFactory.removeInstance();
    }

    ProductOneDTO productOneDTO = null;
/*
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.e(TAG, "onActivityResult 호출 성공");
        // QR코드/바코드를 스캔한 결과 값을 가져옴
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        // 결과값 출력
        if (result != null) {
            if (result.getContents() == null) { //결과값이 없으면
                Log.e(TAG, "result.getContents() == null");
            } else {
                //상품 바코드로 DB연동 고고 그리고 다이얼로그 액티비티 띄우기
                pro_barcode = result.getContents();
                Log.e("ScanFrag pro_barcode is", pro_barcode);
                Map<String, String> pMap = new HashMap<>();
                pMap.put("sto_code", "1");
                pMap.put("pro_barcode", pro_barcode);
                VolleyQueueProvider.callbackVolley(new VolleyCallBack() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "response : " + response);
                        Map<String, Object> rMap = new Gson().fromJson(response, Map.class);
                        //데이터 담아서 팝업(액티비티) 호출
                        for (int i = 0; i < rMap.size(); i++) {
                            productOneDTO = new ProductOneDTO();
                            productOneDTO.setPRO_CODE((String) rMap.get("PRO_CODE"));
                            productOneDTO.setPRO_IMG((String) rMap.get("PRO_IMG"));
                            productOneDTO.setPRO_STOCK_EA((String) rMap.get("PRO_STOCK_EA"));
                            productOneDTO.setPRO_NAME((String) rMap.get("PRO_NAME"));
                            productOneDTO.setPRO_PRICE((String) rMap.get("PRO_PRICE"));
                            Intent intent = new Intent(getApplicationContext(), ProInfoActivity.class);
                            intent.putExtra("data", productOneDTO);
                            startActivityForResult(intent, 1);
                        }

                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }, "product/find_pro", pMap);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
    */

}