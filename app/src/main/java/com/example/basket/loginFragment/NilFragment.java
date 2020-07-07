package com.example.basket.loginFragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.example.basket.R;
import com.example.basket.controller.MemberVerifier;
import com.example.basket.logical.HashUtil;
import com.example.basket.ui.main.LoginActivity;
import com.example.basket.ui.main.PlazaActivity;
import com.example.basket.util.VolleyCallback;
import com.example.basket.util.VolleyQueueProvider;
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
				/***************톰캣연동이되면 사라질것임************/
				((LoginActivity)getActivity()).PlazaEnterActivity();
				/**************************************************/
				loginProgress((Map<String, Object>)resultMap.get("response"));
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
	public void loginProgress(Map<String, Object> profileMap) {
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
				Map<String, Object> resultMap = new Gson().fromJson(response, Map.class);
				if(resultMap.get("mem_name").equals("아이디 또는 비밀번호가 일치하지 않습니다.")){
					Toast.makeText(mContext, "아이디 또는 비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
				} else {
					Log.i(TAG, "NAVER CONNECT SUCCESS");
					Log.i(TAG, "DATABASE ISNERT OR UPDATE SUCCESS");
					for(Map.Entry dtoTOMap : resultMap.entrySet()){
						if(dtoTOMap.getKey().equals("mem_code")) {
							MemberDTO.getInstance().setMem_code(dtoTOMap.getValue().toString());continue;}
						if(dtoTOMap.getKey().equals("mem_name")) {MemberDTO.getInstance().setMem_name(dtoTOMap.getValue().toString());continue;}
						if(dtoTOMap.getKey().equals("mem_email")) {MemberDTO.getInstance().setMem_email(dtoTOMap.getValue().toString());continue;}
						if(dtoTOMap.getKey().equals("mem_pw")) {MemberDTO.getInstance().setMem_pw(dtoTOMap.getValue().toString());continue;}
						if(dtoTOMap.getKey().equals("mem_age")) {MemberDTO.getInstance().setMem_age(dtoTOMap.getValue().toString());continue;}
						if(dtoTOMap.getKey().equals("mem_gender")) {MemberDTO.getInstance().setMem_gender(dtoTOMap.getValue().toString());continue;}
						if(dtoTOMap.getKey().equals("mem_birth")) {MemberDTO.getInstance().setMem_birth(dtoTOMap.getValue().toString());continue;}
						if(dtoTOMap.getKey().equals("mem_tel")) {MemberDTO.getInstance().setMem_tel(dtoTOMap.getValue().toString());continue;}
						if(dtoTOMap.getKey().equals("mem_entrance")) {MemberDTO.getInstance().setMem_tel(dtoTOMap.getValue().toString());continue;}
					}
					Log.i(TAG, "MEMBERDTO toString() START ");
					MemberDTO.getInstance().toString();
					Log.i(TAG, "MEMBERDTO toString() FINISH ");
				}
				((LoginActivity)getActivity()).PlazaEnterActivity();
				Toast.makeText(mContext, MemberDTO.getInstance().getMem_name() + "님 환영합니다.", Toast.LENGTH_LONG).show();
			}
			@Override
			public void onErrorResponse(VolleyError error) {

			}
		}, "", HashUtil.mapToDtoAndMapBinder(profileMap, TAG));
	}

	@Override
	public void logoutProgress(Activity activity) {
		Log.i(TAG, "logoutProgress()");
		if(activity!=null){
			Log.i(TAG, "activity : " + activity.toString());
		}
		if(mActivity!=null){
			Log.i(TAG, "mActivity : " + mActivity.toString());
		}
		if(getActivity()!=null){
			Log.i(TAG, "getActivity() : " + getActivity().toString());
		}
		//new RefreshTokenTask().execute();
		mOAuthLoginInstance.logout(mContext);
		//new DeleteTokenTask().execute();
		((PlazaActivity)activity).LoginEnterActivity();
		MemberDTO.getInstance().removeInfo();
	}
}