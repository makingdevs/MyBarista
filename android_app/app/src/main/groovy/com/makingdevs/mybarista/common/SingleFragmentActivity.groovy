package com.makingdevs.mybarista.common

import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.service.SessionManager
import com.makingdevs.mybarista.service.SessionManagerImpl
import com.makingdevs.mybarista.ui.activity.ListBrewActivity
import com.makingdevs.mybarista.ui.activity.LoginActivity
import com.makingdevs.mybarista.ui.activity.ProfileActivity
import com.makingdevs.mybarista.ui.activity.SearchUserActivity
import groovy.transform.CompileStatic

@CompileStatic
abstract class SingleFragmentActivity extends AppCompatActivity implements WithFragment {

    SessionManager mSessionManager = SessionManagerImpl.instance

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        FragmentManager fm = getSupportFragmentManager()
        Fragment fragment = fm.findFragmentById(R.id.container)
        if (!fragment)
            fm.beginTransaction().add(R.id.container, createFragment()).commit()
    }

    void createMenu(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar)
        toolbar.setVisibility(View.VISIBLE)
        setSupportActionBar(toolbar)
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu)
        return true
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Integer id = item.getItemId()

        //TODO refactor de esto hay una mejor manera
        if (id == R.id.menu_profile) {
            Intent intent = ProfileActivity.newIntentWithContext(this)
            startActivity(intent)
            finish()
        }
        if(id == R.id.menu_search) {
            Intent intent = SearchUserActivity.newIntentWithContext(this)
            startActivity(intent)
            finish()
        }
        if(id == R.id.menu_logout) {
            mSessionManager.setLogout(this)
            Intent intent = LoginActivity.newIntentWithContext(this)
            startActivity(intent)
            finish()
        }

        super.onOptionsItemSelected(item)
    }

}