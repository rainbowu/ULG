package com.example.gatech.ulg;

import android.graphics.RectF;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import eu.amirs.JSON;
import me.tittojose.www.timerangepicker_library.TimeRangePickerDialog;

import me.tittojose.www.timerangepicker_library.TimeRangePickerDialog;

public abstract class reserveCalenderBaseActivity extends BaseActivity implements WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener, TimeRangePickerDialog.OnTimeRangeSelectedListener {


    private static final String POST_RESERVEEVENT_API = "https://unitedlab-171401.appspot.com/EventUpdate/";
    private static final String ACCOUNT_INFO_API = "https://unitedlab-171401.appspot.com/AccountInfo/";



    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    private WeekView mWeekView;
    private WeekViewEvent CurrentClickedEvent;

    public static final String TIMERANGEPICKER_TAG = "timerangepicker";
    private String TAG = CalenderBaseActivity.class.getSimpleName();
    private String curUserName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_reserve_calender_base, contentFrameLayout);

//        setContentView(R.layout.activity_calender_base);


        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) findViewById(R.id.weekView);

        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(this);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);

        // Set long press listener for empty view
        mWeekView.setEmptyViewLongPressListener(this);

        // Set up a date time interpreter to interpret how the date and time will be formatted in
        // the week view. This is optional.
        setupDateTimeInterpreter(false);

        GetUserINnfo getUserINnfoAsyncTask = new GetUserINnfo(ACCOUNT_INFO_API);
        getUserINnfoAsyncTask.execute(ACCOUNT_INFO_API);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        setupDateTimeInterpreter(id == R.id.action_week_view);
        switch (id){
            case R.id.action_today:
                mWeekView.goToToday();
                return true;
            case R.id.action_day_view:
                if (mWeekViewType != TYPE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(1);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_three_day_view:
                if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_THREE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(3);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_week_view:
                if (mWeekViewType != TYPE_WEEK_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_WEEK_VIEW;
                    mWeekView.setNumberOfVisibleDays(7);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Set up a date time interpreter which will show short date values when in week view and long
     * date values otherwise.
     * @param shortDate True if the date values should be short.
     */
    private void setupDateTimeInterpreter(final boolean shortDate) {
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" M/d", Locale.getDefault());

                // All android api level do not have a standard way of getting the first letter of
                // the week day name. Hence we get the first char programmatically.
                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0));
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
                return hour > 11 ? (hour - 12) + " PM" : (hour == 0 ? "12 AM" : hour + " AM");
            }
        });
    }



    protected String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(this, "Clicked " + event.getName(), Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
//        Toast.makeText(this, "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Clicked " + event.getName(), Toast.LENGTH_SHORT).show();
        // Start event
        final TimeRangePickerDialog timePickerDialog = TimeRangePickerDialog.newInstance(reserveCalenderBaseActivity.this, false);
        timePickerDialog.show(this.getSupportFragmentManager(), TIMERANGEPICKER_TAG);
        CurrentClickedEvent = event;


    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {
        Toast.makeText(this, "Empty view long pressed: " + getEventTitle(time), Toast.LENGTH_SHORT).show();
    }

    public WeekView getWeekView() {
        return mWeekView;
    }

    @Override
    public void onTimeRangeSelected(int startHour, int startMin, int endHour, int endMin) {

        int EventStartHour = CurrentClickedEvent.getStartTime().get(Calendar.HOUR_OF_DAY);
        int EventStartMinute = CurrentClickedEvent.getStartTime().get(Calendar.MINUTE);

        int EventEndHour = CurrentClickedEvent.getEndTime().get(Calendar.HOUR_OF_DAY);
        int EventEndMinute = CurrentClickedEvent.getEndTime().get(Calendar.MINUTE);

        int EventStart = EventStartHour * 60 + EventStartMinute;
        int EventEnd = EventEndHour * 60  + EventEndHour;

        int SelcetedStart = startHour * 60 + startMin;
        int SelectedEnd = endHour * 60 + endMin;

        String eventTimeRange = Integer.toString(EventStartHour) + ":" + Integer.toString(EventStartMinute)
                + "~" + Integer.toString(EventEndHour) + ":" + Integer.toString(EventEndMinute);

        // Debug: print the event time and Clicked time
        String startTime = startHour + " : " + startMin;
        String endTime = endHour + " : " + endMin;

        String EventstartTime = EventStartHour + " : " + EventStartMinute;
        String EventendTime = EventEndHour + " : " + EventEndMinute;

        Log.d(TAG, "Event Time:"  + EventstartTime + "\n" + EventendTime);
        Log.d(TAG, "You Clicked" + startTime + "\n" + endTime);


        if (SelcetedStart < EventStart || SelcetedStart > EventEnd || SelectedEnd < EventStart || SelectedEnd > EventEnd ){

            Toast.makeText(this, "Please re-select time range within " + eventTimeRange, Toast.LENGTH_SHORT).show();

        }else{

            // TODO: Reverved + POST

            JSON curEventJSON = new JSON(CurrentClickedEvent.getLocation());

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

            String equip_id = curEventJSON.key("equipID").toString();
            String manager_username = curEventJSON.key("manager").toString();
            String title = curUserName + " reserve " + curEventJSON.key("equip").toString();


            Calendar clicked_start =  CurrentClickedEvent.getStartTime();
            Calendar clicked_end =  CurrentClickedEvent.getEndTime();

            clicked_start.set(Calendar.HOUR_OF_DAY, startHour);
            clicked_start.set(Calendar.MINUTE, startMin);

            clicked_end.set(Calendar.HOUR_OF_DAY, endHour);
            clicked_end.set(Calendar.MINUTE, endMin);

            String start_time = formatter.format(clicked_start.getTime());
            String end_time = formatter.format(clicked_end.getTime());

            JSON reverveEvent = JSON.create(
                    JSON.dic(
                            "method", "new",
                            "event", JSON.dic(
                                    "title", title,
                                    "eventType", "reserve",
                                    "manager", manager_username,
                                    "user", curUserName,
                                    "start_time", start_time,
                                    "end_time", end_time,
                                    "equipID", equip_id,
                                    "disable", "false"
                            )
                    )
            );

            HttpAsyncTask signup_login = new HttpAsyncTask(POST_RESERVEEVENT_API, reverveEvent.toString());
            signup_login.execute(POST_RESERVEEVENT_API);


        }


    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        private String url, postStr;
        private HttpHandler httpHandler;

        public HttpAsyncTask(String url, String postStr) {
            this.url = url;
            this.postStr = postStr;
        }

        @Override
        protected String doInBackground(String... urls) {

            JSON data = new JSON(postStr);

            httpHandler = new HttpHandler();
            String jsonStr = httpHandler.makePOSTServiceCall(url, data);

            Log.d(TAG, postStr);

            return jsonStr;

        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            JSON resultJSON = new JSON(result);

            if (result == null || resultJSON.key("status").equals("Error")){
                Toast.makeText(getApplicationContext(), "Cannot reserve the event, please try again.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, result);
            }else {
                Toast.makeText(getApplicationContext(), "Successfully reserve an equipment!!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, result);
            }

        }
    }


    private class GetUserINnfo extends AsyncTask<String, Void, String> {

        private String url;
        private HttpHandler httpHandler;

        public GetUserINnfo(String url) {
            this.url = url;
        }

        @Override
        protected String doInBackground(String... urls) {

            httpHandler = new HttpHandler();
            String jsonStr = httpHandler.makeGETServiceCall(ACCOUNT_INFO_API);
            return jsonStr;

        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            if (result != null){

                JSON res = new JSON(result);
                res = res.key("data");
                curUserName = res.key("username").toString();

            }

        }
    }

}
