package com.example.oerlex.android_assignment3;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.oerlex.android_assignment3.map.CityMapActivity;
import com.example.oerlex.android_assignment3.map.RouteMapActivity;
import com.example.oerlex.android_assignment3.phone.PhoneActivity;
import com.example.oerlex.android_assignment3.weather.WorldWeather;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void openWeatherIntent(View view){
        Intent weatherIntent = new Intent(this, WorldWeather.class);
        startActivity(weatherIntent);
    }

    public void openPhoneIntent(View view){
        Intent phoneIntent = new Intent(this, PhoneActivity.class);
        startActivity(phoneIntent);
    }

    public void openCityIntent(View view){
        Intent cityIntent = new Intent(this, CityMapActivity.class);
        startActivity(cityIntent);
    }

    public void openRouteIntent(View view){
        Intent routeIntent = new Intent(this, RouteMapActivity.class);
        startActivity(routeIntent);
    }
}
