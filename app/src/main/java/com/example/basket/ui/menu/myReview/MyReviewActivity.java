package com.example.basket.ui.menu.myReview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RatingBar;

import com.example.basket.R;

public class MyReviewActivity extends AppCompatActivity {
    private RatingBar ratingbar_small;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_review);

        ratingbar_small = findViewById(R.id.ratingbarSmall);

        ratingbar_small.setOnRatingBarChangeListener(new Listener());
    }

    class Listener implements RatingBar.OnRatingBarChangeListener
    {
        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            ratingbar_small.setRating(rating);
        }
    }
}