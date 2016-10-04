package com.makingdevs.mybarista.ui.activity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.annotation.Nullable
import android.widget.Toast
import com.makingdevs.mybarista.common.MultiFragmentActivity
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.ui.fragment.CommentsFragment
import com.makingdevs.mybarista.ui.fragment.RatingCoffeeFragment
import com.makingdevs.mybarista.ui.fragment.ShowCheckinFragment
import com.makingdevs.mybarista.ui.fragment.ShowCircleFlavorFragment
import groovy.transform.CompileStatic
import groovy.transform.TypeChecked

import static com.makingdevs.mybarista.common.RequestPermissionAndroid.*

@CompileStatic
public class ShowCheckinActivity extends MultiFragmentActivity {

    static String CHECK_IN_ID = "check_in_id"
    static String CIRCLE_FLAVOR_ID = "circle_flavor_id"
    static String CURRENT_CHECK_IN = "check_in"
    private static final String TAG = "ShowCheckInActivity"

    static Intent newIntentWithContext(Context context, Checkin checkin) {
        Intent intent = new Intent(context, ShowCheckinActivity)
        intent.putExtra(CHECK_IN_ID, checkin.id)
        intent.putExtra(CIRCLE_FLAVOR_ID, checkin.circle_flavor_id)
        intent.putExtra(CURRENT_CHECK_IN, checkin)
        intent
    }

    @TypeChecked
    Map createFragments() {
        if (!getIntent().extras.getString(CHECK_IN_ID)) throw new IllegalArgumentException("El checkin no tiene ID, mmmm tamales!")
        //TODO: cambiar a meotodo newInstance en los frgamentos utilizados
        [top: new ShowCheckinFragment(), middleTop: new RatingCoffeeFragment(), middleBootom: new ShowCircleFlavorFragment(), bottom: new CommentsFragment()]
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager?.fragments?.each {
            it?.onActivityResult(requestCode, resultCode, data)
        }
    }
}