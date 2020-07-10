package com.example.basket.vo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBHelper extends SQLiteOpenHelper {
    public static final String TAG = "DBHelper";
    public static final int DB_VERSION = 1; // 개발자가 버전관리
    public static final String name = "DBHelper.sqlite.basket";


    //DB헬퍼 넣기.
    public DBHelper(Context context) {
        super(context, name, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "onCreate()");
        db.execSQL("CREATE TABLE BASKET_soak (PRO_CODE INTEGER , PRO_IMG TEXT, PRO_STOCK_EA INTEGER, PRO_NAME TEXT, PRO_PRICE INTEGER);");
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "onUpgrade()");

    }

    //인서트하는 메소드이름.
    public void insert(int PRO_CODE, String PRO_IMG, int PRO_STOCK_EA , String PRO_NAME , int PRO_PRICE) {
        Log.i(TAG, "insert()");
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO BASKET_soak VALUES('" + PRO_CODE +"', '" + PRO_IMG + "', '" + PRO_STOCK_EA + "', '" + PRO_NAME + "',  '" + PRO_PRICE + "');");
        //DB닫아버리기
        db.close();
    }

    //갯수 수정버튼
    public void update(int PRO_STOCK_EA ) {
        Log.i(TAG, "update()");
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE BASKET_soak SET PRO_STOCK_EA='" + PRO_STOCK_EA+"';");
        db.close();
    }

    //삭제하기버튼 취소버튼
    //DROP TABLE BASKET
    public void delete(String PRO_NAME) {
        Log.i(TAG, "delete()");
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM BASKET_soak WHERE PRO_NAME='" + PRO_NAME + "';");
        db.close();
    }

    //읽고 쓸때 커서를 이용해야됨.
    public List<Map<String, String>> getResult() {
        Log.i(TAG, "getResult()");
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM BASKET_soak", null);
        List<Map<String, String>> basketList = new ArrayList<>();
        Map<String, String> basketOneMap = null;
        while (cursor.moveToNext()) {
            basketOneMap = new HashMap<>();
            basketOneMap.put("PRO_IMG", cursor.getString(1));
            basketOneMap.put("PRO_STOCK_EA", Integer.toString(cursor.getInt(2)));
            basketOneMap.put("PRO_NAME", cursor.getString(3));
            basketOneMap.put("PRO_PRICE", Integer.toString(cursor.getInt(4)));
            basketList.add(basketOneMap);
        }
        return basketList;

    }


}