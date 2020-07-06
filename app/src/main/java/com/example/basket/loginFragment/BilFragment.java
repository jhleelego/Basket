package com.example.basket.loginFragment;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.example.basket.controller.MemberVerifier;
import com.example.basket.logical.HashUtil;
import com.example.basket.ui.main.LoginActivity;
import com.example.basket.ui.main.PlazaActivity;
import com.example.basket.util.VolleyCallBack;
import com.example.basket.util.VolleyQueueProvider;
import com.example.basket.vo.MemberDTO;
import com.google.gson.Gson;

import java.util.Map;

public class BilFragment extends Fragment implements MemberVerifier {
    public static final String TAG = "Bil";
    private static Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        Log.i(TAG, "onAttach()");
        super.onAttach(context);
        this.mContext = (Context)context;
        Log.i(TAG, "onAttach() mContext : " + mContext.toString());
        Log.i(TAG, "onAttach() mActivity : " + getActivity().toString());
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
        VolleyQueueProvider.callbackVolley(new VolleyCallBack() {
            @Override
            public void onResponse(String response) { //resonse : JSONArray
                Map<String, Object> resultMap = new Gson().fromJson(response, Map.class);
                if(resultMap.get("mem_name").equals("아이디 또는 비밀번호가 일치하지 않습니다.")){
                    Toast.makeText(mContext, "아이디 또는 비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                    onStop();
                    onDestroy();
                    onDetach();
                } else {
                    Log.i(TAG, "BASKET CONNECT SUCCESS");
                    Log.i(TAG, "DATABASE ISNERT OR UPDATE SUCCESS");
                    for(Map.Entry dtoTOMap : resultMap.entrySet()){
                        if(dtoTOMap.getKey().equals("mem_code")) {MemberDTO.getInstance().setMem_code(dtoTOMap.getValue().toString());continue;}
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
        ((PlazaActivity)activity).LoginEnterActivity();
        MemberDTO.getInstance().removeInfo();
    }
}
