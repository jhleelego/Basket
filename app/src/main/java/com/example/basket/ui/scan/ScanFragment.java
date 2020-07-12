package com.example.basket.ui.scan;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.basket.R;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class ScanFragment extends Fragment {
    public static final String TAG = "ScanFragment";

    private View root = null;
    private Context mContext = null;
    private Activity mActivity = null;
    private DecoratedBarcodeView barcodeView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.e(TAG, "onAttach()");
        this.mContext = context;
        this.mActivity = getActivity();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView 호출됨");
        View root = inflater.inflate(R.layout.fragment_scan, container, false);
        barcodeView = root.findViewById(R.id.barcode_view);
        //new FragmentIntentIntegrator(ScanFragment.this).initiateScan();// 이게 전체화면 바코드 스캐너 - result 불러올수있음
        if(mActivity!=null){
            Log.e(TAG, "mActivity : " + getActivity().toString());
            Log.e(TAG, "getActivity : " + getActivity().toString());
        }
        scanCustomScanner(root);
        return root;
    }

    public void scanCustomScanner(View view) {
        Log.e(TAG, "scanCustomScanner(View view) 호출 성공");

    }
}