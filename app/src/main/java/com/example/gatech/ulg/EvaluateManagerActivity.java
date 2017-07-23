package com.example.gatech.ulg;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class EvaluateManagerActivity extends BaseActivity {

    private String TAG = EvaluateManagerActivity.class.getSimpleName();



    private Button submitReview;
    private RatingBar managerRatingBar;
    private RatingBar equipmentRatingBar;
    private int equipmentRate = 5;
    private int managerRate = 5;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_evaluate_manager, contentFrameLayout);

        managerRatingBar = (RatingBar)findViewById(R.id.managerRatingBar);
        equipmentRatingBar = (RatingBar)findViewById(R.id.equipmentRatingBar);
        submitReview = (Button)findViewById(R.id.submitReview);


        setButtonListener();
        setEquipmentRatingListener();
        setManagerRatingListener();


    }


    public void setButtonListener() {
        submitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EvaluateManagerActivity.this, "Rating submitted successfully!", Toast.LENGTH_SHORT).show();

                finish();

            }
        });
    }

    public void setEquipmentRatingListener() {

        equipmentRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                equipmentRate = (int) Math.round(v);
                Log.d(TAG, "equipment rate:" + Integer.toString(equipmentRate) );

            }
        });
    }

    public void setManagerRatingListener() {

        managerRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                managerRate = (int) Math.round(v);
                Log.d(TAG, "manage rater:" + Integer.toString(managerRate) );

            }
        });


    }
}
