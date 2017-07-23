package com.example.gatech.ulg;

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

    private TextView categoryNameTextview, filter1Textview, filter2Textview, filter3Textview;
    private TextView seekBarValue1, seekBarValue2, seekBarValue3;
    private Spinner spinner1, spinner2, spinner3;
    private SeekBar seekBar1, seekBar2, seekBar3;
    private Button button;
    private String Categoryid;

    private String CategoryName = "Category";

    private List<List<String>> ParameterEnumChoice = new ArrayList<List<String>>();
    private List<String> ParameterEnumNames = new ArrayList<String>();
    private List<String> ParameterNumericalNames = new ArrayList<String>();
    private Map<String, String> FilteridMap = new HashMap<String, String>();
    private Map<String, Spinner> SpinnerMap = new HashMap<String, Spinner>();
    private Map<String, TextView> SeekBarValueMap = new HashMap<String, TextView>();
    private List<Spinner> spinners = new ArrayList<Spinner>();
    private List<SeekBar> seekbars = new ArrayList<SeekBar>();
    private List<TextView> seekbarvalues = new ArrayList<TextView>();
    private List<TextView> filternames = new ArrayList<TextView>();
    private List<List<Integer>> ParameterNumericalRange = new ArrayList<List<Integer>>();


    private String searchResult;
    private int currentFilterCount = 0;
    private int step = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_filter_equipment, contentFrameLayout);


        filter1Textview = (TextView) findViewById(R.id.filter1);
        filter2Textview = (TextView) findViewById(R.id.filter2);
        filter3Textview = (TextView) findViewById(R.id.filter3);
        filternames.add(filter1Textview);
        filternames.add(filter2Textview);
        filternames.add(filter3Textview);


        seekBarValue1 = (TextView) findViewById(R.id.seekbarvalue1);
        seekBarValue2 = (TextView) findViewById(R.id.seekbarvalue2);
        seekBarValue3 = (TextView) findViewById(R.id.seekbarvalue3);
        seekbarvalues.add(seekBarValue1);
        seekbarvalues.add(seekBarValue2);
        seekbarvalues.add(seekBarValue3);

        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        spinners.add(spinner1);
        spinners.add(spinner2);
        spinners.add(spinner3);

        seekBar1 = (SeekBar) findViewById(R.id.seekBar1);
        seekBar2 = (SeekBar) findViewById(R.id.seekBar2);
        seekBar3 = (SeekBar) findViewById(R.id.seekBar3);
        seekbars.add(seekBar1);
        seekbars.add(seekBar2);
        seekbars.add(seekBar3);

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
                    temp.add("Any");

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

                    ArrayList<Integer> temp = new ArrayList<Integer>();
                    temp.add(spec.key("parameter").index(0).key("name").intValue());
                    temp.add(spec.key("parameter").index(1).key("name").intValue());
                    ParameterNumericalRange.add(temp);
                }
            }

        } else {
            Log.e(TAG, "Couldn't get json from server.");
            Toast.makeText(getApplicationContext(), "Couldn't get data from server.", Toast.LENGTH_LONG).show();
        }

        categoryNameTextview.setText(CategoryName);

        Log.d(TAG, ParameterNumericalNames.toString());

        // Display data through UI
        for (int i = 0; i < ParameterEnumNames.size(); i++) {

            filternames.get(currentFilterCount).setText(ParameterEnumNames.get(i));

            ArrayAdapter<String> filterdataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, ParameterEnumChoice.get(i));

            filterdataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinners.get(currentFilterCount).setAdapter(filterdataAdapter);

            SpinnerMap.put(ParameterEnumNames.get(i), spinners.get(currentFilterCount));

            // Hide unused seekbar
            seekbars.get(currentFilterCount).setVisibility(View.INVISIBLE);

            currentFilterCount += 1;
        }

        Log.d(TAG, ParameterNumericalRange.toString());

        for (int i = 0; i < ParameterNumericalNames.size(); i++) {

            final TextView seekbarvalue = seekbarvalues.get(currentFilterCount);
            final int seekBarMin = ParameterNumericalRange.get(i).get(0);
            final int seekBarMax = ParameterNumericalRange.get(i).get(1);

            filternames.get(currentFilterCount).setText(ParameterNumericalNames.get(i));

            seekbars.get(currentFilterCount).setMax((seekBarMax - seekBarMin) / step);

            SeekBarValueMap.put(ParameterNumericalNames.get(i), seekbarvalue);

            seekbars.get(currentFilterCount).setOnSeekBarChangeListener(
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

                            double value = seekBarMin + (progress * step);
                            seekbarvalue.setText(String.valueOf((int) value));
                        }
                    }
            );
            spinners.get(currentFilterCount).setVisibility(View.INVISIBLE);

            currentFilterCount += 1;

        }


        if (currentFilterCount < 3){
            for(int i=currentFilterCount;i<3;i++) {
                spinners.get(i).setVisibility(View.INVISIBLE);
                filternames.get(i).setVisibility(View.INVISIBLE);
                seekbars.get(i).setVisibility(View.INVISIBLE);
            }
        }


        button.setOnClickListener(new View.OnClickListener() {


            // TODO: Turn the json create into more flexible way.

            @Override
            public void onClick(View v) {

                JSONArray paraList = new JSONArray();

                for (int i = 0; i < ParameterEnumNames.size(); i++) {
                    Log.d(TAG, "NAME"+ParameterEnumNames.get(i).toString());

                    String id = FilteridMap.get(ParameterEnumNames.get(i));
                    String value = SpinnerMap.get(ParameterEnumNames.get(i)).getSelectedItem().toString();
                    String disableSearch = "false";

                    if (value.equals("Any")){
                        disableSearch = "true";
                    }

                    JSONObject parameter = new JSONObject();
                    try {
                        parameter.put("disabled", disableSearch);
                        parameter.put("specID", id);
                        parameter.put("value", value);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    paraList.put(parameter);

                }

                for (int i = 0; i < ParameterNumericalNames.size(); i++) {
                    String id = FilteridMap.get(ParameterNumericalNames.get(i));
                    String value = SeekBarValueMap.get(ParameterNumericalNames.get(i)).getText().toString();
                    String disableSearch = "false";

                    if (value.equals("")){
                        disableSearch = "true";
                    }
                    JSONObject parameter = new JSONObject();
                    try {
                        parameter.put("disabled", disableSearch);
                        parameter.put("specID", id);
                        parameter.put("value", value);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    paraList.put(parameter);

                }

                // POST data
                JSON generatedJsonObject;

                generatedJsonObject = JSON.create(
                        JSON.dic(
                                "typeID", Categoryid,
                                "paraList", paraList
                        )
                );

                HttpAsyncTask httpAsyncTask = new HttpAsyncTask(POST_EQUIPSEARCH_API, generatedJsonObject.toString());
                httpAsyncTask.execute(POST_EQUIPSEARCH_API);


                // TODO send result to show showFilterResultActivity
//                Intent i = new Intent(filterEquipmentActivity.this, showFilterResultActivity.class);
//                //i.putExtra("searchResult", searchResult);
//                startActivity(i);
            }
        });


    }




    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        private String url;
        private HttpHandler httpHandler;
        private String postdata;

        public HttpAsyncTask(String url, String postdata) {
            this.url = url;
            this.postdata = postdata;
        }

        @Override
        protected String doInBackground(String... urls) {

            JSON postjson = new JSON(postdata);


            httpHandler = new HttpHandler();
            String jsonStr = httpHandler.makePOSTServiceCall(url, postjson);

            Log.d(TAG, jsonStr);
            return jsonStr;

        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            searchResult = result;

            JSON resultjson = new JSON(result);

            if (resultjson.key("equipList").count() == 0){
                Toast.makeText(getApplicationContext(), "Sorry, No matching results. Please change filters", Toast.LENGTH_LONG).show();
            }else {

                Log.d(TAG, result);
            }

        }
    }





}



