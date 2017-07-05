package com.example.gatech.ulg;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    //private static String WEATHER_QUERY_URL_ZIP = "http://api.openweathermap.org/data/2.5/weather?zip=30318&APPID=109d7d4557083d7fd1f40f9f5aaf861d";
    private static String WEATHER_QUERY_URL_ZIP = "https://backend-dot-unitedlab-171401.appspot.com/type/";

    public ArrayList<String> WeatherInfo = new ArrayList<String>();
    public String GetResult = "";

    Button Get, Post;
    TextView Get_view, tvIsConnected;
    public EditText etName,etCountry,etTwitter;
    Person person;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Get = (Button) findViewById(R.id.button_get);
        Get_view = (TextView) findViewById(R.id.textView_get);
        etName = (EditText) findViewById(R.id.etName);
        etCountry = (EditText) findViewById(R.id.etCountry);
        etTwitter = (EditText) findViewById(R.id.etTwitter);
        Post = (Button) findViewById(R.id.button_post);
        tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);


        TabHost tabHost = (TabHost) findViewById(R.id.Tabhost);
        tabHost.setup();
        TabHost.TabSpec specs = tabHost.newTabSpec("Tag1");
        specs.setContent(R.id.tab1);
        specs.setIndicator("Get");
        tabHost.addTab(specs);

        specs = tabHost.newTabSpec("Tag2");
        specs.setContent(R.id.tab2);
        specs.setIndicator("Post");
        tabHost.addTab(specs);

        if(isConnected()){
            tvIsConnected.setText("You are conncted");
        }
        else{
            tvIsConnected.setText("You are NOT conncted");
        }

        // add click listener to Button "POST"
        Get.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                new GetWeathers().execute(WEATHER_QUERY_URL_ZIP);
                Get_view.setText(GetResult);



            }
        });

        Post.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if(!validate())
                    Toast.makeText(getBaseContext(), "Enter some data!", Toast.LENGTH_LONG).show();
                // call AsynTask to perform network operation on separate thread
               // new HttpAsyncTask().execute("http://hmkcode.appspot.com/jsonservlet");

               new HttpAsyncTask().execute("http://hmkcode.appspot.com/jsonservlet");


            }
        });



    }



    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    public static String POST(String url, Person person){
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";



            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("name", "bad");
            jsonObject.accumulate("country", "123123123123123");
            jsonObject.accumulate("twitter", "bad");
            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }



    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    private boolean validate(){
        if(etName.getText().toString().trim().equals(""))
            return false;
        else if(etCountry.getText().toString().trim().equals(""))
            return false;
        else if(etTwitter.getText().toString().trim().equals(""))
            return false;
        else
            return true;
    }



    private ArrayList<String> getWeatherInfo(JSONObject WeatherJson, String[]...
            forecastQueriedProperties) {

        /** This will store the forecast to be returned */
        ArrayList<String> weather = new ArrayList<>();

        try {
            for (String[] properties : forecastQueriedProperties) {
                JSONObject tempJSON = WeatherJson;
                String queriedProperty = "";
                for (int i = 0; i < properties.length; i++) {
                    if (i + 1 < properties.length) {
                        if (properties[i].equals("weather")) {
                            String wS = tempJSON.getString(properties[i]);
                            tempJSON = new JSONArray(wS).getJSONObject(0);
                        } else {
                            tempJSON = new JSONObject(tempJSON.getString(properties[i]));
                        }
                    } else {
                        queriedProperty = tempJSON.getString(properties[i]);
                    }

                }
                weather.add(queriedProperty);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return weather;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // 3. build jsonObject
            JSONObject js = new JSONObject();
            try {
                js.accumulate("name", "BOB");
                js.accumulate("country", "US");
                js.accumulate("twitter", "YOOOOOOOO!");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response

            String jsonStr = sh.makePOSTServiceCall(urls[0],js);
            GetResult = jsonStr;



//            String s = POST(urls[0],person);

            return jsonStr;

        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
        }
    }



    private class GetWeathers extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... urls) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response

            Log.e(TAG, "url: " + urls[0]);
            String jsonStr = sh.makeGETServiceCall(urls[0]);
            GetResult = jsonStr;

            Log.e(TAG, "Response from url: " + jsonStr);

//            if (jsonStr != null) {
//                try {
//                    JSONObject jsonObj = new JSONObject(jsonStr);
//
////                    WeatherInfo = getWeatherInfo(jsonObj,
////                            new String[]{"name"},
////                            new String[]{"sys", "country"},
////                            new String[]{"weather", "description"},
////                            new String[]{"main", "temp"},
////                            new String[]{"main", "pressure"},
////                            new String[]{"main", "humidity"});
////                    //Log.e(TAG, "Response from url: " + WeatherInfo);
////
//                } catch (final JSONException e) {
//                    Log.e(TAG, "Json parsing error: " + e.getMessage());
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getApplicationContext(),
//                                    "Json parsing error: " + e.getMessage(),
//                                    Toast.LENGTH_LONG)
//                                    .show();
//                        }
//                    });
//
//                }
//            } else {
//                Log.e(TAG, "Couldn't get json from server.");
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getApplicationContext(), "Please type in valid ZIP code.", Toast.LENGTH_LONG).show();
//                    }
//                });
//
//            }

            return null;
        }


    }

}
