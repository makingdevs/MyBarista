package com.makingdevs.mybarista.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.crashlytics.android.Crashlytics
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.ui.fragment.ListBrewFragment
import com.makingdevs.mybarista.ui.fragment.ProfileFragment
import com.makingdevs.mybarista.ui.fragment.SearchUserFragment
import io.fabric.sdk.android.Fabric

class PrincipalActivity extends AppCompatActivity {

    static Intent newIntentWithContext(Context context) {
        Intent intent = new Intent(context, PrincipalActivity)
        intent
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, new Crashlytics());
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
            if (position == 0 && !wasSelected){
                changeFragment(new ListBrewFragment())
            }
            if (position == 1 && !wasSelected){
                changeFragment(new SearchUserFragment())
            }
            if (position == 2 && !wasSelected){
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
}