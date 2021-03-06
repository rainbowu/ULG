package com.example.gatech.ulg;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


public class BaseActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.schedule:
                        Intent intent_schedule = new Intent(getApplicationContext(), CurScheduleActivity.class);
                        startActivity(intent_schedule);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.ingroup:
                        Intent intent_incategory = new Intent(getApplicationContext(), showEquipmentCategory.class);
                        startActivity(intent_incategory);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.outgroup:
                        Intent intent_outcategor = new Intent(getApplicationContext(), showEquipmentCategory.class);
                        startActivity(intent_outcategor);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.setting:
                        Intent intent_setting = new Intent(getApplicationContext(), SettingActivity.class);
                        startActivity(intent_setting);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.logout:
                        Intent anIntent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(anIntent);
                        drawerLayout.closeDrawers();
                        HttpHandler.isLogged = false;
                        finish();
                        break;

                }
                return false;
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        actionBarDrawerToggle.syncState();
    }

}