package com.example.basket.loginFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.example.basket.controller.MemberVerifier;
import com.example.basket.logical.OAuthCallbackParser;
import com.example.basket.ui.main.LoginActivity;
import com.example.basket.ui.main.PlazaActivity;
import com.example.basket.util.VolleyCallback;
import com.example.basket.util.VolleyQueueProvider;
import com.example.basket.vo.MemberDTO;
import com.google.gson.Gson;
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
import java.util.List;
import java.util.Map;

public class KilFragment extends Fragment implements MemberVerifier {
    public static final String TAG = "kil";
    private SessionCallback sessionCallback = new SessionCallback();
    Session session;
    private Context mContext;
    private Activity mActivity;
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
            ((LoginActivity)getActivity()).logOutActive();
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
                                Map<String, String> profileMap = new HashMap<>();
                                if(kakaoAccount.getEmail()!=null){
                                    Log.i(TAG, "kakaoAccount.getEmail() : " + kakaoAccount.getEmail());
                                    profileMap.put("email",kakaoAccount.getEmail());
                                }
                                if(kakaoAccount.getProfile()!=null){
                                    if(kakaoAccount.getProfile().getNickname()!=null){
                                        Log.i(TAG,  "kakaoAccount.getProfile().getNickname() : " + kakaoAccount.getProfile().getNickname());
                                        profileMap.put("name", kakaoAccount.getProfile().getNickname());
                                    }
                                }
                                if(kakaoAccount.getAgeRange()!=null){
                                    Log.i(TAG, "kakaoAccount.getAgeRange().toString() : " + kakaoAccount.getAgeRange().toString());
                                    profileMap.put("age",kakaoAccount.getAgeRange().toString());
                                }
                                if(kakaoAccount.getGender()!=null){
                                    Log.i(TAG, "kakaoAccount.getGender().toString() : " + kakaoAccount.getGender().toString());
                                    profileMap.put("gender",kakaoAccount.getGender().toString());
                                }
                                if(kakaoAccount.getBirthday()!=null){
                                    Log.i(TAG, "kakaoAccount.getBirthday() : " + kakaoAccount.getBirthday());
                                    profileMap.put("birthday",kakaoAccount.getBirthday());
                                }
                                loginProgress(profileMap);
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
                            ((LoginActivity)mActivity).plazaEnterActivity();
                        }
                    });
        }
    }

    @Override
    public void loginProgress(Map<String, String> profileMap) {
        Log.i(TAG, "loginProgress()");
        /************DB갖다오기 (정보에 맞는 계정 로우 업데이트, 멤버정보 가져오기)************
         * mem_code
         * mem_name
         * mem_email
         * mem_pw
         * mem_age
         * mem_gender
         * mem_birth
         * mem_tel
         * mem_entrance
         **********************************************************************************/
        VolleyQueueProvider.callbackVolley(new VolleyCallback() {
            @Override
            public void onResponse(String response) { //resonse : JSONArray
                Log.i(TAG, "response : " + response);

                Map<String, Object> proResultMap = (Map<String, Object>)((List<Map<String, Object>>)(new Gson().fromJson(response, List.class))).get(0);
                for(Map.Entry dtoTOMap : proResultMap.entrySet()){
                    if(dtoTOMap.getKey().equals("MEM_CODE")) {MemberDTO.getInstance().setMem_code(dtoTOMap.getValue().toString());continue;}
                    if(dtoTOMap.getKey().equals("MEM_NAME")) {MemberDTO.getInstance().setMem_name(dtoTOMap.getValue().toString());continue;}
                    if(dtoTOMap.getKey().equals("MEM_EMAIL")) {MemberDTO.getInstance().setMem_email(dtoTOMap.getValue().toString());continue;}
                    if(dtoTOMap.getKey().equals("MEM_PW")) {MemberDTO.getInstance().setMem_pw(dtoTOMap.getValue().toString());continue;}
                    if(dtoTOMap.getKey().equals("MEM_AGE")) {MemberDTO.getInstance().setMem_age(dtoTOMap.getValue().toString());continue;}
                    if(dtoTOMap.getKey().equals("MEM_GENDER")) {MemberDTO.getInstance().setMem_gender(dtoTOMap.getValue().toString());continue;}
                    if(dtoTOMap.getKey().equals("MEM_BIRTh")) {MemberDTO.getInstance().setMem_birth(dtoTOMap.getValue().toString());continue;}
                    if(dtoTOMap.getKey().equals("MEM_TEL")) {MemberDTO.getInstance().setMem_tel(dtoTOMap.getValue().toString());continue;}
                    if(dtoTOMap.getKey().equals("MEM_ENTRANCE")) {MemberDTO.getInstance().setMem_tel(dtoTOMap.getValue().toString());continue;}
                }
                Log.i(TAG, "MEMBERDTO toString() START ");
                MemberDTO.getInstance().toString();
                Log.i(TAG, "MEMBERDTO toString() FINISH ");
                ((LoginActivity)getActivity()).plazaEnterActivity();
                    Toast.makeText(mContext, MemberDTO.getInstance().getMem_name() + "님 환영합니다.", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "error : " + error.toString());
            }
        }, "member/proc_login_social", OAuthCallbackParser.mapToDtoAndMapBinder(profileMap, TAG));
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
        ((PlazaActivity)activity).loginEnterActivity();
    }

}