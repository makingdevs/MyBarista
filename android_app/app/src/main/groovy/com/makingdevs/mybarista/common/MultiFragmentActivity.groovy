package com.makingdevs.mybarista.common

import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.crashlytics.android.Crashlytics
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.model.User
import com.makingdevs.mybarista.service.SessionManager
import com.makingdevs.mybarista.service.SessionManagerImpl
import com.makingdevs.mybarista.ui.activity.LoginActivity
import com.makingdevs.mybarista.ui.activity.PrincipalActivity
import com.makingdevs.mybarista.ui.fragment.CommentsFragment
import com.makingdevs.mybarista.ui.fragment.RatingCoffeFragment
import com.makingdevs.mybarista.ui.fragment.ShowCheckinFragment
import com.makingdevs.mybarista.ui.fragment.ShowCircleFlavorFragment
import groovy.transform.CompileStatic
import io.fabric.sdk.android.Fabric

@CompileStatic
abstract class MultiFragmentActivity extends AppCompatActivity implements WithMultiFragments {

    SessionManager mSessionManager = SessionManagerImpl.instance

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_multi_fragment)

        Map fragments = createFragments()

        ShowCheckinFragment topFragment= (ShowCheckinFragment) fragments.top
        RatingCoffeFragment middleTopFragment= (RatingCoffeFragment) fragments.middleTop
        ShowCircleFlavorFragment middleBottomFragment= (ShowCircleFlavorFragment) fragments.middleBootom
        CommentsFragment bottomFragment= (CommentsFragment) fragments.bottom

        FragmentManager manager=getSupportFragmentManager()

        FragmentTransaction transaction=manager.beginTransaction()

        transaction.add(R.id.topFragment, topFragment, "topFragment")
        transaction.add(R.id.middleTopFragment, middleTopFragment, "middleFragment")
        transaction.add(R.id.middleBottomFragment, middleBottomFragment, "middleFragment")
        transaction.add(R.id.bottomFragment, bottomFragment, "bottomFragment")

        transaction.commit()
    }

    void createMenu(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar)
        toolbar.setVisibility(View.VISIBLE)
        setSupportActionBar(toolbar)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true)
        getSupportActionBar().setDisplayShowHomeEnabled(true)
        toolbar.navigationOnClickListener = { finish() }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu)
        return true
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Integer id = item.getItemId()

        if (id == R.id.menu_profile) {
            User user = mSessionManager.getUserSession(this)
            Intent intent = PrincipalActivity.newIntentWithContext(this)
            startActivity(intent)
            finish()
        }
        if(id == R.id.menu_search) {
            Intent intent = PrincipalActivity.newIntentWithContext(this)
            startActivity(intent)
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