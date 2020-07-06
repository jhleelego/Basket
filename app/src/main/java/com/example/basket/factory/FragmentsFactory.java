package com.example.basket.factory;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.basket.R;
import com.example.basket.controller.MemberVerifier;
import com.example.basket.loginFragment.BilFragment;
import com.example.basket.loginFragment.KilFragment;
import com.example.basket.loginFragment.NilFragment;


public class FragmentsFactory {
    public static final String TAG = "FragmentFactory";
    public static MemberVerifier memberVerifier = null;
    private static final MemberVerifier UNDEFINED_FRAGMENT = new MemberVerifier() {
        @Override public void loginProgress() {

        }
        @Override public void logoutProgress(Activity activity) {

        }
    };

    public static final MemberVerifier newInstance(View v, EditText et_inputID, EditText et_inputPW, Context context) {
        Log.i(TAG, v.toString());
        if (v.getId() == R.id.btn_nilEnter) {
            return memberVerifier = new NilFragment();
        } else if (v.getId() == R.id.btn_kilEnter) {
            return memberVerifier = new KilFragment();
        } else if (v.getId() == R.id.btn_bil) {
            if((et_inputID.getText().toString()!=null&&et_inputID.getText().toString().length()>0)
                    &&(et_inputPW.getText().toString()!=null&&et_inputPW.getText().toString().length()>0)){
                return memberVerifier = new BilFragment();
            } else {
                Toast.makeText(context, "아이디 또는 비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                return UNDEFINED_FRAGMENT;
            }
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
        if(memberVerifier!=null){
            Log.i(TAG, memberVerifier.toString());
            ((Fragment)memberVerifier).onStop();
            ((Fragment)memberVerifier).onDestroy();
            ((Fragment)memberVerifier).onDetach();
            memberVerifier=null;
        }
    }
}
