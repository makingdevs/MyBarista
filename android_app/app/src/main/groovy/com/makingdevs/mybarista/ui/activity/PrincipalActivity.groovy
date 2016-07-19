package com.makingdevs.mybarista.ui.activity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.widget.Toast
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.makingdevs.mybarista.BuildConfig
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.common.RequestPermissionAndroid
import com.makingdevs.mybarista.ui.fragment.ListBrewFragment
import com.makingdevs.mybarista.ui.fragment.ProfileFragment
import com.makingdevs.mybarista.ui.fragment.SearchUserFragment
import groovy.transform.CompileStatic
import io.fabric.sdk.android.Fabric

import static com.makingdevs.mybarista.common.RequestPermissionAndroid.*

@CompileStatic
class PrincipalActivity extends AppCompatActivity {

    static Intent newIntentWithContext(Context context) {
        Intent intent = new Intent(context, PrincipalActivity)
        intent
    }

    RequestPermissionAndroid requestPermissionAndroid = new RequestPermissionAndroid()

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        CrashlyticsCore core = new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()
        Fabric.with(this, new Crashlytics.Builder().core(core).build())
        setContentView(R.layout.activity_principal)

        FragmentManager fm = getSupportFragmentManager()
        Fragment fragment = fm.findFragmentById(R.id.container)
        if (!fragment)
            fm.beginTransaction().add(R.id.container, new ListBrewFragment()).commit()

        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Check-ins", R.drawable.ic_foursquar)
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Busqueda", R.drawable.ic_search)
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Perfil", R.drawable.ic_profile)

        bottomNavigation.addItem(item1)
        bottomNavigation.addItem(item2)
        bottomNavigation.addItem(item3)

        bottomNavigation.setAccentColor(Color.parseColor("#0288D1"))
        bottomNavigation.setInactiveColor(Color.parseColor("#727272"))

        bottomNavigation.onTabSelectedListener = { int position, boolean wasSelected ->
            //TODO: cambiar a meotodo newInstance en todos los fragmentos invocados
            if (position == 0 && !wasSelected){
                changeFragment(new ListBrewFragment())
            }
            if (position == 1 && !wasSelected){
                changeFragment(new SearchUserFragment())
            }
            if (position == 2 && !wasSelected){
                requestPermissionAndroid.checkPermission(this,"storage")
                changeFragment(new ProfileFragment())
            }
            true
        }
    }

    private void changeFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            moveTaskToBack(true)
            return true
        }
        return super.onKeyDown(keyCode, event)
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