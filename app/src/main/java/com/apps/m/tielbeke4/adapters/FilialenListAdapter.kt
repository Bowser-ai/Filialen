package com.apps.m.tielbeke4.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apps.m.tielbeke4.R
import com.apps.m.tielbeke4.filialen.Filiaal
import java.util.Locale

/**
 * An adapter for a list view of `filialen`.
 */
class FilialenListAdapter(
    private val context: Context,
    filialenList: List<Filiaal>,
) : RecyclerView.Adapter<FiliaalHolder>(),
    Filterable {
    // The list that is currently shown in the list view.
    private val adapterFilialenList = filialenList.toMutableList()

    // The original list of filialen before filtering actions.
    private val originalFilialenList = filialenList.toMutableList()

    fun setAdapter(list: List<Filiaal>) {
        fun <T> swapIterables(
            it1: MutableCollection<T>,
            it2: Iterable<T>,
        ) {
            it1.clear()
            it1.addAll(it2)
        }

        // If the filialen retrieved have changed, update this view.
        swapIterables(adapterFilialenList, list)
        swapIterables(originalFilialenList, list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        p0: ViewGroup,
        p1: Int,
    ): FiliaalHolder {
        val itemView =
            LayoutInflater.from(p0.context).inflate(
                R.layout.filialen_lijst_row,
                p0,
                false,
            )
        return FiliaalHolder(context, itemView)
    }

    override fun getItemCount(): Int = adapterFilialenList.size

    override fun onBindViewHolder(
        p0: FiliaalHolder,
        p1: Int,
    ) {
        p0.onBind(adapterFilialenList[p1])
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val result = FilterResults()
                // Filter the original list on the constraint because the
                // adapter list is getting changed later.
                result.values =
                    if (constraint.isNullOrEmpty()) {
                        originalFilialenList
                    } else {
                        val pattern = constraint.toString().trim().lowercase()
                        originalFilialenList.filter {
                            it.filiaalnummer.toString().contains(pattern) ||
                                it.Address.lowercase().contains(pattern) ||
                                it.Postcode.lowercase().contains(pattern) ||
                                it.telnum.lowercase().contains(pattern)
                        }
                    }
                return result
            }

            override fun publishResults(
                constraint: CharSequence?,
                results: FilterResults?,
            ) {
                // Update the adapter list with the filter results.
                adapterFilialenList.clear()
                @Suppress("UNCHECKED_CAST")
                adapterFilialenList.addAll(results?.values as Collection<Filiaal>)
                notifyDataSetChanged()
            }
        }
    }
}

/**
 * Hold a row containing a filiaal in the list view.
 */
class FiliaalHolder(
    private val context: Context,
    itemView: View,
) : RecyclerView.ViewHolder(itemView) {
    fun onBind(filiaal: Filiaal) {
        val filiaalNummerView = itemView.findViewById<TextView>(R.id.rij_fil_nummer)
        val adresView = itemView.findViewById<TextView>(R.id.fil_adres)
        val postcodeView = itemView.findViewById<TextView>(R.id.fil_postcode)
        val telNumView = itemView.findViewById<TextView>(R.id.fil_tel)
        val infoView = itemView.findViewById<TextView>(R.id.fil_info)
        val res = context.resources
        filiaalNummerView.text =
            String.format(
                Locale.getDefault(),
                "%d",
                filiaal
                    .filiaalnummer,
            )
        adresView.text =
            String.format(
                res.getString(R.string.address),
                filiaal.Address,
            )
        postcodeView.text =
            String.format(
                res.getString(R.string.zip_code),
                filiaal.Postcode,
            )
        telNumView.text =
            String.format(
                res.getString(
                    R.string
                        .telephone_number,
                ),
                filiaal.telnum,
            )
        infoView.text =
            String.format(
                res.getString(R.string.information),
                filiaal.Info,
            )
    }
}
