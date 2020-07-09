package com.example.basket.ui.menu.event;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.basket.R;

public class EventActivity extends AppCompatActivity {
    public static String TAG ="EvnetActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);


        correspondingEvent();



    }

    public void correspondingEvent() {
        /*Log.i(TAG, "correspondingEvent()");
        VolleyQueueProvider.callbackVolley(new VolleyCallback() {
            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }, "", );*/
    }
}