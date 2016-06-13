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
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.common.MultiFragmentActivity
import com.makingdevs.mybarista.common.SingleFragmentActivity
import com.makingdevs.mybarista.ui.fragment.CommentsFragment
import com.makingdevs.mybarista.ui.fragment.RatingCoffeFragment
import com.makingdevs.mybarista.ui.fragment.ShowCheckinFragment
import com.makingdevs.mybarista.ui.fragment.ShowCircleFlavorFragment
import groovy.transform.CompileStatic

@CompileStatic
public class ShowCheckinActivity extends MultiFragmentActivity {

    static String EXTRA_CHECKIN_ID = "checkin_id"
    static String EXTRA_CIRCLE_FLAVOR_ID = "circle_flavor_id"
    private static final String TAG = "ShowCheckinActivity"


    static Intent newIntentWithContext(Context context, String id,String idCircleFlavor){
        Intent intent = new Intent(context, ShowCheckinActivity)
        intent.putExtra(EXTRA_CHECKIN_ID, id)
        intent.putExtra(EXTRA_CIRCLE_FLAVOR_ID,idCircleFlavor)
        intent
    }

    Map createFragments(){
        String id = getIntent()?.extras.getSerializable(EXTRA_CHECKIN_ID)
        String idCircleFlavor =  getIntent()?.extras.getSerializable(EXTRA_CIRCLE_FLAVOR_ID)
        if(!id) throw new IllegalArgumentException("El checkin no tiene ID, mmmm tamales!")
        [top:new ShowCheckinFragment(id), middleTop:new RatingCoffeFragment(id), middleBootom: new ShowCircleFlavorFragment(idCircleFlavor), bottom:new CommentsFragment(id)]
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        createMenu()
    }
}