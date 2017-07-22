package com.example.gatech.ulg;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SettingActivity extends BaseActivity {

    private static final String ACCOUNT_INFO_API = "https://unitedlab-171401.appspot.com/AccountInfo/";
    private String TAG = SettingActivity.class.getSimpleName();


    @Bind(R.id.input_name) EditText _nameText;
    @Bind(R.id.input_username) EditText _username;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.btn_update) Button _updateButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_setting, contentFrameLayout);

        ButterKnife.bind(this);

        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(ACCOUNT_INFO_API);
        httpAsyncTask.execute(ACCOUNT_INFO_API);


        _updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Update Information.", Toast.LENGTH_LONG).show();

            }
        });


    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        private String url;
        private HttpHandler httpHandler;
        private String name;
        private String username;
        private String email;


        public HttpAsyncTask(String url) {
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
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

            if (result != null){
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    _nameText.setText(jsonObj.getString("userType"));
                    _emailText.setText(jsonObj.getString("email"));
                    _username.setText(jsonObj.getString("username"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }else {
                Toast.makeText(getApplicationContext(), "Please check network connection.", Toast.LENGTH_LONG).show();
            }

        }
    }





}
