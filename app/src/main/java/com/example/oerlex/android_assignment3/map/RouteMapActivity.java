package com.example.oerlex.android_assignment3.map;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.oerlex.android_assignment3.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RouteMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_map);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        setDownloadURL("odessa");
    }

    @Override
    protected void onResume() {
        super.onResume();

        ((MapFragment) getFragmentManager().findFragmentById(R.id.mapRoute)).getMapAsync(this);
    }

    private void setDownloadURL(String city){
        String urlString;
        URL url;
        switch (city){
            case "stockholm":
                urlString = "http://cs.lnu.se/android/VaxjoToStockholm.kml";
                break;
            case "copenhagen":
                urlString = "http://cs.lnu.se/android/VaxjoToCopenhagen.kml";
                break;
            default:
                urlString = "http://cs.lnu.se/android/VaxjoToOdessa.kml";
        }
        if(!Objects.equals(urlString, "")){
            try {
                url = new URL(urlString);
                new Downloader().execute(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    private class Downloader extends AsyncTask<URL, Void , File>{

        @Override
        protected File doInBackground(URL... params) {
            File route = new File(getFilesDir(), "routes.xml");

            if(route.exists())
                route.delete();

            try {
                URLConnection connection = params[0].openConnection();
                connection.setUseCaches(false);
                connection.connect();
                InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                FileOutputStream fileOutputStream = openFileOutput("routes.xml", Context.MODE_PRIVATE);
                String buffer;
                while ((buffer = bufferedReader.readLine()) != null){
                    fileOutputStream.write(buffer.getBytes());
                }
                fileOutputStream.close();
                bufferedReader.close();
                inputStreamReader.close();
                return route;
            } catch (IOException e) {
                e.printStackTrace();

                return null;
            }
        }


        protected void onPostExecute(File result){
            if (result == null) {
                Toast.makeText(getApplicationContext(), "No file could be retrieved", Toast.LENGTH_LONG).show();
                return;
            }
            List<String> coordinates = new ArrayList<>(3);
            List<String> cityNames = new ArrayList<>(4);
            try {
                InputStream inputStream = new FileInputStream(result);
                XmlPullParser xmlPullParser = Xml.newPullParser();
                xmlPullParser.setInput(inputStream, null);
                while (xmlPullParser.getEventType() != XmlResourceParser.END_DOCUMENT) {
                    if (xmlPullParser.getEventType() == XmlResourceParser.START_TAG) {
                        if (xmlPullParser.getName().equals("coordinates")) {
                            coordinates.add(xmlPullParser.nextText());
                        }
                        if (xmlPullParser.getName().equals("name")) {
                            cityNames.add(xmlPullParser.nextText());
                        }
                    }
                    xmlPullParser.next();
                }
            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
            }

            // get start and end of route
            Route mapsRoute = new Route(cityNames.get(2), cityNames.get(3));
            String startCoordinates[] = coordinates.get(1).split(",");
            String endCoordinates[] = coordinates.get(2).split(",");
            mapsRoute.setStartCoordniates(startCoordinates[1], startCoordinates[0]);
            mapsRoute.setEndCoordninates(endCoordinates[1], endCoordinates[0]);

            // get coordinates of polygon (line) points
            String wayCoordinatesTogether[] = coordinates.get(0).split(" ");
            for (String wayCoordinateTogether : wayCoordinatesTogether) {
                String wayCoordinate[] = wayCoordinateTogether.split(",");
                mapsRoute.addCity(Double.parseDouble(wayCoordinate[1]), Double.parseDouble(wayCoordinate[0]));
            }

            getSupportActionBar().setTitle("Route to " + mapsRoute.getEndCity());
            // draw line and set markers to start and end
            googleMap.clear();
            googleMap.addPolyline(new PolylineOptions().width(5).color(Color.RED).addAll(mapsRoute.getCoordinateList()));

            googleMap.addMarker(new MarkerOptions()
                    .position(mapsRoute.getStartCoordniates())
                    .title("Start: " + mapsRoute.getStartCity())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

            googleMap.addMarker(new MarkerOptions()
                    .position(mapsRoute.getEndCoordninates())
                    .title("End: " + mapsRoute.getEndCity())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            // zoom in to show all markers centered
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (LatLng latLng : mapsRoute.getCoordinateList()) {
                builder.include(latLng);
            }
            LatLngBounds bounds = builder.build();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        }
    }

    /* Add options menu to add new countries and edit settings */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.route_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemOdessa:
                setDownloadURL("odessa");
                return false;
            case R.id.itemCopenhagen:
                setDownloadURL("copenhagen");
                return false;
            case R.id.itemStockholm:
                setDownloadURL("stockholm");
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
