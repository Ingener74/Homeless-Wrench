package ru.venusgames.homelesswrench;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by pavel on 25.02.16.
 */
public class MyLocation {
    private LatLng latLng;
    private String name;

    public MyLocation(String name, LatLng latLng) {
        this.name = name;
        this.latLng = latLng;
    }

    public String getName() {
        return name;
    }

    public LatLng getLatLng() {
        return latLng;
    }
}
