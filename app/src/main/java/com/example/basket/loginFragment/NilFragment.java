package com.example.basket.loginFragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.basket.ui.LoginActivity;
import com.example.basket.R;
import com.example.basket.controller.MemberVerifier;
import com.example.basket.logical.HashUtil;
import com.example.basket.vo.MemberDTO;
import com.google.gson.Gson;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import java.util.HashMap;
import java.util.Map;

/*
 * <br/> OAuth2.0 인증을 통해 Access Token을 발급받는 예제, 연동해제하는 예제,
 * <br/> 발급된 Token을 활용하여 Get 등의 명령을 수행하는 예제, 네아로 커스터마이징 버튼을 사용하는 예제 등이 포함되어 있다.
 * @author naver
 */
public class NilFragment extends Fragment implements MemberVerifier {

	public static final String TAG = "Nil";

	private static String OAUTH_CLIENT_ID = "XRp7KGaVvvtQRkzfCRo2";
	private static String OAUTH_CLIENT_SECRET = "Bpu2rQ8MEQ";
	private static String OAUTH_CLIENT_NAME = "Basket";

	private static OAuthLogin mOAuthLoginInstance;
	private static Context mContext;
	private static Activity mActivity;

	public OAuthLoginButton mOAuthLoginButton;


	Map<String, Object> resultMap = null;
	Map<String, Object> profileMap = null;

	public static NilFragment getInstance() {
		return NilFragment.LazyHolder.instance;
	}

	private static class LazyHolder {
		private static final NilFragment instance = new NilFragment();
	}

	@Override
	public void onAttach(@NonNull Context context) {
		super.onAttach(context);
		this.mContext = (Context)context;
		this.mActivity = (Activity)getActivity();
		Log.i(TAG, "onAttach()");
		Log.i(TAG, "onAttach() mContext : " + mContext.toString());
		initData();
		initView();
	}

	private void initData() {
		Log.i(TAG,"initData() 호출 ");
		mOAuthLoginInstance = OAuthLogin.getInstance();
		mOAuthLoginInstance.showDevelopersLog(true);
		mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME);
	}

	private void initView() {
		Log.i(TAG,"initView() 호출 ");
		Log.i(TAG, "mActivity : " + getActivity());
		mOAuthLoginButton = (OAuthLoginButton)getActivity().findViewById(R.id.btn_nil);
		mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);
		mOAuthLoginInstance.startOauthLoginActivity(getActivity(), mOAuthLoginHandler);
		updateView();
	}

	private void updateView() {
		Log.i(TAG,"updateView() 호출 ");
	}

	//private
	@Override
	public void onResume() {
		Log.i(TAG,"onResume() 호출 ");
		super.onResume();
	}

	public OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
		@Override
		public void run(boolean success) {
			Log.i(TAG,"run() 호출 ");
			if (success) {
				String accessToken = mOAuthLoginInstance.getAccessToken(mContext);
				String refreshToken = mOAuthLoginInstance.getRefreshToken(mContext);
				long expiresAt = mOAuthLoginInstance.getExpiresAt(mContext);
				String tokenType = mOAuthLoginInstance.getTokenType(mContext);
				String state =mOAuthLoginInstance.getState(mContext).toString();
				Log.i(TAG,"AccessToken  : " + accessToken);
				Log.i(TAG,"RefreshToken : " + refreshToken);
				Log.i(TAG,"ExpiresAt    : " + expiresAt);
				Log.i(TAG,"TokenType    : " + tokenType);
				Log.i(TAG,"State        : " + state);
				new RequestApiTask().execute();
			} else {
				String errorCode = mOAuthLoginInstance.getLastErrorCode(mContext).getCode();
				String errorDesc = mOAuthLoginInstance.getLastErrorDesc(mContext);
				Toast.makeText(mContext, "errorCode:" + errorCode + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
			}
		}
	};

	private class DeleteTokenTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			Log.i(TAG,"doInBackground() 호출 ");
			boolean isSuccessDeleteToken = mOAuthLoginInstance.logoutAndDeleteToken(mContext);
			if (!isSuccessDeleteToken) {
				// 서버에서 token 삭제에 실패했어도 클라이언트에 있는 token 은 삭제되어 로그아웃된 상태이다
				// 실패했어도 클라이언트 상에 token 정보가 없기 때문에 추가적으로 해줄 수 있는 것은 없음
				Log.d(TAG, "errorCode:" + mOAuthLoginInstance.getLastErrorCode(mContext));
				Log.d(TAG, "errorDesc:" + mOAuthLoginInstance.getLastErrorDesc(mContext));
			}
			return null;
		}

		protected void onPostExecute(Void v) {
			Log.i(TAG,"onPostExecute() 호출 ");
			updateView();
		}
	}

	private class RequestApiTask extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			Log.i(TAG,"onPreExecute() 호출 ");
		}

		@Override
		protected String doInBackground(Void... params) {
			Log.i(TAG,"doInBackground() 호출 ");
			String url = "https://openapi.naver.com/v1/nid/me";
			String at = mOAuthLoginInstance.getAccessToken(mContext);
			return mOAuthLoginInstance.requestApi(mContext, at, url);
		}

		protected void onPostExecute(String content) {
			Log.i(TAG, "onPostExecute() 호출 ");
			resultMap = new HashMap<>();
			resultMap = new Gson().fromJson(content, resultMap.getClass());
			if (resultMap.get("resultcode").equals("00") && resultMap.get("message").equals("success")) {
				profileMap = (Map<String, Object>)resultMap.get("response");
				((LoginActivity)getActivity()).enterActivity();
			}
		}
	}

	public class RefreshTokenTask extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {
			Log.i(TAG,"doInBackground() 호출 ");
			return mOAuthLoginInstance.refreshAccessToken(mContext);
		}

		protected void onPostExecute(String res) {
			Log.i(TAG,"onPostExecute() 호출 ");
			updateView();
		}
	}

	@Override
	public void loginProgress() {
		Log.i(TAG, "loginProgress()");
		HashUtil.mapToDTOBinder(profileMap, TAG);



		/************************************************************
		 *
		 *
		 * DB갖다오기(정보에 맞는 계정 로우 업데이트, 멤버코드 가져오기)
		 *
		 *
		 ************************************************************/

	}

	@Override
	public void logoutProgress() {
		Log.i(TAG, "logoutProgress()");
		new DeleteTokenTask().execute();
		mOAuthLoginInstance.logout(mContext);
		new RefreshTokenTask().execute();
		MemberDTO.getInstance().setMem_code(null);

	/*public void activityNil(String active){
		switch (active) {
			case "refresh" : { //토큰다시받기버튼
				new RefreshTokenTask().execute();
				break;
			}
			case "logout" : { //로그아웃버튼
				//OAuthLogin.logout() 메서드가 호출되면 클라이언트에 저장된 토큰이 삭제되고
				//OAuthLogin.getState() 메서드가 OAuthLoginState.NEED_LOGIN 값을 반환합니다.
				mOAuthLoginInstance.logout(mContext);
				updateView();
				break;
			}
			case "delete" : { //연동끊기버튼
				new DeleteTokenTask().execute();
				break;
			}
			default:
				break;
		}
	}*/
	}
}