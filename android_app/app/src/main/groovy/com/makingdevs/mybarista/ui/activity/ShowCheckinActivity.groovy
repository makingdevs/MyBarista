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
import com.makingdevs.mybarista.ui.fragment.RatingCoffeFragment
import com.makingdevs.mybarista.ui.fragment.ShowCheckinFragment
import com.makingdevs.mybarista.ui.fragment.ShowCircleFlavorFragment
import groovy.transform.CompileStatic
import groovy.transform.TypeChecked

import static com.makingdevs.mybarista.common.RequestPermissionAndroid.*

@CompileStatic
public class ShowCheckinActivity extends MultiFragmentActivity {

    static String EXTRA_CHECKIN_ID = "checkin_id"
    static String EXTRA_CIRCLE_FLAVOR_ID = "circle_flavor_id"
    static String EXTRA_CURRENT_CHECKIN = "checkin"
    private static final String TAG = "ShowCheckinActivity"

    static Intent newIntentWithContext(Context context, Checkin checkin) {
        Intent intent = new Intent(context, ShowCheckinActivity)
        intent.putExtra(EXTRA_CHECKIN_ID, checkin.id)
        intent.putExtra(EXTRA_CIRCLE_FLAVOR_ID, checkin.circle_flavor_id)
        intent.putExtra(EXTRA_CURRENT_CHECKIN, checkin)
        intent
    }

    @TypeChecked
    Map createFragments() {
        if (!getIntent().extras.getString(EXTRA_CHECKIN_ID)) throw new IllegalArgumentException("El checkin no tiene ID, mmmm tamales!")
        //TODO: cambiar a meotodo newInstance en los frgamentos utilizados
        [top: new ShowCheckinFragment(), middleTop: new RatingCoffeFragment(), middleBootom: new ShowCircleFlavorFragment(), bottom: new CommentsFragment()]
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