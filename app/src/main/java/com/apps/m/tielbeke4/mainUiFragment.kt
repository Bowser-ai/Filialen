package com.apps.m.tielbeke4

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.apps.m.tielbeke4.databinding.MainUiFragmentBinding
import com.apps.m.tielbeke4.filialen.DiversenFragment
import com.apps.m.tielbeke4.filialen.Filiaal
import com.apps.m.tielbeke4.mededelingen.AddMededelingFragment
import com.apps.m.tielbeke4.viewmodel.FiliaalViewModel
import com.apps.m.tielbeke4.viewmodel.InjectorUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class MainUiFragment : Fragment() {
    private var _binding: MainUiFragmentBinding? = null
    private val binding get() = _binding!!

    private val mainJob = Job()
    private val mainScope = CoroutineScope(Dispatchers.Main + mainJob)
    private val filiaalModel by lazy {
        val factory = InjectorUtils.provideFilialenViewModelFactory()
        activity?.let { ViewModelProvider(it, factory)[FiliaalViewModel::class.java] }
    }

    private val buttonListeners by lazy(mode = LazyThreadSafetyMode.NONE) {
        object : FiliaalViewModel.OnButtonListener {
            override fun zoekButtonListener() {
                mainScope.launch {
                    try {
                        closeKeyBoard()
                        val filiaalnummer = filiaalModel?.filiaalNummer?.get()?.toShort()
                        val filiaal = filiaalModel?.queryDb(filiaalnummer ?: 0)
                        if (filiaal != null) {
                            if (filiaal.filiaalnummer.toInt() != 0) {
                                filiaalModel?.apply {
                                    this.filiaal.set(filiaal)
                                    filiaalTitle.set(filiaal.toString())
                                    filiaalNummer.set("")
                                    activateAddMededelingButton()
                                }
                            } else {
                                filiaalModel?.apply {
                                    filiaalTitle.set("")
                                    Snackbar.make(binding.rootLayout, getString(R.string.filiaal_niet_gevonden_text),
                                        Snackbar.LENGTH_SHORT ).show()
                                    filiaalNummer.set("")
                                    deactivateAddMededelingButton()
                                }
                            }
                        }

                    } catch (E: NumberFormatException) {
                        filiaalModel?.apply {
                            filiaalTitle.set("")
                            Snackbar.make(binding.rootLayout, getString(R.string.geen_invoer), Snackbar.LENGTH_SHORT ).show()
                            filiaalNummer.set("")
                            filiaal.set(Filiaal())
                            deactivateAddMededelingButton()
                        }
                    }
                }
            }

            override fun clickKaartListener() {
                if (filiaalModel?.filiaal?.get()?.Address == "") {
                    Snackbar.make(binding.rootLayout, getString(R.string.geen_geldig_adres), Snackbar.LENGTH_SHORT ).show()
                    return
                }
                val intent = Intent( Intent.ACTION_VIEW, Uri.parse(
                        "geo:0,0?q=${filiaalModel?.filiaal?.get()?.Address} " +
                                "${filiaalModel?.filiaal?.get()?.Postcode}"
                    ))

                if (activity?.packageManager?.resolveActivity(
                        intent, PackageManager.MATCH_DEFAULT_ONLY ) != null ) {
                    intent.`package` = "com.google.android.apps.maps"
                    startActivity(intent)
                } else Snackbar.make( binding.rootLayout, "Google maps niet gevonden!", Snackbar.LENGTH_SHORT ).show()
            }

            override fun clickTelListener() {
                if (filiaalModel?.filiaal?.get()?.telnum?.contains(Regex("\\d+-\\d+")) == false) {
                    Snackbar.make( binding.rootLayout, getString(R.string.geen_geldig_telefoonnummer),
                        Snackbar.LENGTH_SHORT ).show()
                    return
                }
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse(
                    "tel:${binding.filiaalmodel?.filiaal?.get()?.telnum}")
                )
                startActivity(intent)
            }

            override fun addMededelingListener() {
                if (filiaalModel?.getFilialen()?.value?.none {
                        it.filiaalnummer == filiaalModel!!.filiaal.get()?.filiaalnummer
                    } == true)
                {
                    Snackbar.make(binding.rootLayout, getString(R.string.geen_geldig_filiaal_gekozen),
                        Snackbar.LENGTH_SHORT).show()
                } else {
                    parentFragmentManager.findFragmentByTag(ADD_MEDEDELINGEN_FRAGMENT_TAG).let {
                        val fragment = it as? AddMededelingFragment ?: AddMededelingFragment()
                        fragment.isCancelable = false
                        parentFragmentManager.beginTransaction().add(fragment, ADD_MEDEDELINGEN_FRAGMENT_TAG).commit()
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.main_ui_fragment, container, false)
        binding.filiaalmodel = filiaalModel
        filiaalModel?.apply {
            setOnButtonListener(buttonListeners)
            mededelingAddedCallback = {
                Snackbar.make(binding.rootLayout, "Mededeling is toegevoegd aan de database", Snackbar.LENGTH_LONG ).show()
            }
        }

        binding.executePendingBindings()
        filiaalModel?.mededelingenButtonVisibility?.let {
            when (it) {
                View.VISIBLE -> activateAddMededelingButton()
                else -> deactivateAddMededelingButton()
            }
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.diversen_filialen, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.diverse_filialen_item -> {
                parentFragmentManager.findFragmentByTag(DIVERSE_FRAGMENT_TAG).let {
                    if (it == null) {
                        parentFragmentManager.beginTransaction()
                            .add(
                                DiversenFragment(),
                                DIVERSE_FRAGMENT_TAG
                            ).commit()

                    }
                }
                true
            }

            R.id.app_info_id -> {
                parentFragmentManager.findFragmentByTag(APP_INFO_TAG).let {
                    if (it == null) {
                        val fragment = AppInfoDialogFragment()
                        fragment.isCancelable = false
                        parentFragmentManager.beginTransaction()
                            .add(fragment, APP_INFO_TAG).commit()
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun closeKeyBoard() {
        val view = activity?.currentFocus
        if (view != null) {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun activateAddMededelingButton() {
        binding.addMededelingButton?.visibility = View.VISIBLE
        filiaalModel?.mededelingenButtonVisibility = View.VISIBLE
    }

    private fun deactivateAddMededelingButton() {
        binding.addMededelingButton?.visibility = View.GONE
        filiaalModel?.mededelingenButtonVisibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        mainJob.cancel()
        _binding = null
    }

    companion object {
        private const val DIVERSE_FRAGMENT_TAG = "diversen_fragement"
        private const val APP_INFO_TAG = "AppInfoDialogFragment"
        private const val ADD_MEDEDELINGEN_FRAGMENT_TAG = "AddMededelingFragment"
    }
}
