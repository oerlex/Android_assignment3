package com.example.oerlex.android_assignment3.weather;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.RemoteViews;

import com.example.oerlex.android_assignment3.R;

import java.util.Map;
import java.util.Objects;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link WeatherWidgetConfigureActivity WeatherWidgetConfigureActivity}
 */
public class WeatherWidget extends AppWidgetProvider {

    int counter = 0;
    RemoteViews views;

    public void updateWeather(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        counter++;
        System.out.println("Update Nr. "+counter);

        // Construct the RemoteViews object
        views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);

        for (int appWidgetId : appWidgetIds) {
            System.out.println("ID : "+appWidgetId);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String city_name = preferences.getString("city_" + appWidgetId, "");
            String url_city = preferences.getString("url_"+ appWidgetId,"");
            System.out.println("1.GetString (city_"+appWidgetId+" with value "+city_name);
            System.out.println("2.GetString (url_"+appWidgetId+" with value "+url_city);

            System.out.println("About to initialize the buttons");

            // open weather app on click
            Intent openAppIntent = new Intent(context, WorldWeather.class);
            openAppIntent.putExtra("city", city_name);
            openAppIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntentWidget = PendingIntent.getActivity(context, appWidgetId, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.backgroundTextView, pendingIntentWidget);

            // refresh on update button click
            Intent refreshIntent = new Intent(context, WeatherWidget.class);
            refreshIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            refreshIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{appWidgetId});
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.btnUpdate, pendingIntent);

            if (!Objects.equals(url_city, "")) {
                System.out.println("STARTING A NEW SERVICE");
                // start service to load weather data
                Intent retrieverIntent = new Intent(context, WeatherRetriever.class);
                retrieverIntent.putExtra("URL", url_city);
                retrieverIntent.putExtra("appID", appWidgetId);
                retrieverIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startService(retrieverIntent);
                System.out.println("called new service");
            }

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        updateWeather(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)){
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            appWidgetManager.updateAppWidget(new ComponentName(context.getPackageName(), this.getClass().getName()), views);
        }
    }
}

