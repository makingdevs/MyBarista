package com.makingdevs.mybarista.common

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.widget.EditText
import android.widget.Toast

class RequestPermissionAndroid {

    static final int PERMISSIONS_REQUEST_WRITE = 1
    static final int PERMISSIONS_REQUEST_CAMERA = 1

    void checkPermission(Activity activity, String permission) {
        switch (permission) {
            case "camera":
                if (ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
                        createPopMenu(activity,"cámara",[Manifest.permission.CAMERA] as String[])
                    } else {
                        ActivityCompat.requestPermissions(activity, [Manifest.permission.CAMERA] as String[], PERMISSIONS_REQUEST_CAMERA)
                    }
                }
                break
            case "storage":
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        createPopMenu(activity,"almacenamiento",[Manifest.permission.WRITE_EXTERNAL_STORAGE] as String[])
                    } else {
                        ActivityCompat.requestPermissions(activity, [Manifest.permission.WRITE_EXTERNAL_STORAGE] as String[], PERMISSIONS_REQUEST_WRITE)
                    }
                }
                break
            case "location":
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                        && ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        createPopMenu(activity,"localización",[Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION] as String[])
                    } else {
                        ActivityCompat.requestPermissions(activity, [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION] as String[], PERMISSIONS_REQUEST_WRITE)
                    }
                }
                break
        }

    }

    void createPopMenu(Activity activity,String permission,String[] permissionArray){
        final int PERMISSION = 1
        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
        builder.setMessage("¿Quieres permitir que Barista tenga acceso a ${permission}?")
                .setPositiveButton("Negar", new DialogInterface.OnClickListener() {
            void onClick(DialogInterface dialog, int id) {
                Toast.makeText(activity.getApplicationContext(),"Permiso de ${permission} negado",Toast.LENGTH_SHORT).show()
            }
        })
                .setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
            void onClick(DialogInterface dialog, int id) {
                ActivityCompat.requestPermissions(activity, permissionArray , PERMISSION)
            }
        })
        AlertDialog alertDialog = builder.create()
        alertDialog.show()
    }
}