package com.makingdevs.mybarista.ui.activity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.app.Fragment
import android.widget.Toast
import com.makingdevs.mybarista.common.SingleFragmentActivity
import com.makingdevs.mybarista.ui.fragment.BaristaFragment
import groovy.transform.CompileStatic

import static com.makingdevs.mybarista.common.RequestPermissionAndroid.*

@CompileStatic
class BaristaActivity extends SingleFragmentActivity{

    static Intent newIntentWithContext(Context context) {
        Intent intent = new Intent(context, BaristaActivity)
        intent
    }

    @Override
    Fragment createFragment() {
        //TODO: cambiar a meotodo newInstance
        new BaristaFragment()
    }

    @Override
    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
                }
                return;
                break
            case PERMISSIONS_REQUEST_WRITE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
                }
                return;
                break
            case PERMISSIONS_REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
                }
                return;
                break

        }
    }
}