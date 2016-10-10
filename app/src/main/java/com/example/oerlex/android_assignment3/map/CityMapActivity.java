package com.example.oerlex.android_assignment3.map;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.oerlex.android_assignment3.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class CityMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ArrayList<String> allCities = new ArrayList<>();
    private HashMap<String, String> markerCity = new HashMap<>();
    private Toast distanceToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_map);
        try {
            allCities = getAllCities();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> getAllCities() throws IOException {
        ArrayList<String> cities = new ArrayList<>();
        InputStream inputStream = getResources().openRawResource(R.raw.cities);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String item;
        while(true){
            item = bufferedReader.readLine();
            if(item != null){
                cities.add(item);
            }else{
                break;
            }
        }
        return cities;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        final ArrayList<Marker> markers = new ArrayList<>();

        for(String city : allCities){
            String[] cityAttributes = city.split(";");
            Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng( Double.valueOf(cityAttributes[1]), Double.valueOf(cityAttributes[2]))));
            markerCity.put(marker.getId(),cityAttributes[0]);
            markers.add(marker);
        }

        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                LatLngBounds.Builder latlngBuilder = new LatLngBounds.Builder();
                for (Marker marker : markers){
                    latlngBuilder.include(marker.getPosition());
                }
                LatLngBounds bounds = latlngBuilder.build();
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,100));
            }
        });

        googleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {

                String sCity = "";
                float sWay = Float.MAX_VALUE;
                float distances[] = new float[1];
                LatLng latLng =   googleMap.getCameraPosition().target;;
                double lat = latLng.latitude;
                double lng = latLng.longitude;

                for(String city : allCities){
                    String[] cityAtt =  city.split(";");
                    Location.distanceBetween(lat,lng, Double.valueOf(cityAtt[1]), Double.valueOf(cityAtt[2]), distances);
                    if(distances[0] < sWay){
                        sWay = distances[0];
                        sCity = cityAtt[0];
                    }
                }

                sWay = sWay / 1000;
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                String dong = decimalFormat.format(sWay);
                if(dong.contains(","))
                   dong = dong.replaceAll(",",".");
                sWay = Float.valueOf(dong);

                if (distanceToast != null) distanceToast.cancel();
                distanceToast = Toast.makeText(CityMapActivity.this, "The closest city is" + sCity + ", " + sWay + "km", Toast.LENGTH_SHORT);
                distanceToast.show();
            }
        });
    }
}
