package com.example.gatech.ulg;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.amirs.JSON;

public class filterEquipmentActivity extends BaseActivity {

    private String POST_EQUIPSEARCH_API = "https://unitedlab-171401.appspot.com/EquipSearch/";


    private String TAG = filterEquipmentActivity.class.getSimpleName();

    private TextView categoryNameTextview, filter1Textview, filter2Textview, filter3Textview, seekBarValue;
    private Spinner spinner1, spinner2;
    private SeekBar seekBar;
    private Button button;
    private String Categoryid;

    private String CategoryName = "Category";

    private List<List<String>> ParameterEnumChoice = new ArrayList<List<String>>();
    private List<String> ParameterEnumNames = new ArrayList<String>();
    private List<String> ParameterNumericalNames = new ArrayList<String>();
    private Map<String, String> FilteridMap = new HashMap<String, String>();
    private int step = 1;


    private int seekBarMin, seekBarMax;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_filter_equipment, contentFrameLayout);


        filter1Textview = (TextView) findViewById(R.id.filter1);
        filter2Textview = (TextView) findViewById(R.id.filter2);
        filter3Textview = (TextView) findViewById(R.id.filter3);
        seekBarValue = (TextView) findViewById(R.id.seekbarvalue);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        button = (Button) findViewById(R.id.button);

        categoryNameTextview = (TextView) findViewById(R.id.category);
        categoryNameTextview.setTextColor(Color.parseColor("#0099ff"));

        Bundle bundle = getIntent().getExtras();


        String filterstr = bundle.getString("filters");
        JSON filter = new JSON(filterstr);

        // Parse data from JSON
        if (filter != null) {


            CategoryName = filter.key("name").stringValue();
            Categoryid = filter.key("id").stringValue();

            for (int i = 0; i < filter.key("specs").count(); i++) {
                JSON spec = filter.key("specs").index(i);


                if (spec.key("paraType").stringValue().equals("enum")) {
                    ArrayList<String> temp = new ArrayList<String>();

                    ParameterEnumNames.add(spec.key("name").stringValue());
                    FilteridMap.put(spec.key("name").stringValue(), spec.key("id").stringValue());

                    for (int j = 0; j < spec.key("parameter").count(); j++) {
                        JSON parameter = spec.key("parameter").index(j);
                        temp.add(parameter.key("name").stringValue());
                    }
                    Log.d(TAG, temp.toString());

                    ParameterEnumChoice.add(temp);
                } else if (spec.key("paraType").stringValue().equals("numerical")) {
                    ParameterNumericalNames.add(spec.key("name").stringValue());
                    FilteridMap.put(spec.key("name").stringValue(), spec.key("id").stringValue());

                    seekBarMin = spec.key("parameter").index(0).key("name").intValue();
                    seekBarMax = spec.key("parameter").index(1).key("name").intValue();
                }
            }

        } else {
            Log.e(TAG, "Couldn't get json from server.");
            Toast.makeText(getApplicationContext(), "Couldn't get data from server.", Toast.LENGTH_LONG).show();
        }

        categoryNameTextview.setText(CategoryName);

        Log.d(TAG, ParameterNumericalNames.toString());

        // Display data through UI
        if (ParameterEnumNames.size() >= 2) {


            filter1Textview.setText(ParameterEnumNames.get(0));
            filter2Textview.setText(ParameterEnumNames.get(1));

            ArrayAdapter<String> filter1dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, ParameterEnumChoice.get(0));
            filter1dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            ArrayAdapter<String> filter2dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, ParameterEnumChoice.get(1));
            filter2dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner1.setAdapter(filter1dataAdapter);
            spinner2.setAdapter(filter2dataAdapter);


        } else if (ParameterEnumNames.size() == 1) {

            filter1Textview.setText(ParameterEnumNames.get(0));

            ArrayAdapter<String> filter1dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, ParameterEnumChoice.get(0));
            filter1dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner1.setAdapter(filter1dataAdapter);

            filter2Textview.setVisibility(View.INVISIBLE);
            spinner2.setVisibility(View.INVISIBLE);


        } else {
            filter1Textview.setVisibility(View.INVISIBLE);
            spinner1.setVisibility(View.INVISIBLE);
            filter2Textview.setVisibility(View.INVISIBLE);
            spinner2.setVisibility(View.INVISIBLE);
        }


        if (ParameterNumericalNames.size() == 1) {

            filter3Textview.setText(ParameterNumericalNames.get(0));
            seekBar.setMax((seekBarMax - seekBarMin) / step);


            seekBar.setOnSeekBarChangeListener(
                    new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                        }

                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress,
                                                      boolean fromUser) {
                            // Ex :
                            // And finally when you want to retrieve the value in the range you
                            // wanted in the first place -> [3-5]
                            //
                            // if progress = 13 -> value = 3 + (13 * 0.1) = 4.3
                            double value = seekBarMin + (progress * step);
                            seekBarValue.setText(String.valueOf((int) value));
                        }
                    }
            );


        } else {
            filter3Textview.setVisibility(View.INVISIBLE);
            seekBar.setVisibility(View.INVISIBLE);
        }


        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // POST data

                // generate POST JSON

                JSON generatedJsonObject = JSON.create(
                        JSON.dic(
                                "typeID", Categoryid,
                                "paraList", JSON.array(
                                        JSON.dic(
                                                "specID", "1",
                                                "value", "1",
                                                "disabled", "false"
                                        ),
                                        JSON.dic(
                                                "specID", "1",
                                                "value", "1",
                                                "disabled", "false"
                                        ),
                                        JSON.dic(
                                                "specID", "1",
                                                "value", "1",
                                                "disabled", "false"
                                        )
                                )
                        )
                );



                Intent i = new Intent(filterEquipmentActivity.this, showFilterResultActivity.class);
                //i.putExtra("filters", categorymap.get(key));
                startActivity(i);
            }

        });


    }




    private class SearchEquipment extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... urls) {

            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeGETServiceCall(urls[0]);


            return jsonStr;
        }

        @Override
        protected void onPostExecute(String result) {


        }
    }





}



