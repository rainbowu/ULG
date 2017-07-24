package com.example.gatech.ulg;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import eu.amirs.JSON;

public class SettingActivity extends BaseActivity {

    private static final String ACCOUNT_INFO_API = "https://unitedlab-171401.appspot.com/AccountInfo/";
    private String TAG = SettingActivity.class.getSimpleName();

    private TextView _nameText, _usernameText, _emailText;
    private Button _updateButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_setting, contentFrameLayout);

        _nameText = (TextView) findViewById(R.id.name);
        _usernameText = (TextView) findViewById(R.id.username);
        _emailText = (TextView) findViewById(R.id.email);
        _updateButton = (Button) findViewById(R.id.btn_update);

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

            JSON res = new JSON(result);
            res = res.key("data");
            Log.d(TAG, res.key("firstName").toString() + " " + res.key("lastName").toString());
            Log.d(TAG, res.key("email").toString());

            _nameText.setText(res.key("firstName").toString() + " " + res.key("lastName").toString());
            _emailText.setText(res.key("email").toString());
            _usernameText.setText(res.key("username").toString());

        }
    }





}
