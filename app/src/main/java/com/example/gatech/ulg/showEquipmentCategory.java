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

import eu.amirs.JSON;


public class showEquipmentCategory extends BaseActivity {

    private String GET_EQUIPTYPELIST_API = "https://unitedlab-171401.appspot.com/EquipTypeList/";

    private String TAG = showEquipmentCategory.class.getSimpleName();
    private ArrayList<String> category = new ArrayList<String>();
    private Map<String, String> categorymap = new HashMap<String, String>();
    private ListView listView;
    private ArrayAdapter<String> itemsAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_show_equipment_category, contentFrameLayout);

        new GetEquipmentCategory().execute(GET_EQUIPTYPELIST_API);

        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
        listView = (ListView) findViewById(R.id.Listview_catetory);
        listView.setAdapter(itemsAdapter);


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

            Log.d(TAG, "Get: " + jsonStr);

            JSON EquipTypeList = new JSON(jsonStr);

            EquipTypeList.key("Library").count();

            for(int i=0; i<EquipTypeList.key("Library").count(); i++){

                String name = EquipTypeList.key("Library").index(i).key("name").stringValue();
                String specs = EquipTypeList.key("Library").index(i).toString();
                category.add(name);
                publishProgress(name);
                categorymap.put(name, specs);
            }

            return jsonStr;
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
