package com.example.gatech.ulg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class showFilterResultActivity extends AppCompatActivity {

    String[] listofSpinner = {"Eppendorf 5810R"};

    private ListView listview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_filter_result);

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
