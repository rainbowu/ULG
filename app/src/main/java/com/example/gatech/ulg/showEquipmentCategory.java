package com.example.gatech.ulg;

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
import java.util.List;
import java.util.Map;


public class showEquipmentCategory extends AppCompatActivity {

    String[] listofCountries={"India","China","Nepal","Bhutan"};
    private String TAG = showEquipmentCategory.class.getSimpleName();
    private ArrayList<String> category = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_equipment_category);

        new GetEquipmentCategory().execute("https://backend-dot-unitedlab-171401.appspot.com/type/");


        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, category);


        ListView listView = (ListView) findViewById(R.id.Listview_catetory);
        listView.setAdapter(itemsAdapter);

        // Button
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(showEquipmentCategory.this  ,category.get(position), Toast.LENGTH_LONG).show();



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

                    Map<String, List<String>> map = new HashMap<String, List<String>>();
                    Map<String, List<String>> filter2 = new HashMap<String, List<String>>();

                    for ( int i = 0; i < categories.length(); i++){
                        String name = categories.getJSONObject(i).getString("name");
                        JSONArray a1 = categories.getJSONObject(i).getJSONArray("filter1");
                        JSONArray a2 = categories.getJSONObject(i).getJSONArray("filter2");

                        List<String> templist1 = new ArrayList<String>();
                        List<String> templist2 = new ArrayList<String>();

                        for(int j = 0; j < a1.length(); j++){
                            templist1.add(a1.get(j).toString());
                        }
                        for(int j = 0; j < a1.length(); j++){
                            templist1.add(a2.get(j).toString());
                        }

                        category.add(name);
                        filter1.put(name, templist1);
                        filter2.put(name, templist2);

                    }
//
//                    for (Map.Entry<String, List<String>> entry : filter1.entrySet()) {
//                        Log.d(TAG, entry.getKey());
//                        Log.d(TAG, "--------------------------------------");
//                        List<String> valueList = entry.getValue();
//                        for (String s : valueList) {
//                            Log.d(TAG, s );
//                        }
//                    }












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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Q____________Q", Toast.LENGTH_LONG).show();
                    }
                });


            }
            return "1";
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
        }
    }








}
