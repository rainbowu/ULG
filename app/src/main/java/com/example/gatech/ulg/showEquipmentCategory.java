package com.example.gatech.ulg;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class showEquipmentCategory extends BaseActivity {

    //String[] listofCategory = ["Evaporator","entrifuges‎","glassware‎","porcelainware"‎, "Magnifiers","Microscopes‎","Thermometers‎"];


    private String TAG = showEquipmentCategory.class.getSimpleName();
    private ArrayList<String> category = new ArrayList<String>();
    private Map<String, String> categorymap = new HashMap<String, String>();
    private ListView listView;
    private String API = "https://backend-dot-unitedlab-171401.appspot.com/type/";
    private ArrayAdapter<String> itemsAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_show_equipment_category, contentFrameLayout);


        new GetEquipmentCategory().execute(API);


        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
        listView = (ListView) findViewById(R.id.Listview_catetory);
        listView.setAdapter(itemsAdapter);




        // Button
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String key = category.get(position);
                Intent i = new Intent(showEquipmentCategory.this, filterEquipmentActivity.class);
                i.putExtra("filters", categorymap.get(key));
                startActivity(i);
            }
        });



    }


    private class GetEquipmentCategory extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... urls) {

            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeGETServiceCall(urls[0]);

            if (jsonStr != null) {
                try {
                    //Log.d(TAG, "Get: " + jsonStr);
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray categories = jsonObj.getJSONArray("category");


                    for ( int i = 0; i < categories.length(); i++){
                        String name = categories.getJSONObject(i).getString("name");

                        category.add(name);
                        publishProgress(name);
                        categorymap.put(name, categories.getString(i));

                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");

            }
            return (null);
        }

        @Override
        protected void onProgressUpdate(String... item) {
            (itemsAdapter).add(item[0]);
        }

        @Override
        protected void onPostExecute(String result) {


        }
    }

}
