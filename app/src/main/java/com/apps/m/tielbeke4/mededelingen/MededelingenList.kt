package com.apps.m.tielbeke4.mededelingen

import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apps.m.tielbeke4.R
import com.apps.m.tielbeke4.adapters.MededelingSwipeController
import com.apps.m.tielbeke4.adapters.MededelingenAdapter
import com.apps.m.tielbeke4.filialen.Filiaal
import com.apps.m.tielbeke4.viewmodel.FiliaalViewModel
import com.apps.m.tielbeke4.viewmodel.FilialenRepository
import com.apps.m.tielbeke4.viewmodel.FireBaseDao
import com.apps.m.tielbeke4.viewmodel.InjectorUtils

class MededelingenList : Fragment() {


    private val viewModel by lazy {
        val factory = InjectorUtils.provideFilialenViewModelFactory()
        ViewModelProviders.of(activity as FragmentActivity, factory).get(FiliaalViewModel::class.java)
    }

    lateinit var mededelingenRecyclerView: RecyclerView

    private val mededelingenAdapter by lazy {
        MededelingenAdapter(viewModel.getFilialen()?.value as? MutableList<Filiaal> ?: mutableListOf())
    }

    private val mededelingSwipeController by lazy {
        val mededelingSwipeController =
            MededelingSwipeController(viewModel.getFilialen()?.value ?: listOf())
        mededelingSwipeController.setSwipeCallbacks(swipeCallback)
        mededelingSwipeController
    }

    private val swipecontroller by lazy {

        ItemTouchHelper(mededelingSwipeController)
    }


    private val swipeCallback by lazy {
        object : MededelingSwipeController.MededelingenSwipeCalbacks {
            override fun deleteMededelingConfirmation(filiaal: Filiaal) {

                AlertDialog.Builder(context).setMessage(
                    "Bevestiging gevraagd voor verwijdering van de mededeling\n" +
                            "${filiaal.filiaalnummer}\n${filiaal.Address}"
                )
                    .setCancelable(false).setPositiveButton("Ja") { dialog, _ ->
                        FilialenRepository.getInstance(FireBaseDao()).apply {
                            deleteMededeling(filiaal)
                            mededelingDeletedCallback = {
                                Snackbar.make(
                                    mededelingenRecyclerView, "Mededeling is verwijderd uit de database",
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }
                        dialog.dismiss()
                    }.setNegativeButton("nee") { dialog, _ ->
                        mededelingenAdapter.notifyDataSetChanged()
                        dialog.dismiss()
                    }.show()

            }

        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(activity).inflate(R.layout.mededelingen_list, container, false)
        mededelingenRecyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_mededelingen_id)
        mededelingenRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mededelingenRecyclerView.adapter = mededelingenAdapter
        swipecontroller.attachToRecyclerView(mededelingenRecyclerView)
        viewModel.getFilialen()?.observe(this, Observer {

            if (it != null && !it.isEmpty()) {
                mededelingenAdapter.setAdapter(it.toMutableList())
            }

        })
        return view
    }

}


