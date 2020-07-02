package com.example.basket.ui.menu.myStoreChoice;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.basket.R;


public class FavStoreActivity extends AppCompatActivity {
    ImageButton img_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_store);

         Toolbar  toolbar =findViewById(R.id.amsc_tb_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




    /*    img_btn = (ImageButton)findViewById(btn_maker_cho);
        onBackPressed();
*/
     /*   @Override 뒤로가기버튼 구현.
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    onBackPressed();
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }
*/


/*
   @Override
        public void onBackPressed() {
            super.onBackPressed();
            Log.i("nickname", nickName);
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("nickname", nickName);
            startActivity(intent);
        }

*/




    }

    }