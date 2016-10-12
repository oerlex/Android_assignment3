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



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        counter++;
        System.out.println("Update Nr. "+counter);

        // Construct the RemoteViews object
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String city_name = preferences.getString("city_" + appWidgetId, "");
            String url_city = preferences.getString("url_"+ appWidgetId,"");

            // open weather app on click
            Intent openAppIntent = new Intent(context, WorldWeather.class);
            openAppIntent.putExtra("city", city_name);
            openAppIntent.putExtra("url", url_city);
            openAppIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntentWidget = PendingIntent.getActivity(context, appWidgetId, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.backgroundTextView, pendingIntentWidget);

            // refresh on update button click
            Intent refreshIntent = new Intent(context, WeatherRetriever.class);
            refreshIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            refreshIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{appWidgetId});
            refreshIntent.putExtra("url", url_city);
            refreshIntent.putExtra("city", city_name);
            refreshIntent.putExtra("appID", appWidgetId);
            PendingIntent pendingIntent = PendingIntent.getService(context, appWidgetId, refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.btnUpdate, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        for (int appWidgetId : appWidgetIds) {
            editor.remove("city_" + appWidgetId);
        }
        editor.apply();
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

