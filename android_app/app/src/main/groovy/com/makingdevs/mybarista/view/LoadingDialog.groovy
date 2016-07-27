package com.makingdevs.mybarista.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.makingdevs.mybarista.R
import groovy.transform.CompileStatic

@CompileStatic
class LoadingDialog extends DialogFragment {

    LoadingDialog() {}

    static final String MSG_RES_ID = "title_res_id"
    TextView labelLoading

    static LoadingDialog newInstance(@StringRes int message) {
        Bundle args = new Bundle()
        args.putInt(MSG_RES_ID, message)

        LoadingDialog loadingDialog = new LoadingDialog()
        loadingDialog.setCancelable(false)
        loadingDialog.arguments = args

        loadingDialog
    }

    @Override
    Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading_data, null)

        labelLoading = (TextView) view.findViewById(R.id.label_loading)
        labelLoading.text = getMessage()

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
        builder.setView(view)

        builder.create()
    }

    String getMessage() {
        getString(arguments.getInt(MSG_RES_ID))
    }
}