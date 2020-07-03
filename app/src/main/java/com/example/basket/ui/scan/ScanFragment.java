package com.example.basket.ui.scan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.basket.util.VolleyQueueProvider;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ScanFragment extends Fragment implements  DecoratedBarcodeView.TorchListener {
    public static final String TAG = "ScanFragment";
    private DecoratedBarcodeView barcodeView;
    private CaptureManager manager;
    private boolean isFlashOn = false;// 플래시가 켜져 있는지

    private Context mContext = null;
    private Activity mActivity = null;

    String pro_barcode = null; //스캔한 상품바코드


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        this.mActivity = getActivity();
        //QR코드 스캐너 호출
        //new IntentIntegrator(mActivity).initiateScan();

        /*dddddddddddddddddddddddddddddddddddd
        final ListView listView = getActivity().findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                new String[]{"copy","past","cut","delete","convert","open", "copy","past","cut","delete","convert","open", "copy","past","cut","delete","convert","open"}));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), position+" 번째 값 : " + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
            }

        });
        dddddddddddddddddddddddddddddddddddd*/


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_scan, container, false);
        barcodeView = root.findViewById(R.id.barcode_view);
        if(mActivity!=null){
            Log.i(TAG, "mActivity" + mActivity.toString());
        }
        if(getActivity()!=null){
            Log.i(TAG, "getActivity() : " + getActivity().toString());
        }
        if(savedInstanceState!=null){
            Log.i(TAG, "savedInstance : " + savedInstanceState);

        }
        manager = new CaptureManager(getActivity() , barcodeView);
        manager.initializeFromIntent(mActivity.getIntent() , savedInstanceState);
        manager.decode();

        //btFlash = mActivity.findViewById(R.id.bt_flash);
       /* btFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFlashOn){
                    barcodeView.setTorchOff();
                }else{
                    barcodeView.setTorchOn();
                }
            }
        });*/
        return root;
    }

    /*public void pageMove(View view) {
        webView = mActivity.findViewById(R.id.wv_qr);
        WebSettings webSettings = webView.getSettings();
        //자바스크립트를 사용할 수 있게
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setSupportMultipleWindows(true);
//        webSettings.setUseWideViewPort(true);
//        webView.setWebViewClient(new WebViewClient());
//        webView.setWebChromeClient(new WebChromeClient());
//        webView.loadUrl(qrcontent);
    }*/

  /*  public void scanExit(View view) {
        if(webView.canGoBack()) webView.goBack();
        else finish();
    }*/

    public void jsonArrayRequestTest(){
        String url = "http://192.168.0.37:5050/product/find_pro.do?pro_barcode="+pro_barcode;
        try{
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                for(int i=0; i<response.length(); i++) {
                                    JSONObject sth = response.getJSONObject(i);
                                    String id = sth.getString("member_id");
                                    String name = sth.getString("member_name");

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Errorr", error.toString());
                }
            }
            );

            // Access the RequestQueue through your singleton class.
            VolleyQueueProvider.openQueue(jsonArrayRequest);

        } catch (Exception e) {
            Log.i("Volley",e.toString());
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // QR코드/바코드를 스캔한 결과 값을 가져옴
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        // 결과값 출력
        if(result != null){
            if(result.getContents() == null) { //결과값이 없으면
                Toast.makeText(mContext, "Cancelled", Toast.LENGTH_LONG).show();
            }
            else {
                //qrcontent = result.getContents();
                //Toast.makeText(mContext,  qrcontent ,Toast.LENGTH_LONG).show();

            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onTorchOn() {
        //btFlash.setText("플래시끄기");
        isFlashOn = true;
    }

    @Override
    public void onTorchOff() {
        //btFlash.setText("플래시켜기");
        isFlashOn = false;
    }
    @Override
    public void onResume() {
        super.onResume();
        manager.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        manager.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        manager.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        manager.onSaveInstanceState(outState);
    }
}