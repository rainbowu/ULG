package com.example.gatech.ulg;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class EventCountdownActivity extends BaseActivity {


    private long timeCountInMilliSeconds = 0;


    //You need to pass in the duration time for this variable, in minutes.
    private int duration = 100;

    private enum TimerStatus {
        STARTED,
        STOPPED
    }

    private TimerStatus timerStatus = TimerStatus.STOPPED;

    private ProgressBar progressBarCircle;
    private TextView textViewTime;
    private ImageView imageViewStartStop;
    private CountDownTimer countDownTimer;
    private Button button;
    private int equipid = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_event_coundown, contentFrameLayout);

        Bundle bundle = getIntent().getExtras();
        int []id_duration = bundle.getIntArray("id_duration");
        equipid = id_duration[0];
        duration = id_duration[1];


        if (duration > 0) {
            // assigning values after converting to milliseconds
            timeCountInMilliSeconds = duration * 60 * 1000;
        } else {
            // toast message to fill edit text
            Toast.makeText(getApplicationContext(), R.string.wrongDurationTime, Toast.LENGTH_LONG).show();
        }




        // method call to initialize the views
        initViews();
        textViewTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));

        imageViewStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startStop();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            //You need to set the action for pressing the "COMPLETE!" button here
            public void onClick(View view) {
                timerStatus = TimerStatus.STOPPED;
                stopCountDownTimer();
                Toast.makeText(EventCountdownActivity.this, "Event completed, please provide some feedback.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), EvaluateManagerActivity.class);
                i.putExtra("equipid", equipid);
                startActivity(i);
                finish();

            }
        });

    }

    /**
     * method to initialize the views
     */
    private void initViews() {
        progressBarCircle = (ProgressBar) findViewById(R.id.progressBarCircle);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        imageViewStartStop = (ImageView) findViewById(R.id.imageViewStartStop);
        button = (Button) findViewById(R.id.finishActivityButton);
    }


    /**
     * method to start and stop count down timer
     */
    private void startStop() {
        if (timerStatus == TimerStatus.STOPPED) {
            imageViewStartStop.setVisibility(View.INVISIBLE);
            textViewTime.setVisibility(View.VISIBLE);
            button.setVisibility(View.VISIBLE);

            // call to initialize the progress bar values
            setProgressBarValues();

            // changing the timer status to started
            timerStatus = TimerStatus.STARTED;

            // call to start the count down timer
            startCountDownTimer();

        } else {

            // changing stop icon to start icon
            imageViewStartStop.setImageResource(R.drawable.icon_start);

            // changing the timer status to stopped
            timerStatus = TimerStatus.STOPPED;
            stopCountDownTimer();
        }
    }



    /**
     * method to start count down timer
     */
    private void startCountDownTimer() {

        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                textViewTime.setText(hmsTimeFormatter(millisUntilFinished));

                progressBarCircle.setProgress((int) (millisUntilFinished / 1000));

            }

            @Override
            public void onFinish() {

                textViewTime.setText("Finished!");

                // call to initialize the progress bar values
                progressBarCircle.setProgress(0);

                // changing the timer status to stopped
                timerStatus = TimerStatus.STOPPED;
                imageViewStartStop.setVisibility(View.INVISIBLE);

                Intent i = new Intent(getApplicationContext(), EvaluateManagerActivity.class);
                i.putExtra("equipid", equipid);
                startActivity(i);
                finish();
            }

        };
        countDownTimer.start();
    }

    /**
     * method to stop count down timer
     */
    private void stopCountDownTimer() {
        countDownTimer.cancel();
    }

    /**
     * method to set circular progress bar values
     */
    private void setProgressBarValues() {

        progressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
        progressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);
    }


    /**
     * method to convert millisecond to time format
     *
     * @param milliSeconds
     * @return HH:mm:ss time formatted string
     */
    private String hmsTimeFormatter(long milliSeconds) {

        String hms = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));

        return hms;


    }


}