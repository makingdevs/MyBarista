package com.makingdevs.mybarista.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.ui.fragment.ListBrewFragment
import com.makingdevs.mybarista.ui.fragment.ProfileFragment
import com.makingdevs.mybarista.ui.fragment.SearchUserFragment

class PrincipalActivity extends AppCompatActivity {

    static Intent newIntentWithContext(Context context) {
        Intent intent = new Intent(context, BaristaActivity)
        intent
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        FragmentManager fm = getSupportFragmentManager()
        Fragment fragment = fm.findFragmentById(R.id.container)
        if (!fragment)
            fm.beginTransaction().add(R.id.container, new ListBrewFragment()).commit()

        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Check-ins", R.drawable.ic_foursquar, R.color.primary)
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Busqueda", R.drawable.ic_search, R.color.primary)
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Perfil", R.drawable.ic_profile, R.color.primary)

        bottomNavigation.addItem(item1)
        bottomNavigation.addItem(item2)
        bottomNavigation.addItem(item3)

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
}