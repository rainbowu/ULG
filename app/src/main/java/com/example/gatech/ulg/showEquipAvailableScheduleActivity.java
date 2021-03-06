package com.example.gatech.ulg;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alamkanak.weekview.WeekViewEvent;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import eu.amirs.JSON;

public class showEquipAvailableScheduleActivity extends reserveCalenderBaseActivity {

    private ArrayList<WeekViewEvent> _event = new ArrayList<WeekViewEvent>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        String equipmentJSONstr = bundle.getString("events");

        JSON equipmentJSON = new JSON(equipmentJSONstr);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);




        for(int i=0; i<equipmentJSON.count(); i++){

            JSON event = equipmentJSON.index(i);

            String id = event.key("id").toString();
            String name = event.key("equip").toString();
            String location = event.key("location").toString();
            String eventType = event.key("eventType").toString();

            Calendar starttime = Calendar.getInstance();
            Calendar endtime = Calendar.getInstance();
            try {
                starttime.setTime(formatter.parse(event.key("start_time").toString()));
                endtime.setTime(formatter.parse(event.key("end_time").toString()));
            } catch (ParseException e) {
                Log.d("123", "Time parsing error.");
            }

            WeekViewEvent weekevent = new WeekViewEvent(1, name, starttime, endtime);

            // Workaround: Use the location field to pass the Sting.
            weekevent.setLocation(event.toString());
            if (eventType.equals("share"))
                weekevent.setColor(getResources().getColor(R.color.event_color_01));
            else
                weekevent.setColor(getResources().getColor(R.color.event_color_02));

            _event.add(weekevent);
        }




    }


    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        // Populate the week view with some events.
        ArrayList<WeekViewEvent> eventsMonth = new ArrayList<WeekViewEvent>();
        for (int i = 0; i < _event.size(); i++) {
            if (_event.get(i).getStartTime().get(Calendar.MONTH) == newMonth) {
                eventsMonth.add(_event.get(i));
            }
        }

        return eventsMonth;
    }
}
