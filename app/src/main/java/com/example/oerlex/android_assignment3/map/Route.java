package com.example.oerlex.android_assignment3.map;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by oerlex on 07.10.2016.
 */
public class Route {
    private String startCity;
    private String endCity;
    private LatLng startCoordniates;
    private LatLng endCoordninates;
    private ArrayList<LatLng> coordinateList;


    public Route(String start, String end){
        startCity = start;
        endCity = end;
        coordinateList = new ArrayList<>();
    }

    public void addCity(double lat, double lng){
        coordinateList.add(new LatLng(lat,lng));
    }

    public String getStartCity() {
        return startCity;
    }

    public String getEndCity() {
        return endCity;
    }

    public LatLng getStartCoordniates() {
        return startCoordniates;
    }

    public LatLng getEndCoordninates() {
        return endCoordninates;
    }


    public void setStartCoordniates(String startCityLat, String startCityLng) {
        this.startCoordniates = new LatLng(Double.parseDouble(startCityLat), Double.parseDouble(startCityLng));
    }

    public void setEndCoordninates(String endCityLat, String endCityLng) {
        this.endCoordninates = new LatLng(Double.parseDouble(endCityLat), Double.parseDouble(endCityLng));;
    }

    public ArrayList<LatLng> getCoordinateList() {
        return coordinateList;
    }

    public void setCoordinateList(ArrayList<LatLng> coordinateList) {
        this.coordinateList = coordinateList;
    }
}
