package com.apps.m.tielbeke4

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.*
import android.view.inputmethod.InputMethodManager
import com.apps.m.tielbeke4.databinding.MainUiFragmentBinding
import com.apps.m.tielbeke4.filialen.DiversenFragment
import com.apps.m.tielbeke4.filialen.Filiaal
import com.apps.m.tielbeke4.mededelingen.AddMededelingFragment
import com.apps.m.tielbeke4.viewmodel.FiliaalViewModel
import com.apps.m.tielbeke4.viewmodel.InjectorUtils
import kotlinx.android.synthetic.main.main_ui_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class MainUiFragment : Fragment() {

    private lateinit var binding: MainUiFragmentBinding
    private val mainJob = Job()
    private val mainScope = CoroutineScope(Dispatchers.Main + mainJob)


    private val filiaalModel by lazy(mode = LazyThreadSafetyMode.NONE) {
        val factory = InjectorUtils.provideFilialenViewModelFactory()
        ViewModelProviders.of(activity as FragmentActivity, factory).get(FiliaalViewModel::class.java)
    }

    private val buttonListeners by lazy(mode = LazyThreadSafetyMode.NONE) {
        object : FiliaalViewModel.OnButtonListener {

            override fun zoekButtonListener() {

                mainScope.launch {
                    try {
                        closeKeyBoard()
                        val filiaalnummer = filiaalModel.filiaalNummer.get()?.toShort()
                        val filiaal = filiaalModel.queryDb(filiaalnummer ?: 0)
                        if (filiaal.filiaalnummer.toInt() != 0) {
                            filiaalModel.apply {
                                this.filiaal.set(filiaal)
                                filiaalTitle.set(filiaal.toString())
                                filiaalNummer.set("")
                            }
                        } else {
                            filiaalModel.apply {
                                filiaalTitle.set("")
                                Snackbar.make(root_layout, getString(R.string.filiaal_niet_gevonden_text), Snackbar.LENGTH_SHORT).show()
                                filiaalNummer.set("")

                            }

                        }

                    } catch (E: NumberFormatException) {
                        filiaalModel.apply {
                            filiaalTitle.set("")
                            Snackbar.make(root_layout, getString(R.string.geen_invoer), Snackbar.LENGTH_SHORT).show()
                            filiaalNummer.set("")
                            filiaal.set(Filiaal())

                        }
                    }
                }
            }


            override fun clickKaartListener() {


                if (filiaalModel.filiaal.get()?.Address == "") {
                    Snackbar.make(root_layout, getString(R.string.geen_geldig_adres), Snackbar.LENGTH_SHORT).show()
                    return
                }
                val intent = Intent(
                        Intent.ACTION_VIEW, Uri.parse(
                        "geo:0,0?q=${filiaalModel.filiaal.get()?.Address} " +
                                "${filiaalModel.filiaal.get()?.Postcode}"
                )
                )
                if (activity?.packageManager?.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                    intent.`package` = "com.google.android.apps.maps"
                    startActivity(intent)
                } else Snackbar.make(root_layout, "Google maps niet gevonden!", Snackbar.LENGTH_SHORT).show()

            }


            override fun clickTelListener() {

                if (filiaalModel.filiaal.get()?.telnum?.contains(Regex("\\d+-\\d+")) == false) {
                    Snackbar.make(root_layout, getString(R.string.geen_geldig_telefoonnummer), Snackbar.LENGTH_SHORT).show()
                    return
                }

                val intent =
                        Intent(Intent.ACTION_DIAL, Uri.parse("tel:${binding.filiaalmodel?.filiaal?.get()?.telnum}"))
                startActivity(intent)
            }

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_ui_fragment, container, false)
        binding.filiaalmodel = filiaalModel

        filiaalModel.apply {

            setOnButtonListener(buttonListeners)
            mededelingAddedCallback = {
                Snackbar.make(root_layout, "Mededeling is toegevoegd aan de database", Snackbar.LENGTH_LONG).show()
            }
        }
        binding.executePendingBindings()
        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.diversen_filialen, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem?) =
            when (item?.itemId) {
                R.id.diverse_filialen_item -> {
                    fragmentManager?.findFragmentByTag(DIVERSE_FRAGMENT_TAG).let {
                        if (it == null) {
                            fragmentManager?.beginTransaction()
                                    ?.add(
                                            DiversenFragment(),
                                            DIVERSE_FRAGMENT_TAG
                                    )?.commit()

                        }
                    }
                    true

                }


                R.id.app_info_id -> {
                    fragmentManager?.findFragmentByTag(APP_INFO_TAG).let {
                        if (it == null) {
                            val fragment = AppInfoDialogFragment()
                            fragment.isCancelable = false
                            fragmentManager?.beginTransaction()
                                    ?.add(fragment, APP_INFO_TAG)?.commit()

                        }
                    }
                    true
                }

                R.id.add_mededeling -> {
                    if (filiaalModel.getFilialen()?.value?.none {
                                it.filiaalnummer == filiaalModel.filiaal.get()?.filiaalnummer
                            } == true) {
                        Snackbar.make(root_layout, getString(R.string.geen_geldig_filiaal_gekozen), Snackbar.LENGTH_SHORT).show()

                    } else {
                        fragmentManager?.findFragmentByTag(ADD_MEDEDELINGEN_FRAGMENT_TAG).let {
                            if (it == null) {
                                val fragment = AddMededelingFragment()
                                fragment.isCancelable = false
                                fragmentManager?.beginTransaction()?.add(
                                        fragment,
                                        ADD_MEDEDELINGEN_FRAGMENT_TAG
                                )?.commit()

                            }
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

    override fun onDestroy() {
        super.onDestroy()
        mainJob.cancel()
    }

    companion object {
        private const val DIVERSE_FRAGMENT_TAG = "diversen_fragement"
        private const val FILIALEN_LIJST_FRAGMENT_TAG = "filialen_lijst_fragment"
        private const val APP_INFO_TAG = "AppInfoDialogFragment"
        private const val MEDEDELINGEN_FRAGMENT_TAG = "MededelingenList"
        private const val ADD_MEDEDELINGEN_FRAGMENT_TAG = "AddMededelingFragment"

    }

}

