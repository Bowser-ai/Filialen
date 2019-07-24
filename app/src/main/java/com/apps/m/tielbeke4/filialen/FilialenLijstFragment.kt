package com.apps.m.tielbeke4.filialen

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
import com.apps.m.tielbeke4.R
import com.apps.m.tielbeke4.adapters.FilialenLijstAdapter
import com.apps.m.tielbeke4.viewmodel.FiliaalViewModel
import com.apps.m.tielbeke4.viewmodel.InjectorUtils


class FilialenLijstFragment : Fragment() {

    private val viewmodel by lazy(mode = LazyThreadSafetyMode.NONE) {
        val factory = InjectorUtils.provideFilialenViewModelFactory()
        ViewModelProviders.of(activity as FragmentActivity,factory).get(FiliaalViewModel::class.java)
    }

    private val filialenLijstAdapter by lazy(mode = LazyThreadSafetyMode.NONE) {
        FilialenLijstAdapter(viewmodel.getFilialen()?.value as? MutableList<Filiaal> ?: mutableListOf())
    }

        private lateinit var searchView: SearchView

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setHasOptionsMenu(true)

        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.filialen_fragment, container, false)
            val recyclerView = view.findViewById<RecyclerView>(R.id.filialen_lijst)
            recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recyclerView.adapter = filialenLijstAdapter
            viewmodel.getFilialen()?.observe(this, Observer {
               filialenLijstAdapter.setAdapter(it as MutableList<Filiaal>)
            })
            return view

        }


        override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
            inflater?.inflate(R.menu.filialen_lijst_menu, menu)
            val menuItem = menu?.findItem(R.id.action_search)
            searchView = menuItem?.actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {

                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    filialenLijstAdapter.filter.filter(newText)
                    return true
                }
            })

        }


    }