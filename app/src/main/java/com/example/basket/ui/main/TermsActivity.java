package com.example.basket.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basket.R;

import java.util.HashMap;
import java.util.Map;

public class TermsActivity extends AppCompatActivity {
    CheckBox[] cba;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        ((TextView) findViewById(R.id.tv_terms1)).setMovementMethod(new ScrollingMovementMethod());
        ((TextView) findViewById(R.id.tv_terms2)).setMovementMethod(new ScrollingMovementMethod());
        ((TextView) findViewById(R.id.tv_terms3)).setMovementMethod(new ScrollingMovementMethod());
        cba = new CheckBox[] {findViewById(R.id.terms_cb_1),findViewById(R.id.terms_cb_2),findViewById(R.id.terms_cb_3),findViewById(R.id.terms_cb_4)};
    }

    public void goToLogin(View view) {
        onBackPressed();
    }

    public void goToSignUp(View view) {
        if (cba[0].isChecked() && cba[1].isChecked()) {
            Intent intent = new Intent(this, SignUpActivity.class);
            intent.putExtra("terms1", String.valueOf(cba[0].isChecked()));
            intent.putExtra("terms2", String.valueOf(cba[1].isChecked()));
            intent.putExtra("terms3", String.valueOf(cba[2].isChecked()));
            intent.putExtra("terms4", String.valueOf(cba[3].isChecked()));
            startActivity(intent);
        } else {
            Toast.makeText(this, "필수 항목들은 모두 체크해야합니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public void termsAll(View view) {
        if (((CheckBox) view).isChecked()) {
            for (CheckBox c : cba) {
                c.setChecked(true);
            }
        } else {
            for (CheckBox c : cba) {
                c.setChecked(false);
            }
        }
    }

    public void termsEa(View view) {
        for (CheckBox c : cba) {
            if (!c.isChecked()) {
                ((CheckBox) findViewById(R.id.terms_cb_all)).setChecked(false);
            }
        }
    }
}