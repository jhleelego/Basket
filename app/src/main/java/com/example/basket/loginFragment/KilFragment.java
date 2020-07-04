package com.example.basket.loginFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.basket.R;
import com.example.basket.controller.MemberVerifier;
import com.example.basket.factory.FragmentsFactory;
import com.example.basket.logical.HashUtil;
import com.example.basket.ui.LoginActivity;
import com.example.basket.ui.main.PlazaActivity;
import com.example.basket.ui.main.PlazaActivity;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.OptionalBoolean;
import com.kakao.util.exception.KakaoException;

import java.util.HashMap;
import java.util.Map;

public class KilFragment extends Fragment implements MemberVerifier {
    public static final String TAG = "Kil";
    private SessionCallback sessionCallback = new SessionCallback();
    Session session;
    private Context mContext;
    private Activity mActivity;

    public static KilFragment getInstance() {
        return KilFragment.LazyHolder.instance;
    }

    private static class LazyHolder {
        private static final KilFragment instance = new KilFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach()");
        this.mContext = context;
        this.mActivity = getActivity();
        session = Session.getCurrentSession();
        if(!session.isOpened()){
            session.close();
        }
        session.addCallback(sessionCallback);
        Log.i(TAG, "session.isOpened() : " + session.isOpened());
        if(mActivity!=null){
            Log.i(TAG, "mActivity!=null : " + mActivity.toString());
        }
        session.open(AuthType.KAKAO_LOGIN_ALL, KilFragment.this);
        if(getActivity()!=null){
            Log.i(TAG, "getActivity()!=null : " + getActivity().toString());
        }
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
    public void loginProgress() {
        Log.i(TAG, "loginProgress()");
    }

    @Override
    public void logoutProgress(Activity activity) {
        Log.i(TAG, "logoutProgress()");
        UserManagement.getInstance()
                .requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Toast.makeText(mContext, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
        ((PlazaActivity)activity).LoginEnterActivity();
    }


    public class SessionCallback implements ISessionCallback {

        public static final String TAG = "SessionCallback";

        // 로그인에 성공한 상태
        @Override
        public void onSessionOpened() {
            requestMe();
        }
        // 로그인에 실패한 상태
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.e("SessionCallback :: ", "onSessionOpenFailed : " + exception.getMessage());
        }
        // 사용자 정보 요청
        public void requestMe() {
            UserManagement.getInstance()
                    .me(new MeV2ResponseCallback() {
                        @Override
                        public void onSessionClosed(ErrorResult errorResult) {
                            Log.e("KAKAO_API", "세션이 닫혀 있음: " + errorResult);
                        }

                        @Override
                        public void onFailure(ErrorResult errorResult) {
                            Log.e("KAKAO_API", "사용자 정보 요청 실패: " + errorResult);
                        }

                        @Override
                        public void onSuccess(MeV2Response result) {
                            Log.i("KAKAO_API", "사용자 아이디: " + result.getId());
                            UserAccount kakaoAccount = result.getKakaoAccount();
                            if (kakaoAccount != null) {
                                // 이메일
                                String email = kakaoAccount.getEmail();
                                if (email != null) {
                                    //Log.i("KAKAO_API", "email: " + email);
                                } else if (kakaoAccount.emailNeedsAgreement() == OptionalBoolean.TRUE) {
                                    // 동의 요청 후 이메일 획득 가능
                                    // 단, 선택 동의로 설정되어 있다면 서비스 이용 시나리오 상에서 반드시 필요한 경우에만 요청해야 합니다.
                                } else {
                                    // 이메일 획득 불가
                                }
                                Map<String, Object> pMap = new HashMap<>();
                                if(kakaoAccount.getEmail()!=null){
                                    Log.i(TAG, "kakaoAccount.getEmail() : " + kakaoAccount.getEmail());
                                    pMap.put("email",kakaoAccount.getEmail());
                                }
                                if(kakaoAccount.getProfile()!=null){
                                    if(kakaoAccount.getProfile().getNickname()!=null){
                                        Log.i(TAG,  "kakaoAccount.getProfile().getNickname() : " + kakaoAccount.getProfile().getNickname());
                                        pMap.put("name", kakaoAccount.getProfile().getNickname());
                                    }
                                }
                                if(kakaoAccount.getAgeRange()!=null){
                                    Log.i(TAG, "kakaoAccount.getAgeRange().toString() : " + kakaoAccount.getAgeRange().toString());
                                    pMap.put("age",kakaoAccount.getAgeRange());
                                }
                                if(kakaoAccount.getGender()!=null){
                                    Log.i(TAG, "kakaoAccount.getGender().toString() : " + kakaoAccount.getGender().toString());
                                    pMap.put("gender",kakaoAccount.getGender());
                                }
                                if(kakaoAccount.getBirthday()!=null){
                                    Log.i(TAG, "kakaoAccount.getBirthday() : " + kakaoAccount.getBirthday());
                                    pMap.put("birthday",kakaoAccount.getBirthday());
                                }
                                HashUtil.mapToDTOBinder(pMap, KilFragment.TAG);
                                // 프로필
                                Profile profile = kakaoAccount.getProfile();
                                if (profile != null) {
                                /*Log.d("KAKAO_API", "nickname: " + profile.getNickname());
                                Log.d("KAKAO_API", "profile image: " + profile.getProfileImageUrl());
                                Log.d("KAKAO_API", "thumbnail image: " + profile.getThumbnailImageUrl());*/
                                } else if (kakaoAccount.profileNeedsAgreement() == OptionalBoolean.TRUE) {
                                    // 동의 요청 후 프로필 정보 획득 가능
                                } else {
                                    // 프로필 획득 불가
                                }
                            }
                            ((LoginActivity)mActivity).PlazaEnterActivity();
                        }
                    });
        }
    }
}