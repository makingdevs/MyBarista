package com.makingdevs.mybarista.ui.fragment

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makingdevs.mybarista.R
import groovy.transform.CompileStatic

@CompileStatic
public class CommentsFragment extends Fragment {

    private static final String TAG = "CommentsFragment"

    CommentsFragment(){}

    @Override
    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_comments,container, false)
        root
    }


}
