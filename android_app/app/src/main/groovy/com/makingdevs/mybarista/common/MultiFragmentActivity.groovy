package com.makingdevs.mybarista.common

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.service.SessionManager
import com.makingdevs.mybarista.service.SessionManagerImpl
import com.makingdevs.mybarista.ui.fragment.CommentsFragment
import com.makingdevs.mybarista.ui.fragment.RatingCoffeFragment
import com.makingdevs.mybarista.ui.fragment.ShowCheckinFragment
import com.makingdevs.mybarista.ui.fragment.ShowCircleFlavorFragment
import groovy.transform.CompileStatic

@CompileStatic
abstract class MultiFragmentActivity extends AppCompatActivity implements WithMultiFragments {

    SessionManager mSessionManager = SessionManagerImpl.instance

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
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

}