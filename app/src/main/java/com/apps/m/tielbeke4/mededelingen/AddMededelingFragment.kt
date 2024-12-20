package com.apps.m.tielbeke4.mededelingen

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.apps.m.tielbeke4.R
import com.apps.m.tielbeke4.databinding.FragmentMededelingInputBinding
import com.apps.m.tielbeke4.filialen.Filiaal
import com.apps.m.tielbeke4.viewmodel.FiliaalViewModel
import com.apps.m.tielbeke4.viewmodel.FilialenDao
import com.apps.m.tielbeke4.viewmodel.FilialenRepository
import com.apps.m.tielbeke4.viewmodel.InjectorUtils

class AddMededelingFragment : DialogFragment() {
    private val binding by lazy {
        DataBindingUtil.inflate<FragmentMededelingInputBinding>(
            LayoutInflater.from(context),
            R.layout.fragment_mededeling_input,
            null,
            false,
        )
    }

    private val viewModel by lazy {
        val factory = InjectorUtils.provideFilialenViewModelFactory()
        ViewModelProviders.of(activity as FragmentActivity, factory).get(FiliaalViewModel::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding.viewModel = viewModel
        binding.viewModel?.mededeling?.set(
            binding.viewModel
                ?.filiaal
                ?.get()
                ?.mededeling,
        )

        return AlertDialog
            .Builder(activity)
            .setView(binding.root)
            .setPositiveButton("ok") { dialog, _ ->

                val filiaal = binding.viewModel?.filiaal?.get()
                filiaal?.mededeling = binding.viewModel?.mededeling?.get() ?: ""
                FilialenRepository.getInstance(FilialenDao()).addMededeling(
                    binding
                        ?.viewModel
                        ?.filiaal
                        ?.get() ?: Filiaal(),
                )
                dialog.dismiss()
            }.setNegativeButton("Annuleren") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    companion object {
        const val FILIAAL_ARG = "filiaal_arg"
    }
}
