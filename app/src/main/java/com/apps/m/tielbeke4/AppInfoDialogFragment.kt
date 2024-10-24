package com.apps.m.tielbeke4

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import androidx.core.app.ShareCompat
import androidx.fragment.app.DialogFragment

class AppInfoDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val diversen = inflater.inflate(
            R.layout.app_info_dialog_fragment, null)

        val mIntent: Intent = ShareCompat.IntentBuilder.from(requireActivity())
            .setType("text/plain")
        .addEmailTo(getString(R.string.E_mail)).setSubject("Nieuwe Filialen")
        .createChooserIntent()

        val textViewEmail = diversen.findViewById<TextView>(R.id
            .text_view_email)

        if(activity?.packageManager?.resolveActivity(mIntent,PackageManager
            .MATCH_DEFAULT_ONLY) == null) {
            textViewEmail.isEnabled = false
        }

        textViewEmail.setOnClickListener {
            startActivity(mIntent)
        }

       return AlertDialog.Builder(context).setView(diversen.rootView)
            .setPositiveButton("Ok")  { dialog, _ ->
                dialog.dismiss()
            }.show()
    }
}
