package com.makingdevs.mybarista.common

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

class RequestPermissionAndroid{

    static final int PERMISSIONS_REQUEST_WRITE = 1
    static final int PERMISSIONS_REQUEST_CAMERA = 2
    static final int PERMISSIONS_REQUEST_LOCATION = 3

    RequestPermissionAndroid() { super() }

    void checkPermission(Activity activity, String permission) {
        switch (permission) {
            case "camera":
                if (ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
                        ActivityCompat.requestPermissions(activity, [Manifest.permission.CAMERA] as String[], PERMISSIONS_REQUEST_CAMERA)
                    } else {
                        ActivityCompat.requestPermissions(activity, [Manifest.permission.CAMERA] as String[], PERMISSIONS_REQUEST_CAMERA)
                    }
                }
                break
            case "storage":

                if (ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        ActivityCompat.requestPermissions(activity,[Manifest.permission.WRITE_EXTERNAL_STORAGE] as String[], PERMISSIONS_REQUEST_WRITE)
                    } else {
                        ActivityCompat.requestPermissions(activity,[Manifest.permission.WRITE_EXTERNAL_STORAGE] as String[], PERMISSIONS_REQUEST_WRITE)
                    }
                }
                break
            case "location":
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                        && ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        ActivityCompat.requestPermissions(activity, [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION] as String[], PERMISSIONS_REQUEST_LOCATION)
                    } else {
                        ActivityCompat.requestPermissions(activity, [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION] as String[], PERMISSIONS_REQUEST_LOCATION)
                    }
                }
                break
        }

    }

}