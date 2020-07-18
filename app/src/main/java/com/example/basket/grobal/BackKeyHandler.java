package com.example.basket.grobal;

import android.app.Activity;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

public class BackKeyHandler {
    private long keyPressTime = 0;
    private Toast toast;
    private Activity activity;

    public BackKeyHandler(Activity context){
        this.activity = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onBackPressed(){
        /* 다음 뒤로가기 키 입력시간에 따라서 종료할지, 유지할지 결정*/
        /* 2000 = 2초 입니다.*/
        if(System.currentTimeMillis() > keyPressTime + 2000){
            keyPressTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if(System.currentTimeMillis() <= keyPressTime + 2000){
            // 어플리케이션 종료
            toast.cancel();
            /*activity.moveTaskToBack(true);						// 태스크를 백그라운드로 이동
            activity.finishAndRemoveTask();						// 액티비티 종료 + 태스크 리스트에서 지우기
            android.os.Process.killProcess(android.os.Process.myPid());	// 앱 프로세스 종료*/
        }
    }

    public void showGuide(){
        /* Toast 창을 띄워주는 곳 */
        toast = Toast.makeText(activity, "\'뒤로\'버튼을 한번 더 누르시면 종료 됩니다.", toast.LENGTH_SHORT);
        toast.show();
    }
}
