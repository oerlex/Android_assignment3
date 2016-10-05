package com.example.oerlex.android_assignment3.weather;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oerlex.android_assignment3.R;

import java.util.List;

/**
 * Created by Oerlex on 31.08.2016.
 */
public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.MyViewHolder> {

    private List<WeatherForecast> weatherForecastList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView temperature, windspeed, time, date, rain;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            temperature = (TextView) view.findViewById(R.id.temperature);
            windspeed = (TextView) view.findViewById(R.id.windspeed);
            time = (TextView) view.findViewById(R.id.time);
            date = (TextView) view.findViewById(R.id.date);
            rain = (TextView) view.findViewById(R.id.rain);
            image = (ImageView) view.findViewById(R.id.image);
        }
    }
    public WeatherAdapter(List<WeatherForecast> forecastList) {
        this.weatherForecastList = forecastList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_weather_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        WeatherForecast weatherForecast = weatherForecastList.get(position);

        holder.temperature.setText(String.valueOf(weatherForecast.getTemperature())+"°");
        holder.windspeed.setText(String.valueOf(weatherForecast.getWindSpeed())+"m/s");
        holder.time.setText(weatherForecast.getStartHHMM()+"-"+weatherForecast.getEndHHMM());
        holder.date.setText(weatherForecast.getStartYYMMDD());
        holder.rain.setText(String.valueOf(weatherForecast.getRain())+"mm/h");

        int weatherCode = weatherForecast.getWeatherCode();
        if(weatherCode==1){
            holder.image.setImageResource(R.drawable.sun);
        }else if(weatherCode==2 || weatherCode==3){
            holder.image.setImageResource(R.drawable.cloudy_1);
        }else if(weatherCode==4){
            holder.image.setImageResource(R.drawable.cloudy);
        }else if(weatherCode==40 || weatherCode==5 || weatherCode==41 || weatherCode==24){
            holder.image.setImageResource(R.drawable.rain_1);
        }else{
            holder.image.setImageResource(R.drawable.cloudy);
        }
    }


    @Override
    public int getItemCount() {
        return weatherForecastList.size();
    }
}


