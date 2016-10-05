package com.example.oerlex.android_assignment3.weather;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.example.oerlex.android_assignment3.R;

import java.util.Map;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link weather_widgetConfigureActivity weather_widgetConfigureActivity}
 */
public class weather_widget extends AppWidgetProvider {

    Map<String, String> citiesMap = WeatherHandler.getCities2Url();

    public void updateWeather(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        CharSequence widgetText = weather_widgetConfigureActivity.loadTitlePref(context, appWidgetIds);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);

        for (int appWidgetId : appWidgetIds) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String city_name = preferences.getString("city_" + appWidgetId, "");
            String url_city = citiesMap.get(city_name);
            System.out.println("city: " + city_name);
            System.out.println("city url: " + url_city);
            System.out.println("widget id: " + appWidgetId);

            if (url_city != null && !url_city.isEmpty()) {
                // start service to load weather data
                Intent startServiceIntent = new Intent(context, WeatherRetriever.class);
                startServiceIntent.putExtra("URL", url_city);
                startServiceIntent.putExtra("appID", appWidgetId);
                startServiceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startService(startServiceIntent);
                System.out.println("called new service");
            }


            // open weather app on click
            Intent widgetClickIntent = new Intent(context, WorldWeather.class);
            widgetClickIntent.putExtra("city", city_name);
            widgetClickIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntentWidget = PendingIntent.getActivity(context, appWidgetId, widgetClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.backgroundTextView, pendingIntentWidget);

            // refresh on update button click
            Intent btnClickIntent = new Intent(context, weather_widget.class);
            btnClickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            btnClickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{appWidgetId});
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, btnClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.btn_widget_update, pendingIntent);

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
            weather_widgetConfigureActivity.deleteTitlePref(context, appWidgetId);
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

}

