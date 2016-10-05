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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.oerlex.android_assignment3.R;


/**
 * The configuration screen for the {@link weather_widget weather_widget} AppWidget.
 */


public class weather_widgetConfigureActivity extends Activity {

    public String weatherURL="";
    private static final String PREFS_NAME = "com.example.oerlex.android_assignment3.weather.weather_widget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
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
            final Context context = weather_widgetConfigureActivity.this;
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
            editor.putString("URL", weatherURL);
            System.out.println("Create new widget for: " + place + " with id: " + mAppWidgetId);
            editor.apply();

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
            views.setTextViewText(R.id.tv_widget_city, place);

            // open weather app on click
            Intent widgetClickIntent = new Intent(context, WorldWeather.class);
            widgetClickIntent.putExtra("city", place);
            widgetClickIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntentWidget = PendingIntent.getActivity(context, mAppWidgetId, widgetClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.backgroundTextView, pendingIntentWidget);

            // refresh on update button click
            Intent btnClickIntent = new Intent(context, weather_widget.class);
            btnClickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            btnClickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{mAppWidgetId});
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, mAppWidgetId, btnClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.btn_widget_update, pendingIntent);

            appWidgetManager.updateAppWidget(mAppWidgetId, views);

            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public weather_widgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int[] appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

}

