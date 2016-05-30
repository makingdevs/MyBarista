package com.makingdevs.mybarista.ui.fragment

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makingdevs.mybarista.R


class ProfileFragment extends Fragment{

    ProfileFragment(){}

    View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        //validateHasUserSession()
        View root = inflater.inflate(R.layout.fragment_profile, container, false)
        root
    }
}