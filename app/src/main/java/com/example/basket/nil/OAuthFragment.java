package com.example.basket.nil;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.basket.LoginActivity;
import com.example.basket.MainActivity;
import com.example.basket.R;
import com.google.gson.Gson;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

/// 네이버 아이디로 로그인 샘플앱

/**
 * <br/> OAuth2.0 인증을 통해 Access Token을 발급받는 예제, 연동해제하는 예제,
 * <br/> 발급된 Token을 활용하여 Get 등의 명령을 수행하는 예제, 네아로 커스터마이징 버튼을 사용하는 예제 등이 포함되어 있다.
 *
 * @author naver
 */
public class OAuthFragment extends Fragment {

	private static final String TAG = "OAuthFragment";

	/**
	 * client 정보를 넣어준다.
	 */
	private static String OAUTH_CLIENT_ID = "XRp7KGaVvvtQRkzfCRo2";
	private static String OAUTH_CLIENT_SECRET = "Bpu2rQ8MEQ";
	private static String OAUTH_CLIENT_NAME = "Basket";

	private static OAuthLogin mOAuthLoginInstance;
	private static Context mContext;

	private OAuthLoginButton mOAuthLoginButton;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		Log.i("::OAuthFragment -", "onCreateView() 호출 ");
		Log.i("AAA", getActivity().toString());
		Log.i("::OAuthFragment -", "끝 ");
		Log.i("AAA", inflater.toString());
		ViewGroup root = null;
		if(getActivity() instanceof LoginActivity) {
			Log.i("INSTANCEOF", "LOGINACTIVITY");
			root = (ViewGroup) inflater.inflate(R.layout.activity_login, container, false);
		} else if(getActivity() instanceof MainActivity) {
			Log.i("INSTANCEOF", "MAINACTIVITY");
			root = (ViewGroup) inflater.inflate(R.layout.activity_main, container, false);
		}

		this.mContext = inflater.getContext();
		initData();
		initView(root);
		return root;
	}

	private void initData() {
		Log.i("::OAuthFragment -","initData() 호출 ");
		mOAuthLoginInstance = OAuthLogin.getInstance();
		mOAuthLoginInstance.showDevelopersLog(true);
		mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME);

	}

	private void initView(ViewGroup root) {
		Log.i("::OAuthFragment -","initView() 호출 ");

		if(getActivity() instanceof LoginActivity) {
			Log.i("INSTANCEOF", "LOGINACTIVITY");
			mOAuthLoginButton = (OAuthLoginButton)root.findViewById(R.id.buttonOAuthLoginImg);
			mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);
		}
		updateView();
	}


	private void updateView() {
		Log.i("::OAuthFragment -","updateView() 호출 ");
	}

	//private
	@Override
	public void onResume() {
		Log.i("::OAuthFragment -","onResume() 호출 ");
		//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		super.onResume();

	}

	private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
		@Override
		public void run(boolean success) {
			Log.i("::OAuthFragment -","run() 호출 ");
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


	/*public void onButtonClick(View v) throws Throwable {
		Log.i("::OAuthFragment -","onButtonClick() 호출 ");

		switch (v.getId()) {
			case R.id.buttonOAuth: { //인증하기버튼
				mOAuthLoginInstance.startOauthLoginActivity(OAuthFragment.this, mOAuthLoginHandler);
				break;
			}
			case R.id.buttonVerifier: { //API호출버튼
				new RequestApiTask().execute();
				break;
			}
			case R.id.buttonRefresh: { //토큰다시받기버튼
				new RefreshTokenTask().execute();
				break;
			}
			case R.id.buttonOAuthLogout: { //로그아웃버튼
				//OAuthLogin.logout() 메서드가 호출되면 클라이언트에 저장된 토큰이 삭제되고
				//OAuthLogin.getState() 메서드가 OAuthLoginState.NEED_LOGIN 값을 반환합니다.
				mOAuthLoginInstance.logout(mContext);
				updateView();
				break;
			}
			case R.id.buttonOAuthDeleteToken: { //연동끊기버튼
				new DeleteTokenTask().execute();
				break;
			}
			default:
				break;
		}
	}*/


	private class DeleteTokenTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			Log.i("::DeleteTokenTask -","doInBackground() 호출 ");
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
			Log.i("::DeleteTokenTask -","onPostExecute() 호출 ");
			updateView();
		}
	}

	private class RequestApiTask extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			Log.i("::RequestApiTask -","onPreExecute() 호출 ");
		}

		@Override
		protected String doInBackground(Void... params) {
			Log.i("::RequestApiTask -","doInBackground() 호출 ");
			String url = "https://openapi.naver.com/v1/nid/me";
			String at = mOAuthLoginInstance.getAccessToken(mContext);
			return mOAuthLoginInstance.requestApi(mContext, at, url);
		}

		protected void onPostExecute(String content) {
			Log.i("::RequestApiTask -", "onPostExecute() 호출 ");
			Log.i(TAG, "content : " + content);
			Gson gson = new Gson();
			Map<String, String> reulstMap = new HashMap<>();
			reulstMap = gson.fromJson(content, reulstMap.getClass());
			if (reulstMap.get("resultcode").equals("00") && reulstMap.get("message").equals("success")) {
				Log.i(TAG, "onPostExecute() : resultcode : " + reulstMap.get("resultcode"));
				Log.i(TAG, "onPostExecute() : message : " + reulstMap.get("message"));
				String response = gson.toJson(reulstMap.get("response"));
				Map<String, String> profileMap = new HashMap<>();
				profileMap = gson.fromJson(response, profileMap.getClass());
				Log.i(TAG, "onPostExecute() : id : " + profileMap.get("id"));
				Log.i(TAG, "onPostExecute() : email : " + profileMap.get("email"));
				Log.i(TAG, "onPostExecute() : nickname : " + profileMap.get("nickname"));
				Log.i(TAG, "onPostExecute() : age : " + profileMap.get("age"));
				Log.i(TAG, "onPostExecute() : gender : " + profileMap.get("gender"));
				Log.i(TAG, "onPostExecute() : name : " + profileMap.get("name"));
				Log.i(TAG, "onPostExecute() : birthday : " + profileMap.get("birthday"));
				String nickName = profileMap.get("nickname");
				Intent intent = new Intent(mContext, MainActivity.class);
				intent.putExtra("nickname", nickName);
				startActivity(intent);
				return;
			}
		}
	}

	private class RefreshTokenTask extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {
			Log.i("::RefreshTokenTask -","doInBackground() 호출 ");
			return mOAuthLoginInstance.refreshAccessToken(mContext);
		}

		protected void onPostExecute(String res) {
			Log.i("::RefreshTokenTask -","onPostExecute() 호출 ");
			updateView();
		}
	}
}