/**
 * WorldWeather.java
 * Created: May 9, 2010
 * Jonas Lundberg, LnU
 */

package com.example.oerlex.android_assignment3.weather;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oerlex.android_assignment3.R;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a first prototype for a weather app. It is currently 
 * only downloading weather data for Växjö. 
 * 
 * This activity downloads weather data and constructs a WeatherReport,
 * a data structure containing weather data for a number of periods ahead.
 * 
 * The WeatherHandler is a SAX parser for the weather reports 
 * (forecast.xml) produced by www.yr.no. The handler constructs
 * a WeatherReport containing meta data for a given location
 * (e.g. city, country, last updated, next update) and a sequence 
 * of WeatherForecasts.
 * Each WeatherForecast represents a forecast (weather, rain, wind, etc)
 * for a given time period.
 * 
 * The next task is to construct a list based GUI where each row 
 * displays the weather data for a single period.
 * 
 *  
 * @author jlnmsi
 *
 */

public class WorldWeather extends AppCompatActivity {
	public static String TAG = "dv606.weather";

	private WeatherReport report = null;
	private WeatherAdapter weatherAdapter;
	private List<WeatherForecast> forecastList = new ArrayList<>();
	private WeatherRetriever service = null;
	TextView placeholder;
	private RecyclerView recyclerView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather_main);
		if(networkCheck()) {
			try {
				URL url = new URL("http://www.yr.no/sted/Sverige/Kronoberg/V%E4xj%F6/forecast.xml");
				AsyncTask task = new WeatherThread().execute(url);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}else {
			Toast.makeText(this, "There is no internet connection available. Please try again later", Toast.LENGTH_SHORT).show();
		}
	}


	//Checks if the device has a connection to the internet
	private boolean networkCheck() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	//Iterates through all forecasts and adds them to the list and updates the GUI
	protected void prepareForecast(){
		for (WeatherForecast forecast : report) {
			forecastList.add(forecast);
			weatherAdapter.notifyDataSetChanged();
		}
	}

	private class WeatherThread extends AsyncTask<URL, Void, WeatherReport> {
    	protected WeatherReport doInBackground(URL... urls) {
    		try {
    			return WeatherHandler.getWeatherReport(urls[0]);
    		} catch (Exception e) {
    			throw new RuntimeException(e);
    		}
    	}

    	protected void onProgressUpdate(Void... progress) {

    	}

		//After fetching the report this method initializes the recyclerView
		//
    	protected void onPostExecute(WeatherReport result) {
			Toast.makeText(getApplicationContext(), "WeatherThread task finished", Toast.LENGTH_LONG).show();
    		report = result;
			RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
			placeholder = (TextView) findViewById(R.id.placeholder);
			placeholder.setText(report.getCity() + "(" + report.getCountry() + ")");

			weatherAdapter = new WeatherAdapter(forecastList);
			RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
			recyclerView.setLayoutManager(mLayoutManager);
			recyclerView.setItemAnimator(new DefaultItemAnimator());
			recyclerView.setAdapter(weatherAdapter);
			prepareForecast();
    	}
    }
}