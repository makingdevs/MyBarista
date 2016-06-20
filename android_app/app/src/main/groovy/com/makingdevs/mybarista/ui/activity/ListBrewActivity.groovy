package com.makingdevs.mybarista.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuItem
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.common.SingleFragmentActivity
import com.makingdevs.mybarista.ui.fragment.ListBrewFragment
import groovy.transform.CompileStatic

@CompileStatic
public class ListBrewActivity extends SingleFragmentActivity {

    static Intent newIntentWithContext(Context context){
        Intent intent = new Intent(context, ListBrewActivity)
        intent
    }

    @Override
    Fragment createFragment() {
        new ListBrewFragment()
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setActivityToolbarVisible(true)
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
        }
        if(id == R.id.menu_search) {
            Intent intent = SearchUserActivity.newIntentWithContext(this)
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
