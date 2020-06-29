package com.example.basket.loginFragment;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.basket.controller.MemberVerifier;
import com.kakao.auth.AuthType;
import com.kakao.auth.Session;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.util.HashMap;
import java.util.Map;

public class KilFragment extends Fragment implements MemberVerifier {

    public static final String TAG = "Kil";

    private Button btn_custom_login;
    private Button btn_custom_login_out;
    private SessionCallback sessionCallback = new SessionCallback();
    Session session;
    private Context mContext;

    public static KilFragment getInstance() {
        return KilFragment.LazyHolder.instance;
    }

    private static class LazyHolder {
        private static final KilFragment instance = new KilFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        Log.i(TAG, "onAttach()");
        super.onAttach(context);
        this.mContext = context;
        session = Session.getCurrentSession();
        session.addCallback(sessionCallback);
        Map<String, String> pMap = new HashMap<>();
        session.open(AuthType.KAKAO_LOGIN_ALL, KilFragment.this);
        //loginProgress(pMap);
        btn_custom_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.open(AuthType.KAKAO_LOGIN_ALL, KilFragment.this);
            }
        });

        btn_custom_login_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManagement.getInstance()
                        .requestLogout(new LogoutResponseCallback() {
                            @Override
                            public void onCompleteLogout() {
                                Toast.makeText(mContext, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
        // 세션 콜백 삭제
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.i(TAG, "onActivityResult()");
        // 카카오톡|스토리 간편로그인 실행 결과를 받아서 SDK로 전달
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void loginProgress(Map<String, Object> profileMap) {
        Log.i(TAG, "loginProgress()");

    }

    @Override
    public void logoutProgress() {
        Log.i(TAG, "logoutProgress()");
    }

}