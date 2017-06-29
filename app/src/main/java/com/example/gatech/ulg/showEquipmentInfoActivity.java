package com.example.gatech.ulg;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class showEquipmentInfoActivity extends AppCompatActivity {

    private TextView t1, t2;
    private ImageView v1;

    private String name = "Eppendorf 5810R";
    private String ImageURL = "https://online-shop.eppendorf.com/upload/main/products/export-SCREEN-JPG-max1200pxW-96dpi-RGB/std.lang.all/155462.jpg";
    private String description = "Centrifuge 5810R is a workhorse for medium to high-throughput laboratories. It combines extraordinary versatility and capacity for both tubes and plates with an extraordinary compact footprint.";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_equipment_info);

        t1 = (TextView) findViewById(R.id.textView4);
        t2 = (TextView) findViewById(R.id.textView7);
        v1 = (ImageView) findViewById(R.id.imageView);

        t1.setTextColor(Color.parseColor("#0099ff"));

        t1.setText(name);
        t2.setText(description);

        Picasso.with(this).load(ImageURL).fit().into(v1);

    }
}
