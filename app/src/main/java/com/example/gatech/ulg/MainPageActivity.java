package com.example.gatech.ulg;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainPageActivity extends AppCompatActivity {


    private static final String ACCOUNT_INFO_API = "https://unitedlab-171401.appspot.com/AccountInfo/";
    private static final String LOGOUT_API = "https://unitedlab-171401.appspot.com/logout/";




    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);


        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(ACCOUNT_INFO_API, LOGOUT_API);
        httpAsyncTask.execute(ACCOUNT_INFO_API);

    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        private String url;
        private String logouturl;
        private HttpHandler httpHandler;

        public HttpAsyncTask(String url, String url2) {
            this.url = url;
            this.logouturl = url2;
        }

        @Override
        protected String doInBackground(String... urls) {

            httpHandler = new HttpHandler();


//            httpHandler.makePOSTServiceCall(LOGOUT_API, null);

//            Toast.makeText(getApplicationContext(), Boolean.toString(httpHandler.isLogged), Toast.LENGTH_LONG).show();


            String jsonStr = httpHandler.makeGETServiceCall(ACCOUNT_INFO_API);
            return jsonStr;

        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }
    }



}
