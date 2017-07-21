package com.example.gatech.ulg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

public class showFilterResultActivity extends BaseActivity {

    String[] listofSpinner = {"Eppendorf 5810R"};

    private ListView listview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_show_filter_result, contentFrameLayout);

//        setContentView(R.layout.activity_show_filter_result);

        listview = (ListView) findViewById(R.id.Equipments);

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listofSpinner);



        listview.setAdapter(itemsAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(showFilterResultActivity.this, showEquipmentInfoActivity.class);
                //i.putExtra("filters", categorymap.get(key));
                startActivity(i);
            }
        });

    }
}
