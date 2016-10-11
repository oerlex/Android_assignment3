package com.example.oerlex.android_assignment3.weather;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.oerlex.android_assignment3.R;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oerlex on 04.10.2016.
 */
public class WeatherRetriever extends Service {
    public int appID;
    private WeatherReport report = null;

    private List<WeatherForecast> forecastList = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) { return null;  }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // get weather data
        appID = intent.getIntExtra("appID", -1);
        String cityURL = intent.getStringExtra("url");
        System.out.println("Service: city url = "+ cityURL);
        if (cityURL != null && !cityURL.isEmpty()) {
            try {
                URL url = new URL(cityURL);
                System.out.println("CITY URL : "+url);
                new WeatherThread().execute(url);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return Service.START_NOT_STICKY;
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
        protected void onPostExecute(WeatherReport report) {
            System.out.println("In the postexecute");
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(WeatherRetriever.this);
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.activity_weather_main);
            WeatherForecast forecast = report.iterator().next();
            views.setTextViewText(R.id.temperature, String.valueOf(forecast.getTemperature()) + "°C");
            //views.setImageViewResource(R.id.imageView, WeatherHandler.getImage(forecast.getWeatherCode(), forecast.getStartHHMM()));

            appWidgetManager.updateAppWidget(appID, views);
        }


    }
}
