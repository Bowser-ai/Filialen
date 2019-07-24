package com.apps.m.tielbeke4

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import kotlinx.android.synthetic.main.app_info_dialog_fragment.*
import android.support.v4.app.DialogFragment
import android.support.v4.app.ShareCompat
import android.view.LayoutInflater

import android.widget.TextView

class AppInfoDialogFragment : DialogFragment() {

    private var mIntent : Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mIntent = ShareCompat.IntentBuilder.from(activity).setType("text/plain")
            .addEmailTo(getString(R.string.E_mail)).setSubject("Nieuwe Filialen")
            .createChooserIntent()
        if(activity?.packageManager?.resolveActivity(mIntent,PackageManager.MATCH_DEFAULT_ONLY)==null){
            text_view_email.isEnabled = false
        }
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(activity).inflate(R.layout.app_info_dialog_fragment,null)
        val email = view.findViewById<TextView>(R.id.text_view_email)
        email.setOnClickListener {

            startActivity(mIntent)
        }

        return AlertDialog.Builder(activity).setView(view)
            .setPositiveButton("Ok")  { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

}
