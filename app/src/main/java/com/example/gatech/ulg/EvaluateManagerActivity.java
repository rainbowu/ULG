package com.example.gatech.ulg;

import android.graphics.Color;
import android.os.AsyncTask;
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

import com.alamkanak.weekview.WeekViewEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import eu.amirs.JSON;

public class EvaluateManagerActivity extends BaseActivity {

    private String TAG = EvaluateManagerActivity.class.getSimpleName();
    private static final String POST_EVALUATION_API = "https://unitedlab-171401.appspot.com/EventRate/";


    private Button submitReview;
    private RatingBar managerRatingBar;
    private RatingBar equipmentRatingBar;
    private int equipmentRate = 5;
    private int managerRate = 5;
    private TextView reviewText;
    private int equipid = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_evaluate_manager, contentFrameLayout);

        Bundle bundle = getIntent().getExtras();
        equipid = bundle.getInt("equipid");


        managerRatingBar = (RatingBar)findViewById(R.id.managerRatingBar);
        equipmentRatingBar = (RatingBar)findViewById(R.id.equipmentRatingBar);
        submitReview = (Button)findViewById(R.id.submitReview);
        reviewText = (TextView)findViewById(R.id.reviewText);

        setButtonListener();
        setEquipmentRatingListener();
        setManagerRatingListener();


    }


    public void setButtonListener() {
        submitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                JSON test = JSON.create(
                        JSON.dic(
                                "eventid", Integer.toString(equipid),
                                "manager_rate", Integer.toString(managerRate),
                                "manager_note", reviewText.getText().toString(),
                                "equip_rate", Integer.toString(equipmentRate)
                                )
                );


                HttpAsyncTask httpAsyncTask = new HttpAsyncTask(POST_EVALUATION_API, test.toString());
                httpAsyncTask.execute(POST_EVALUATION_API);



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


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        private String url;
        private HttpHandler httpHandler;
        private String postStr = "";


        public HttpAsyncTask(String url, String JSON) {

            this.url = url;
            this.postStr = JSON;
        }

        @Override
        protected String doInBackground(String... urls) {

            JSON temp = new JSON(postStr);

            httpHandler = new HttpHandler();

            Log.d(TAG, postStr);

            String jsonStr = httpHandler.makePOSTServiceCall(url, temp);

            return jsonStr;

        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {



                if (result != null){

                    Toast.makeText(EvaluateManagerActivity.this, "Rating submitted successfully!", Toast.LENGTH_SHORT).show();
                    finish();

                }else {
                    Toast.makeText(EvaluateManagerActivity.this, "Try again latter.", Toast.LENGTH_SHORT).show();
                }

        }

    }

}
