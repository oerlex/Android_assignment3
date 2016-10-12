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
    private WeatherAdapter weatherAdapter;
    private String city;

    private List<WeatherForecast> forecastList = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) { return null;  }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // get weather data
        appID = intent.getIntExtra("appID", -1);
        city = intent.getStringExtra("city");
        String cityURL = intent.getStringExtra("url");
        if (cityURL != null && !cityURL.isEmpty()) {
            try {
                URL url = new URL(cityURL);
                new WeatherThread().execute(url);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return Service.START_NOT_STICKY;
    }

  public class WeatherThread extends AsyncTask<URL, Void, WeatherReport> {
        protected WeatherReport doInBackground(URL... urls) {
            try {
                return WeatherHandler.getWeatherReport(urls[0],city);
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
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.weather_widget);
            WeatherForecast forecast = report.iterator().next();
            views.setTextViewText(R.id.textviewTemperature, String.valueOf(forecast.getTemperature()) + "°C");
            int weatherCode = setWidgetImage(forecast);
            views.setImageViewResource(R.id.imageviewIcon, weatherCode);
            views.setTextViewText(R.id.textviewName, report.getCity());
            views.setTextViewText(R.id.textviewWindSpeed, String.valueOf(forecast.getWindSpeed())+"m/s");
            appWidgetManager.updateAppWidget(appID, views);
        }



      protected int setWidgetImage(WeatherForecast forecast){
          int weatherCode = forecast.getWeatherCode();
          if(weatherCode==1){
             return R.drawable.sun;
          }else if(weatherCode==2 || weatherCode==3){
              return R.drawable.cloudy_1;
          }else if(weatherCode==4){
              return R.drawable.cloudy;
          }else if(weatherCode==40 || weatherCode==5 || weatherCode==41 || weatherCode==24){
             return R.drawable.rain_1;
          }else{
              return R.drawable.cloudy;
          }
      }
    }
}
