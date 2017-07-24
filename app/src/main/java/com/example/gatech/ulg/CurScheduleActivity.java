package com.example.gatech.ulg;

import com.alamkanak.weekview.WeekViewEvent;

import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import eu.amirs.JSON;

public class CurScheduleActivity extends CalenderBaseActivity {

    private String TAG = CurScheduleActivity.class.getSimpleName();
    private static final String GET_SCHEDULE_API = "https://unitedlab-171401.appspot.com/Eventlist/";
    private Map<String, WeekViewEvent> eventmap = new HashMap<String, WeekViewEvent>();

    private ArrayList _event = new ArrayList<WeekViewEvent>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(GET_SCHEDULE_API);
        httpAsyncTask.execute(GET_SCHEDULE_API);

    }



    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {


        return _event;
    }

    @Override
    public void onTimeRangeSelected(int startHour, int startMin, int endHour, int endMin) {

    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        private String url;
        private HttpHandler httpHandler;
        private String currentResult = "";


        public HttpAsyncTask(String url) {
            this.url = url;
        }

        @Override
        protected String doInBackground(String... urls) {


            httpHandler = new HttpHandler();
            String jsonStr = httpHandler.makeGETServiceCall(url);

            return jsonStr;

        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);


            JSON temp = new JSON(result);

            for(int i=0; i<temp.key("Eventlist").count(); i++){

                JSON event = temp.key("Eventlist").index(i);

                long id = event.key("id").longValue();
                String name = event.key("equip").toString();
                String location = event.key("location").toString();
                String eventType = event.key("eventType").toString();

                Calendar starttime = Calendar.getInstance();
                Calendar endtime = Calendar.getInstance();
                try {
                    starttime.setTime(formatter.parse(event.key("start_time").toString()));
                    endtime.setTime(formatter.parse(event.key("end_time").toString()));
                } catch (ParseException e) {
                    Log.d(TAG, "Time parsing error.");
                }

                WeekViewEvent weekevent = new WeekViewEvent(1, name, starttime, endtime);
                weekevent.setLocation(location);
                if (eventType.equals("share"))
                    weekevent.setColor(getResources().getColor(R.color.event_color_01));
                else
                    weekevent.setColor(getResources().getColor(R.color.event_color_02));
                weekevent.setId(id);

                eventmap.put(name, weekevent);
                _event.add(weekevent);
            }

        }


    }


}
