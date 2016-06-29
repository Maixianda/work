package com.gzliangce.util;

import android.location.Location;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.model.LatLng;


/**
 * 地图辅助类
 * Created by Roger on 1/20/16.
 */
public class MapUtils {

    public static LatLng getLatLng(AMapLocation location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    public static LatLng getLatLng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    public static LatLng getLatLng(float lat, float lng) {
        return new LatLng(lat, lng);
    }

    public static LatLng getLatLng(double lat, double lng) {
        return new LatLng(lat, lng);
    }

}
