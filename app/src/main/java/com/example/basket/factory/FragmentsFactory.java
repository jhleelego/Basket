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

import static com.example.basket.ui.LoginActivity.memberVerifier;


public class FragmentsFactory {
    public static final String TAG = "FragmentFactory";
    private static final MemberVerifier UNDEFINED_FRAGMENT = new MemberVerifier() {
        @Override public void loginProgress() {}
        @Override public void logoutProgress(Activity activity) {}
    };

    public static final MemberVerifier newInstance(View v) {
        Log.i(TAG, v.toString());
        if (v.getId() == R.id.btn_bil) {
            return new BilFragment();
        } else if (v.getId() == R.id.btn_nilEnter) {
            return new NilFragment();
        } else if (v.getId() == R.id.btn_kilEnter) {
            return new KilFragment();
        } else {
            return UNDEFINED_FRAGMENT;
        }
    }

  /*  public static final MemberVerifier getInstance(){
        Log.i(TAG, "getInstance();");
        return memberVerifier;
    }*/

    public static void removeInstance() {
        Log.i(TAG, "removeInstance();");
        ((Fragment)memberVerifier).onStop();
        ((Fragment)memberVerifier).onDestroy();
        ((Fragment)memberVerifier).onDetach();
        memberVerifier = null;
    }
}
