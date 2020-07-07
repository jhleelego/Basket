package com.example.basket.ui.scan;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.example.basket.R;
import com.example.basket.util.VolleyCallback;
import com.example.basket.util.VolleyQueueProvider;
import com.google.gson.Gson;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomScannerActivity extends AppCompatActivity {
    private final String TAG = "CustomScannerActivity";
    private CaptureManager manager;
    private DecoratedBarcodeView barcodeView;
    String lastText = null;
    String pro_barcode = null;

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if(result.getText() == null || result.getText().equals(lastText)) {
                // Prevent duplicate scans
                Log.e(TAG, "// Prevent duplicate scans");
                return;
            }

            pro_barcode = result.getText();
            barcodeView.setStatusText(result.getText());
            Log.e("ScanFrag pro_barcode is", pro_barcode);
            Map<String, String> pMap = new HashMap<>();
            pMap.put("sto_code", "1");
            pMap.put("pro_barcode", pro_barcode);
            VolleyQueueProvider.callbackVolley(new VolleyCallback() {
                @Override
                public void onResponse(String response) {
                    Log.e(TAG, "response : " + response);
                    Map<String, Object> rMap = new Gson().fromJson(response, Map.class);
                    //데이터 담아서 팝업(액티비티) 호출
                 /*   for (int i = 0; i < rMap.size(); i++) {
                        productOneDTO = new ProductOneDTO();
                        productOneDTO.setPRO_CODE((String) rMap.get("PRO_CODE"));
                        productOneDTO.setPRO_IMG((String) rMap.get("PRO_IMG"));
                        productOneDTO.setPRO_STOCK_EA((String) rMap.get("PRO_STOCK_EA"));
                        productOneDTO.setPRO_NAME((String) rMap.get("PRO_NAME"));
                        productOneDTO.setPRO_PRICE((String) rMap.get("PRO_PRICE"));
                        Intent intent = new Intent(getApplicationContext(), ProInfoActivity.class);
                        intent.putExtra("data", productOneDTO);
                        startActivityForResult(intent, 1);
                    }*/
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "error.getStackTrace() : " + error.getStackTrace().toString());
                    Log.e(TAG, "error.getMessage() : " + error.getMessage());
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG);
                }
            }, "product/find_pro", pMap);
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_scanner);
        SlidingUpPanelLayout layout = (SlidingUpPanelLayout)findViewById(R.id.sliding_p);
        layout.setAnchorPoint(0.3f);

        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_view);
        manager = new CaptureManager(this, barcodeView);
        manager.initializeFromIntent(getIntent(), savedInstanceState);
        manager.decode();
        barcodeView.decodeContinuous(callback);

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