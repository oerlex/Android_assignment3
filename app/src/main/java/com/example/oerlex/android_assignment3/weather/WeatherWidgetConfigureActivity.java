package com.example.oerlex.android_assignment3.weather;

import android.app.Activity;
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
 * The configuration screen for the {@link WeatherWidget WeatherWidget} AppWidget.
 */


public class WeatherWidgetConfigureActivity extends Activity {

    public String weatherURL="";
    private static final String PREFS_NAME = "com.example.oerlex.android_assignment3.weather.WeatherWidget";
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
            editor.putString("url_"+mAppWidgetId, weatherURL);
            System.out.println("1.PutString (city_"+mAppWidgetId+" with value "+place);
            System.out.println("2.PutString (url_"+mAppWidgetId+" with value "+weatherURL);
            editor.apply();

            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);

            Intent intent = new Intent(context,WeatherWidget.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            sendBroadcast(intent);

            System.out.println("ID : "+mAppWidgetId);
            finish();

        }
    };



    public WeatherWidgetConfigureActivity() {
        super();
    }

}

