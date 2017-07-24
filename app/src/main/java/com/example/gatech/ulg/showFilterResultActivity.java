package com.example.gatech.ulg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.StringTokenizer;

import eu.amirs.JSON;

public class showFilterResultActivity extends BaseActivity {


    private ArrayList<String> equipments = new ArrayList<String>();
    private String TAG = showFilterResultActivity.class.getSimpleName();
    private ArrayList<String> equipmentJSONstrs = new ArrayList<String>();




    private ListView listview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_show_filter_result, contentFrameLayout);

        Bundle bundle = getIntent().getExtras();
        String searchstr = bundle.getString("searchResult");

        Log.d(TAG, "searchstr:" + searchstr);



        JSON searchResult = new JSON(searchstr);

        for(int i=0; i<searchResult.key("equipList").count();i++){

            equipmentJSONstrs.add(searchResult.key("equipList").index(i).toString());

            String name = searchResult.key("equipList").index(i).key("equipType").toString();
            String location = searchResult.key("equipList").index(i).key("location").toString();
            String lab = searchResult.key("equipList").index(i).key("lab").toString();

            equipments.add(lab + " " + name + " Location:" + location);


        }

        Log.d(TAG, "Euipment:" + equipments.toString());



        listview = (ListView) findViewById(R.id.Equipments);
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, equipments);

        listview.setAdapter(itemsAdapter);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(showFilterResultActivity.this, showEquipmentInfoActivity.class);
                i.putExtra("equipmentJSONstr", equipmentJSONstrs.get(position));
                startActivity(i);
            }
        });

    }
}
