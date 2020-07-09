package com.example.basket.logical;

import android.util.Log;

import com.example.basket.loginFragment.KilFragment;
import com.example.basket.loginFragment.NilFragment;
import com.example.basket.vo.MemberDTO;

import java.util.HashMap;
import java.util.Map;

public class HashUtil {
    public static final String TAG = "HashUtil";
    public static Map<String, String> mapToDtoAndMapBinder(Map<String, String> profileMap, String mem_entrance){
        Log.i(TAG, "HashUtil - mapToDtoAndMapBinder");
        /****************************************************
         *  BASKET 콜백컬럼들
         *  mem_emial : jhleelego@naver.com
         *  mem_pw : 1234567
         *
         *  NAVER 콜백컬럼들
         *  id : 19044688
         *  email : jhleelego@naver.com
         *  nickname : 초보개발자
         *  age : 20-29
         *  gender : M
         *  name : 이정훈
         *  birthday : 03-07
         *
         *  KAKAO 콜백컬럼들
         *  id : 1392706345
         *  email : jhleelego@naver.com
         *  nickname : 이정훈
         *  age : AGE_20-29
         *  gender : MALE
         *  birthday : 0307
         *
         * 
         *  set되는 MemberDTO의 변수 AND put되는 updateMap의 KEY
         *  mem_email : String
         *  mem_name : String
         *  mem_age : String
         *  mem_gender : String
         *  mem_birth : String
         *  mem_entrance : String
         ****************************************************/
        Map<String, String> updateMap = new HashMap<>();
        if(profileMap!=null){
            MemberDTO memberDTO = MemberDTO.getInstance();
            for(Map.Entry vMap : profileMap.entrySet()) {
                Log.i(TAG, vMap.getValue().toString());
                if (vMap.getKey().equals("email")) {
                    memberDTO.setMem_email(vMap.getValue().toString());
                    updateMap.put("mem_email", vMap.getValue().toString());
                } else if (vMap.getKey().equals("name")) {
                        memberDTO.setMem_name(vMap.getValue().toString());
                        updateMap.put("mem_name", (vMap.getValue().toString()));
                } else if (vMap.getKey().equals("age")) {
                    if(mem_entrance.equals(NilFragment.TAG)){
                        memberDTO.setMem_age((vMap.getValue().toString()).substring(0,2));
                        updateMap.put("mem_age", (vMap.getValue().toString()).substring(0,2));
                    } else if(mem_entrance.equals(KilFragment.TAG)){
                        memberDTO.setMem_age((vMap.getValue().toString()).substring(4,5));
                        updateMap.put("mem_age", (vMap.getValue().toString()).substring(4,5));
                    }
                } else if (vMap.getKey().equals("gender")) {
                    if(vMap.getValue().equals("M")||vMap.getValue().equals("MALE")){
                        memberDTO.setMem_gender("남");
                        updateMap.put("mem_gender", "남");
                    } else if(vMap.getValue().equals("W")||vMap.getValue().equals("FEMALE")){
                        memberDTO.setMem_gender("여");
                        updateMap.put("mem_gender", "여");
                    }
                } else if (vMap.getKey().equals("birthday")) {
                    if(mem_entrance.equals(NilFragment.TAG)){
                        memberDTO.setMem_birth(vMap.getValue().toString().replace("-", ""));
                        updateMap.put("mem_birth", vMap.getValue().toString().replace("-", ""));
                    } else if(mem_entrance.equals(KilFragment.TAG)){
                        memberDTO.setMem_birth(vMap.getValue().toString().replace("-", ""));
                        updateMap.put("mem_birth", vMap.getValue().toString().replace("-", ""));
                    }
                }
            }
            memberDTO.setMem_entrance(mem_entrance);
            updateMap.put("mem_entrance", mem_entrance);

            Log.i(TAG, "TO DTO BINDER START");
            memberDTO.toString();
            Log.i(TAG, "TO DTO BINDER FINISH");

            Log.i(TAG, "TO MAP BINDER START");
            for(Map.Entry printMap : updateMap.entrySet()){
                Log.i(TAG, "printMap : " + printMap.getKey() + " : " + printMap.getValue().toString());
            }
            Log.i(TAG, "TO MAP BINDER FINISH");
        }
        return updateMap;
    }
}
