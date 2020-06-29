package com.example.basket.factory;

import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.basket.R;
import com.example.basket.controller.MemberVerifier;
import com.example.basket.loginFragment.BilFragment;
import com.example.basket.loginFragment.KilFragment;
import com.example.basket.loginFragment.NilFragment;


public class FragmentsFactory {
    public static final String TAG = "FragmentFactory";
    private static final MemberVerifier UNDEFINED_FRAGMENT = new MemberVerifier() {
        @Override
        public void loginProgress() {
        }
        @Override
        public void logoutProgress() {
        }
    };

    public static final MemberVerifier getInstance(View v) {
        Log.i(TAG, v.toString());
        if (v.getId() == R.id.btn_bil) {
            return BilFragment.getInstance();
        } else if (v.getId() == R.id.btn_nilEnter) {
            return NilFragment.getInstance();
        } else if (v.getId() == R.id.btn_kilEnter) {
            return KilFragment.getInstance();
        } else {
            return UNDEFINED_FRAGMENT;
        }
    }
}
