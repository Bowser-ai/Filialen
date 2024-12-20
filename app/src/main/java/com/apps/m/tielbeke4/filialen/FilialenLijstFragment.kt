package com.apps.m.tielbeke4.filialen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apps.m.tielbeke4.R
import com.apps.m.tielbeke4.adapters.FilialenListAdapter
import com.apps.m.tielbeke4.viewmodel.FiliaalViewModel
import com.apps.m.tielbeke4.viewmodel.InjectorUtils

class FilialenLijstFragment : Fragment() {
    private val viewmodel by lazy(mode = LazyThreadSafetyMode.NONE) {
        val factory = InjectorUtils.provideFilialenViewModelFactory()
        ViewModelProviders.of(activity as FragmentActivity, factory).get(FiliaalViewModel::class.java)
    }

    private val filialenLijstAdapter by lazy(mode = LazyThreadSafetyMode.NONE) {
        FilialenListAdapter(
            requireContext().applicationContext,
            viewmodel.getFilialen()?.value as?
                MutableList<Filiaal> ?: mutableListOf(),
        )
    }

    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.filialen_fragment, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.filialen_lijst)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = filialenLijstAdapter
        viewmodel.getFilialen()?.observe(
            viewLifecycleOwner,
            { filialenLijstAdapter.setAdapter(it) },
        )
        return view
    }

    override fun onCreateOptionsMenu(
        menu: Menu,
        inflater: MenuInflater,
    ) {
        inflater.inflate(R.menu.filialen_lijst_menu, menu)
        val menuItem = menu.findItem(R.id.action_search)
        searchView = menuItem?.actionView as SearchView
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = false

                override fun onQueryTextChange(newText: String?): Boolean {
                    filialenLijstAdapter.filter.filter(newText)
                    return true
                }
            },
        )
    }
}
