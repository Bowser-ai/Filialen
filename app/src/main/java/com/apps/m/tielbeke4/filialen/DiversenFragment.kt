package com.apps.m.tielbeke4.filialen

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.apps.m.tielbeke4.R
import java.io.BufferedReader
import java.io.InputStreamReader

class DiversenFragment : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_diversen, null, false)
        val diversenTextView = view.findViewById<TextView>(R.id.diversen_text_id)
        diversenTextView.text = generateText(context)
        return AlertDialog.Builder(activity).setView(view)
                .setPositiveButton("Ok")  { dialog, _ ->
                    dialog.dismiss()
                }.show()
    }


    private fun generateText(context: Context?): String {


        return BufferedReader(InputStreamReader(context?.assets?.open("Diversen_Filialen.txt"))).use {
            it.readText()
        }

    }

}





