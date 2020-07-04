package com.example.basket.ui.scan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.basket.R;
import com.example.basket.ui.main.PlazaActivity;
import com.example.basket.util.RequestQ;
import com.example.basket.util.VolleyCallBack;
import com.example.basket.util.VolleyQueueProvider;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ScanFragment extends Fragment implements DecoratedBarcodeView.TorchListener  {
    public static final String TAG = "ScanFragment★★★★★★★★";

    private View root = null;
    private Context mContext = null;
    private Activity mActivity = null;
    private DecoratedBarcodeView barcodeView;

    String pro_barcode = null; //스캔한 상품바코드
    private boolean isFlashOn = false;// 플래시가 켜져 있는지
    private CaptureManager manager;

    private IntentIntegrator scanner;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.e(TAG, "onAttach()");
        this.mContext = context;
        this.mActivity = getActivity();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView 호출됨");
        //QR코드 스캐너 호출
        View root = inflater.inflate(R.layout.fragment_scan, container, false);
        barcodeView = root.findViewById(R.id.barcode_view);

        scanner = new IntentIntegrator(getActivity());
        scanner.setOrientationLocked(true);
        scanner.setPrompt("상품의 바코드를 스캔해주세요.");
        if(mActivity!=null){
            Log.e(TAG, "mActivity : " + getActivity().toString());
            Log.e(TAG, "getActivity : " + getActivity().toString());
        }
        manager = new CaptureManager(mActivity, barcodeView);

        manager.initializeFromIntent(new Intent() , savedInstanceState);
        manager.decode();
        scanner.initiateScan();


        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.e(TAG, "onActivityResult 호출 성공");
        // QR코드/바코드를 스캔한 결과 값을 가져옴
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        // 결과값 출력
        if(result != null){
            if(result.getContents() == null) { //결과값이 없으면
                Log.e(TAG , "result.getContents() == null");
            }
            else {
                //상품 바코드로 DB연동 고고 그리고 다이얼로그 액티비티 띄우기
                pro_barcode = result.getContents();
                Log.e("ScanFrag pro_barcode is" , pro_barcode);
                Map<String, String> pMap = new HashMap<>();
                pMap.put("sto_code", "1");
                pMap.put("pro_barcode", pro_barcode);
                VolleyQueueProvider.callbackVolley(new VolleyCallBack() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "response : " + response);
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }, "product/find_pro", pMap);


            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onTorchOn() {
        Log.e(TAG, "onTorchOn()");
        //btFlash.setText("플래시끄기");
        isFlashOn = true;
    }

    @Override
    public void onTorchOff() {
        Log.e(TAG, "onTorchOff()");
        //btFlash.setText("플래시켜기");
        isFlashOn = false;
    }
    @Override
    public void onResume() {
        Log.e(TAG, "onResume()");
        super.onResume();
        manager.onResume();
    }

    @Override
    public void onPause() {
        Log.e(TAG, "onPause()");
        super.onPause();
        manager.onPause();
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy()");
        super.onDestroy();
        manager.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.e(TAG, "onSaveInstanceState()");
        super.onSaveInstanceState(outState);
        manager.onSaveInstanceState(outState);
    }


}