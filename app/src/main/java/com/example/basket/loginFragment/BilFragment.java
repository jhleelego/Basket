package com.example.basket.loginFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.basket.ui.main.PlazaActivity;
import com.example.basket.controller.MemberVerifier;

public class BilFragment extends Fragment implements MemberVerifier {
    public static final String TAG = "Bil";

    private static Context mContext;


    public static BilFragment getInstance() {
        return BilFragment.LazyHolder.instance;
    }
    private static class LazyHolder {
        private static final BilFragment instance = new BilFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        Log.i(TAG, "onAttach()");
        super.onAttach(context);
        this.mContext = (Context)context;
        Log.i(TAG, "onAttach() mContext : " + mContext.toString());
        Log.i(TAG, "onAttach() mActivity : " + getActivity().toString());
    }


    //자금활동//

    @Override
    public void loginProgress() {
        Log.i(TAG, "loginProgress()");
        Log.i(TAG, "mContext : " + this.mContext);
        Intent intent = new Intent(this.mContext, PlazaActivity.class);



        /*

        디비갖다오기


         */






        startActivity(intent);

    }

    @Override
    public void logoutProgress(Activity activity) {
        Log.i(TAG, "logoutProgress()");

    }



}
