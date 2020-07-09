package com.example.basket.factory;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.example.basket.R;
import com.example.basket.controller.MemberVerifier;
import com.example.basket.loginFragment.BilFragment;
import com.example.basket.loginFragment.KilFragment;
import com.example.basket.loginFragment.NilFragment;

import java.util.Map;


public class FragmentsFactory {
    public static final String TAG = "FragmentFactory";
    public static MemberVerifier memberVerifier = null;
    private static final MemberVerifier UNDEFINED_FRAGMENT = new MemberVerifier() {
        @Override public void loginProgress(Map<String, String> profileMap) {

        }
        @Override public void logoutProgress(Activity activity) {

        }
    };

    public static final MemberVerifier newInstance(View v) {
        Log.i(TAG, v.toString());
        if (v.getId() == R.id.iv_nilEnter) {
            return memberVerifier = new NilFragment();
        } else if (v.getId() == R.id.iv_kilEnter) {
            return memberVerifier = new KilFragment();
        } else if (v.getId() == R.id.btn_bil) {
            return memberVerifier = new BilFragment();
        } else {
            return memberVerifier = UNDEFINED_FRAGMENT;
        }
    }

    public static final MemberVerifier getInstance(){
        Log.i(TAG, "getInstance();");
        if(memberVerifier!=null){
            Log.i(TAG, memberVerifier.toString());
        }
        return memberVerifier;
    }

    public static void removeInstance() {
        Log.i(TAG, "removeInstance();");
        if(getInstance()!=null){
            Log.i(TAG, getInstance().toString());
            ((Fragment)getInstance()).onDestroy();
            memberVerifier=null;
        }
    }
}
