package com.example.gatech.ulg;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import eu.amirs.JSON;

public class showEquipmentInfoActivity extends BaseActivity {

    private String POST_SHOWEVENT_API = "https://unitedlab-171401.appspot.com/Eventlist/"; // <equipid>/

    private TextView _name, _location, _reflink, _brand, _description;
    private ImageView imageView;
    private Button _reserve;

    private String ImageURL = "https://online-shop.eppendorf.com/upload/main/products/export-SCREEN-JPG-max1200pxW-96dpi-RGB/std.lang.all/155462.jpg";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_show_equipment_info, contentFrameLayout);

        Bundle bundle = getIntent().getExtras();
        String equipmentJSONstr = bundle.getString("equipmentJSONstr");

        JSON equipmentJSON = new JSON(equipmentJSONstr);

        _name = (TextView) findViewById(R.id.name);
        _location = (TextView) findViewById(R.id.Location);
        _reflink = (TextView) findViewById(R.id.ReferenceLink);
        _brand = (TextView) findViewById(R.id.Brand);
        _description = (TextView) findViewById(R.id.Description);
        _reserve = (Button) findViewById(R.id.booking);

        imageView = (ImageView) findViewById(R.id.imageView);

        _name.setTextColor(Color.parseColor("#0099ff"));
        _name.setText(equipmentJSON.key("equipType").toString());

        _location.setText("location:" + equipmentJSON.key("location").toString());
        _reflink.setText("linkurl:" + equipmentJSON.key("linkurl").toString());
        _brand.setText("brand: " + equipmentJSON.key("brand").toString());
        _description.setText("description:" + equipmentJSON.key("description").toString());

        if(!equipmentJSON.key("imgurl").toString().equals(""))
            ImageURL = equipmentJSON.key("imgurl").toString();

        Picasso.with(this).load(ImageURL).fit().into(imageView);


        _reserve.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



            }
        });


    }
}
