package com.example.basket.logical;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class HashUtil {
    public static final String TAG = "HashUtil";
    public static Map<String, Object> mapToVOBinder(Map<String, Object> profileMap, String mem_entrance){
        Log.i(TAG, "HashUtil - mapToVOBinder");
        Map<String, Object> pMap = new HashMap<>();
        pMap.put("mem_entrance", mem_entrance);
        /****************************************************
         * 콜백컬럼들
         *  id : 19044688
         *  email : jhleelego@naver.com
         *  nickname : 초보개발자
         *  age : 20-29
         *  gender : M
         *  name : 이정훈
         *  birthday : 03-07
         * 
         * 생산컬럼들
         *  mem_id : int
         *  mem_name : String
         *  mem_age : String
         *  mem_getder : String
         *  mem_birthday : String
         ****************************************************/
        if(profileMap!=null){
            for(Map.Entry vMap : profileMap.entrySet()) {
                Log.i(TAG, vMap.getValue().toString());
                if (vMap.getKey().equals("email")) {
                    pMap.put("mem_id", vMap.getValue().toString());
                } else if (vMap.getKey().equals("name")) {
                    pMap.put("mem_name", vMap.getValue().toString());
                } else if (vMap.getKey().equals("age")) {
                    pMap.put("mem_age", vMap.getValue().toString());
                } else if (vMap.getKey().equals("gender")) {
                    pMap.put("mem_gender", vMap.getValue().toString());
                } else if (vMap.getKey().equals("birthday")) {
                    pMap.put("mem_birth", vMap.getValue().toString());
                }
            }
            Log.i(TAG, "생성된컬럼 시작 ");
            for(Map.Entry rMap : pMap.entrySet()) {
                Log.i(TAG, rMap.getKey().toString() + " , " + rMap.getValue().toString());
            }
            Log.i(TAG, "생성된컬럼  끝 ");



        }
        return pMap;
    }
}
