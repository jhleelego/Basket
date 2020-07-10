package com.example.basket.ui.scan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.example.basket.R;
import com.example.basket.util.VolleyCallback;
import com.example.basket.util.VolleyQueueProvider;
import com.google.zxing.ResultPoint;
import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomScannerActivity extends AppCompatActivity {
    private final String TAG = "CustomScannerActivity";
    private CaptureManager manager;
    private DecoratedBarcodeView barcodeView;
    String now_barcode = null;
    String last_barcode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_scanner);

        BarcodeCallback callback = new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                if (result.getText() == null || result.getText().equals(last_barcode)) {
                    // Prevent duplicate scans
                    Log.e(TAG, "// Prevent duplicate scans");
                    return;
                }
                now_barcode = result.getText();
                Log.i(TAG, "★★★★★★★★★★★★★★★★★★★pro_barcode : " + now_barcode);
                barcodeView.setStatusText(result.getText());
                Log.e("ScanFrag pro_barcode is", now_barcode);
                Map<String, String> pMap = new HashMap<>();
                pMap.put("sto_code", "1");
                pMap.put("pro_barcode", now_barcode);
                VolleyQueueProvider.initRequestQueue(CustomScannerActivity.this);
                VolleyQueueProvider.callbackVolley(new VolleyCallback() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "response : " + response);
                        //데이터 담아서 팝업(액티비티) 호출
                        Intent intent = new Intent(CustomScannerActivity.this, ProInfoActivity.class);
                        intent.putExtra("data", response);
                        startActivityForResult(intent, 1);
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

        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_view);
        manager = new CaptureManager(this, barcodeView);
        manager.initializeFromIntent(getIntent(), savedInstanceState);
        manager.decode();
        barcodeView.decodeContinuous(callback);
        new IntentIntegrator(this).setOrientationLocked(false).setCaptureActivity(CustomScannerActivity.class).initiateScan();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //데이터 받기
                /*String result = data.getStringExtra("result");
                txtResult.setText(result);*/
            }
        }
    }

}