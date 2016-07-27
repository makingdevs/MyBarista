package com.makingdevs.mybarista.view

import android.R
import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View

class LoadingDialog extends DialogFragment {

    static final String TITLE = "title"
    static final String TITLE_RES_ID = "title_res_id"

    LoadingDialog newInstance(String title) {
        Bundle args = new Bundle()
        args.putString(TITLE, title)

        LoadingDialog loadingDialog = new LoadingDialog()
        loadingDialog.arguments = args

        loadingDialog
    }

    @Override
    Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState)

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null)

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
        builder.setView(view)

        builder.create()
    }

    CharSequence getTitle() {
        arguments.getString(TITLE)
    }
}