package com.example.basket.loginFragment;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.example.basket.R;
import com.example.basket.controller.MemberVerifier;
import com.example.basket.ui.main.LoginActivity;
import com.example.basket.ui.main.PlazaActivity;
import com.example.basket.util.VolleyCallback;
import com.example.basket.util.VolleyQueueProvider;
import com.example.basket.vo.MemberDTO;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BilFragment extends Fragment implements MemberVerifier {
    public static final String TAG = "bil";
    private static Context mContext;
    private Map<String, String> pMap = new HashMap<>();

    public EditText et_inputID = null;
    public EditText et_inputPW = null;


    @Override
    public void onAttach(@NonNull Context context) {
        Log.i(TAG, "onAttach()");
        super.onAttach(context);
        this.mContext = (Context)context;
        Log.i(TAG, "onAttach() mContext : " + mContext.toString());
        Log.i(TAG, "onAttach() mActivity : " + getActivity().toString());
        et_inputID = getActivity().findViewById(R.id.et_inputID);
        et_inputPW = getActivity().findViewById(R.id.et_inputPW);
        if((et_inputID.getText().toString()!=null&&et_inputID.getText().toString().length()>0)
                    &&(et_inputPW.getText().toString()!=null&&et_inputPW.getText().toString().length()>0)) {
            Log.i(TAG, "et_inputID : " + et_inputID.getText().toString());
            Log.i(TAG, "et_inputPW : " + et_inputPW.getText().toString());
        }
        pMap.put("mem_email", et_inputID.getText().toString());
        pMap.put("mem_pw", et_inputPW.getText().toString());
        loginProgress(pMap);
    }

    @Override
    public void loginProgress(Map<String, String> pMap) {
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
        VolleyQueueProvider.initRequestQueue(mContext);
        VolleyQueueProvider.openQueue();
        VolleyQueueProvider.callbackVolley(new VolleyCallback() {
            @Override
            public void onResponse(String response) { //resonse : JSONArray
                Log.i(TAG, "response : " + response);
                List<Map<String, Object>> resultList = new Gson().fromJson(response, List.class);
                if(resultList.size()==0){
                    Toast.makeText(mContext, "아이디가 존재하지 않습니다.", Toast.LENGTH_LONG).show();
                    ((LoginActivity)getActivity()).logOutActive();
                } else if(resultList.get(0).get("MEM_NAME").toString().equals("-1")){
                    Toast.makeText(mContext, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                    ((LoginActivity)getActivity()).logOutActive();
                } else {
                    Log.i(TAG, "BASKET CONNECT SUCCESS");
                    Log.i(TAG, "DATABASE ISNERT OR UPDATE SUCCESS");
                    for(Map.Entry dtoTOMap : resultList.get(0).entrySet()){
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
                    MemberDTO.getInstance().setMem_entrance(TAG);
                    Log.i(TAG, "MEMBERDTO toString() START ");
                    MemberDTO.getInstance().toString();
                    Log.i(TAG, "MEMBERDTO toString() FINISH ");
                    ((LoginActivity)getActivity()).plazaEnterActivity();
                    Toast.makeText(mContext, MemberDTO.getInstance().getMem_name() + "님 환영합니다.", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "error : " + error.toString());
            }
        }, "member/proc_login_bil", pMap);
    }

    @Override
    public void logoutProgress(Activity activity) {
        Log.i(TAG, "logoutProgress()");
        ((PlazaActivity)activity).loginEnterActivity();
        MemberDTO.getInstance().removeInfo();
    }
}
