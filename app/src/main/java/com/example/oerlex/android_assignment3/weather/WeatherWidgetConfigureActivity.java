package com.example.oerlex.android_assignment3.weather;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.oerlex.android_assignment3.R;

import java.util.ArrayList;
import java.util.List;


/**
 * The configuration screen for the {@link WeatherWidget WeatherWidget} AppWidget.
 */


public class WeatherWidgetConfigureActivity extends Activity {

    public String weatherURL="";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private String place;
    private WeatherRetriever weatherRetriever;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);
        setContentView(R.layout.weather_widget_configure);
        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = WeatherWidgetConfigureActivity.this;
            // It is the responsibility of the configuration activity to update the app widget

            RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radioButtonGroup);
            if (radioGroup.getCheckedRadioButtonId() == -1){
                Toast.makeText(getApplicationContext(), "Select a city you fool", Toast.LENGTH_SHORT).show();
            }else{
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = (RadioButton)findViewById(selectedId);

                switch (selectedRadioButton.getId()) {
                    case R.id.vaxjo:
                        weatherURL = "http://www.yr.no/sted/Sverige/Kronoberg/V%E4xj%F6/forecast.xml";
                        place = "vaxjo";
                        break;
                    case R.id.hamburg:
                        weatherURL = "http://www.yr.no/sted/Tyskland/Hamburg/Hamburg/forecast.xml";
                        place = "hamburg";
                        break;
                    case R.id.tokyo:
                        weatherURL = "http://www.yr.no/sted/Japan/Tokyo/Tokyo/forecast.xml";
                        place = "tokyo";
                        break;
                    case R.id.ibiza:
                        weatherURL = "http://www.yr.no/sted/Spania/Balearene/Ibiza/forecast.xml";
                        place = "ibiza";
                        break;
                }
            }

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("city_" + mAppWidgetId, place);
            editor.putString("url_" + mAppWidgetId, weatherURL);
            System.out.println("1.PutString (city_"+mAppWidgetId+" with value "+place);
            System.out.println("2.PutString (url_"+mAppWidgetId+" with value "+weatherURL);
            editor.apply();


            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
/*
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
            views.setTextViewText(R.id.textviewName, place);*/

           /* // open weather app on click
            Intent widgetClickIntent = new Intent(context, WorldWeather.class);
            widgetClickIntent.putExtra("city", place);
            widgetClickIntent.putExtra("url",weatherURL);
            widgetClickIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntentWidget = PendingIntent.getActivity(context, mAppWidgetId, widgetClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.backgroundTextView, pendingIntentWidget);

            // refresh on update button click
            Intent btnClickIntent = new Intent(context, WeatherWidget.class);
            btnClickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            btnClickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{mAppWidgetId});
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, mAppWidgetId, btnClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.btnUpdate, pendingIntent);
*/

            new WeatherWidget().onUpdate(context,appWidgetManager,new int[]{mAppWidgetId});
            //appWidgetManager.updateAppWidget(mAppWidgetId, views);

            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();

          /*  Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);

            Intent intent = new Intent(context,WeatherWidget.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            sendBroadcast(intent);

            System.out.println("ID : "+mAppWidgetId);
            finish();*/

        }
    };
}

