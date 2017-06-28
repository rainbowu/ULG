package com.example.gatech.ulg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class filterEquipmentActivity extends AppCompatActivity {

    private TextView t1, t2;
    private Spinner s1, s2;
    private Button button;

    String[] listofSpinner = {"Item:","China","Nepal","Bhutan"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_equipment);

        t1 = (TextView) findViewById(R.id.textView1);
        t2 = (TextView) findViewById(R.id.textView2);
        s1 = (Spinner) findViewById(R.id.spinner1);
        s2 = (Spinner) findViewById(R.id.spinner2);
        button = (Button) findViewById(R.id.button);

        t1.setText(listofSpinner[0]);
        t2.setText(listofSpinner[0]);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listofSpinner);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(dataAdapter);
        s2.setAdapter(dataAdapter);


        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText( filterEquipmentActivity.this,
                        "OnClickListener : " +
                                "\nSpinner 1 : "+ String.valueOf(s1.getSelectedItem()) +
                                "\nSpinner 2 : "+ String.valueOf(s2.getSelectedItem()),
                        Toast.LENGTH_SHORT).show();
            }

        });


    }
}
