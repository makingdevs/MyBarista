package com.makingdevs.mybarista.view

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import com.makingdevs.mybarista.R
import groovy.transform.CompileStatic

@CompileStatic
class NoteDialog extends DialogFragment {

    NoteDialog() {}

    static final String NOTE = "message_res_id"
    EditText previewNote
    Closure onNoteSubmit
    String textNote

    static NoteDialog newInstance(String message) {
        Bundle args = new Bundle()
        args.putString(NOTE, message)

        NoteDialog noteDialog = new NoteDialog()
        noteDialog.setCancelable(false)
        noteDialog.arguments = args

        noteDialog
    }

    @Override
    Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_note, null)

        textNote = arguments.getString(NOTE)
        previewNote = (EditText) view.findViewById(R.id.previewNote)
        previewNote.text = textNote

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
        builder.setView(view)

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            void onClick(DialogInterface dialog, int which) {
                textNote = previewNote.text
                onNoteSubmit.call(textNote)
                dialog.dismiss()
            }
        })

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            void onClick(DialogInterface dialog, int which) {
                dialog.cancel()
            }
        })

        builder.create()
    }
}