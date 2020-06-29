package com.example.basket.factory;

import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.basket.R;
import com.example.basket.loginFragment.BilFragment;
import com.example.basket.loginFragment.KilFragment;
import com.example.basket.loginFragment.NilFragment;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FragmentsFactory {
    private static final Fragment UNDEFINED_FRAGMENT = new Fragment() {
        public static final String TAG = "FragmentFactory";
    };
    public static final Fragment getInstance(FragmentTransaction fragmentTransaction, View v) {
        Log.i(TAG, v.toString());
        switch(v.getId()) {
            case R.id.btn_bil : {
                fragmentTransaction.add(BilFragment.getInstance(), BilFragment.getInstance().TAG);
                fragmentTransaction.commitAllowingStateLoss();
                return BilFragment.getInstance();
            }
            case R.id.btn_nilEnter : {
                fragmentTransaction.add(NilFragment.getInstance(), NilFragment.getInstance().TAG);
                fragmentTransaction.commitAllowingStateLoss();
                return NilFragment.getInstance();
            }
            case R.id.btn_kil : {
                fragmentTransaction.add(KilFragment.getInstance(), KilFragment.getInstance().TAG);
                fragmentTransaction.commitAllowingStateLoss();
                return KilFragment.getInstance();
            }
            default :
                return UNDEFINED_FRAGMENT;
        }
    }
}
