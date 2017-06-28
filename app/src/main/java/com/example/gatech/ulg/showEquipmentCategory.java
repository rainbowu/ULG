package com.example.gatech.ulg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class showEquipmentCategory extends AppCompatActivity {

    String[] listofCountries={"India","China","Nepal","Bhutan"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_equipment_category);

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listofCountries);


        ListView listView = (ListView) findViewById(R.id.Listview_catetory);
        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(showEquipmentCategory.this  ,listofCountries[position], Toast.LENGTH_LONG).show();
            }
        });



    }
}
