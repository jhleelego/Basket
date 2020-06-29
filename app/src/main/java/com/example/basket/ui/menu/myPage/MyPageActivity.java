package com.example.basket.ui.menu.myPage;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.basket.R;

import static android.widget.Toast.LENGTH_LONG;

public class MyPageActivity extends AppCompatActivity {
    public static final String TAG = "MyPageActivity";
    EditText et_user_id = null;
    EditText et_user_pw = null;
    EditText et_user_tel = null;
    EditText et_user_name = null;
    EditText et_user_birth = null;
    EditText et_user_gender = null;
    Button btn_infoModified = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        et_user_id = (EditText) findViewById(R.id.et_inputID);
        et_user_pw = (EditText) findViewById(R.id.et_user_pw);
        et_user_tel = (EditText) findViewById(R.id.et_user_tel);
        et_user_name = (EditText) findViewById(R.id.et_user_name);
        et_user_birth = (EditText) findViewById(R.id.et_user_birth);
        et_user_gender = (EditText) findViewById(R.id.et_user_gender);
        btn_infoModified = (Button) findViewById(R.id.btn_modifiedAndConfirm);


        /********************
         인텐트로 받거나 DB를 갖다오거나. (인텐트좋은듯)

        et_user_id.setText("");
        et_user_pw.setText("");
        et_user_tel.setText("");
        et_user_name.setText("");
        et_user_birth.setText("");
        et_user_gender.setText("");

         ********************/


    }

    public void modifiedAndConfirm(View view) {
        /*Toast.makeText(this, "MODIFIEDANDCONFIRM", Toast.LENGTH_LONG).show();*/
        Log.i(TAG, "modifiedAndConfirm");
        Log.i(TAG, "btn_infoModified.getText : " + btn_infoModified.getText().toString());
        if (btn_infoModified.getText().toString().equals("회원정보수정")) {
            et_user_id.setEnabled(true);
            et_user_pw.setEnabled(true);
            et_user_tel.setEnabled(true);
            et_user_name.setEnabled(true);
            btn_infoModified.setText("수정하기");
        } else if (btn_infoModified.getText().toString().equals("수정하기")) {
            if (et_user_id.length() == 0 || et_user_id == null) {
                Toast.makeText(this, "아이디를 입력하여 주세요.", Toast.LENGTH_LONG).show();
                return;
            } else if (et_user_pw.length() > 0 && et_user_pw != null) {
                Toast.makeText(this, "비밀번호를 입력하여 주세요.", LENGTH_LONG).show();
                return;
            } else if (et_user_tel.length() > 0 && et_user_tel != null) {
                Toast.makeText(this, "핸드폰 번호를 입력하여 주세요.", LENGTH_LONG).show();
                return;
            } else if (et_user_name.length() > 0 && et_user_name != null) {
                Toast.makeText(this, "이름를 입력하여 주세요.", LENGTH_LONG).show();
                return;
            } else {

                /*******************

                       디비작업

                 *******************/
                /*****DB의 결과여부*****/
                boolean dbOK = true;
                /*****DB의 결과여부*****/
          /*      if(!dbOK) {





                } else {



                    et_user_id.setEnabled(false);
                    et_user_pw.setEnabled(false);
                    et_user_tel.setEnabled(false);
                    et_user_name.setEnabled(false);
                    btn_infoModified.setText("회원정보수정");
                }*/
            }
        }
    }
}
