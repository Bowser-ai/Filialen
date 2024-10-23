package com.apps.m.tielbeke4

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater

import android.widget.TextView
import androidx.core.app.ShareCompat
import androidx.fragment.app.DialogFragment
import com.apps.m.tielbeke4.databinding.AppInfoDialogFragmentBinding

class AppInfoDialogFragment : DialogFragment() {

    private var mIntent : Intent? = null
    private lateinit var binding: AppInfoDialogFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AppInfoDialogFragmentBinding.inflate(layoutInflater)
        mIntent = ShareCompat.IntentBuilder.from(requireActivity().parent).setType("text/plain")
            .addEmailTo(getString(R.string.E_mail)).setSubject("Nieuwe Filialen")
            .createChooserIntent()
        if(activity?.packageManager?.resolveActivity(mIntent!!,PackageManager.MATCH_DEFAULT_ONLY)==null){
            binding.textViewEmail.isEnabled = false
        }
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding.textViewEmail.setOnClickListener {

            startActivity(mIntent)
        }

        return AlertDialog.Builder(activity).setView(view)
            .setPositiveButton("Ok")  { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

}
