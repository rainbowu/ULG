package com.example.gatech.ulg;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class showEquipmentCategory extends AppCompatActivity {

    String[] listofCountries={"India","China","Nepal","Bhutan"};
    private String TAG = showEquipmentCategory.class.getSimpleName();
    private ArrayList<String> category = new ArrayList<String>();
    private Map<String, String> categorymap = new HashMap<String, String>();
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_equipment_category);

        new GetEquipmentCategory().execute("https://backend-dot-unitedlab-171401.appspot.com/type/");


        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, category);


        listView = (ListView) findViewById(R.id.Listview_catetory);
        listView.setAdapter(itemsAdapter);

        // Button
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String key = category.get(position);
                //Toast.makeText(showEquipmentCategory.this  , categorymap.get(key) , Toast.LENGTH_LONG).show();
                Intent i = new Intent(showEquipmentCategory.this, filterEquipmentActivity.class);
                i.putExtra("filters", categorymap.get(key));
                startActivity(i);
            }
        });



    }


    private class GetEquipmentCategory extends AsyncTask<String, Void, String> {
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
            return null;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
        }
    }








}
