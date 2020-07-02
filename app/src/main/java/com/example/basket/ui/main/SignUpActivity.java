package com.example.basket.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.basket.R;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private Map<String, String> pMap = new HashMap<>();
    private String mem_id;
    private String mem_pw;
    private String mem_vr;
    private String mem_name;
    private String mem_tel;
    private String mem_birth;
    private String mem_gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ((EditText) findViewById(R.id.et_user_tel)).addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        pMap.put("terms1", getIntent().getStringExtra("terms1"));
        pMap.put("terms2", getIntent().getStringExtra("terms2"));
        pMap.put("terms3", getIntent().getStringExtra("terms3"));
        pMap.put("terms4", getIntent().getStringExtra("terms4"));
    }

    public void tryLogin(View view) {
        String result = checkSignUp();
        if (result.length() != 0) {
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        } else {
            pMap.put("mem_id", mem_id);
            pMap.put("mem_pw", mem_pw);
            pMap.put("mem_name", mem_name);
            pMap.put("mem_tel", mem_tel);
            pMap.put("mem_birth", mem_birth);
            pMap.put("mem_gender", mem_gender);
        }
    }

    private String checkSignUp() {
        if ((mem_id = ((EditText) findViewById(R.id.et_user_id)).getText().toString()).length() == 0) {
            return "아이디를 입력해주세요";
        }
        if ((mem_pw = ((EditText) findViewById(R.id.et_user_pw)).getText().toString()).length() == 0) {
            return "비밀번호를 입력해주세요";
        }
        if ((mem_vr = ((EditText) findViewById(R.id.et_user_vr)).getText().toString()).length() == 0) {
            return "비밀번호를 재입력해주세요";
        }
        if (mem_pw.compareTo(mem_vr) != 0) {
            return "비밀번호가 일치하지 않습니다";
        }
        if ((mem_name = ((EditText) findViewById(R.id.et_user_name)).getText().toString()).length() == 0) {
            return "이름을 입력해주세요";
        }
        if ((mem_tel = ((EditText) findViewById(R.id.et_user_tel)).getText().toString()).length() == 0) {
            return "전화번호를 입력해주세요";
        }
        if ((mem_birth = ((EditText) findViewById(R.id.et_user_birth)).getText().toString()).length() == 0) {
            return "생년월일을 입력해주세요";
        }
        if ((mem_gender = ((EditText) findViewById(R.id.et_user_gender)).getText().toString()).length() == 0) {
            return "성별 입력해주세요";
        }
        return "";
    }

    public void goBack(View view) {
    }
}