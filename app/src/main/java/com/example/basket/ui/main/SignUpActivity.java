package com.example.basket.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.example.basket.R;
import com.example.basket.ui.LoginActivity;
import com.example.basket.util.VolleyCallBack;
import com.example.basket.util.VolleyQueueProvider;
import com.example.basket.vo.MemberDTO;
import com.example.basket.vo.WalletSqlLiter;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import blockchain.ChainUtil;
import blockchain.Wallet;

public class SignUpActivity extends AppCompatActivity {
    private Map<String, String> pMap = new HashMap<>();
    private String mem_email;
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

    public void trySignup(View view) {
        String result = checkSignUp();
        PublicKey myKey;
        if (result.length() != 0) {
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        } else {
            myKey = MemberDTO.getInstance().getMem_wallet().publicKey;
            if (myKey != null) {
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(baos));
                    oos.writeObject(myKey);
                    oos.close();
                    pMap.put("myKey", new String(baos.toByteArray(), "ISO-8859-1"));
                    pMap.put("mem_email", mem_email);
                    pMap.put("mem_pw", mem_pw);
                    pMap.put("mem_name", mem_name);
                    pMap.put("mem_tel", mem_tel);
                    pMap.put("mem_birth", mem_birth);
                    pMap.put("mem_gender", mem_gender);
                    Log.e("info:", "pMap setting 완료");
                    VolleyQueueProvider.callbackVolley(new VolleyCallBack() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("[info]", "signUp onResponse");
                            String[] sa = response.split("#");
                            if ("success".equals(sa[0])) {
                                Toast.makeText(VolleyQueueProvider.app, "회원가입 성공:" + sa[1], Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            } else {
                                Toast.makeText(VolleyQueueProvider.app, "회원가입 실패:" + sa[1], Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(SignUpActivity.this, "회원가입 실패:" + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }, "chain/mem_signUp", pMap);
                } catch (Exception e) {
                    Log.e("SingUp", e.toString());
                }
            } else {
                Log.e("error", "공개키 미아?");
            }
        }
    }

    private String checkSignUp() {
        if ((mem_email = ((EditText) findViewById(R.id.et_user_email)).getText().toString()).length() == 0) {
            return "이메일을 입력해주세요";
        }
        if (mem_email.split("@").length != 2 || mem_email.split("@")[1].split("\\.").length != 2) {
            return "이메일이 형식에 맞지 않습니다.";
        }
        if ((mem_pw = ((EditText) findViewById(R.id.et_user_pw)).getText().toString()).length() == 0) {
            return "비밀번호를 입력해주세요";
        }
        if ((mem_vr = ((EditText) findViewById(R.id.et_user_vr)).getText().toString()).length() == 0) {
            return "비밀번호를 재입력해주세요";
        }
        if (!mem_pw.equals(mem_vr)) {
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
        if (mem_birth.length() != 6) {
            return "생년월일은 주민등록번호 앞자리의 형식으로 입력해주세요\nex)980123";
        }
        if ((mem_gender = ((EditText) findViewById(R.id.et_user_gender)).getText().toString()).length() == 0) {
            return "성별 입력해주세요";
        }
        return "";
    }

    public void goBack(View view) {
    }
}