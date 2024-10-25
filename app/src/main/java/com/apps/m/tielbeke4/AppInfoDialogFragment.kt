package com.apps.m.tielbeke4

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ShareCompat
import androidx.fragment.app.DialogFragment
import com.apps.m.tielbeke4.databinding.AppInfoDialogFragmentBinding

class AppInfoDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = AppInfoDialogFragmentBinding.inflate(layoutInflater)

        // The intent to automatically open an available email client, and
        // prefill the subject.
        val mIntent: Intent = ShareCompat.IntentBuilder(requireActivity())
            .setType("text/plain")
        .addEmailTo(getString(R.string.E_mail)).setSubject(getString(R
            .string.new_branches)).createChooserIntent()

        val textViewEmail = binding.textViewEmail

        // Set the email link on active if a email client could be found.
        activity
            ?.packageManager
            ?.resolveActivity(
                mIntent,
            PackageManager
            .MATCH_DEFAULT_ONLY)?.let {
                textViewEmail.isEnabled = true
                textViewEmail.setOnClickListener {
                    startActivity(mIntent)
                }
            }


       return AlertDialog.Builder(context).setView(binding.root)
            .setPositiveButton("Ok")  { dialog, _ ->
                dialog.dismiss()
            }.show()
    }
}
