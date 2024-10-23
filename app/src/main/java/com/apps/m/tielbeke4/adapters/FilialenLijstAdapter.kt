package com.apps.m.tielbeke4.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apps.m.tielbeke4.R
import com.apps.m.tielbeke4.filialen.Filiaal


class FilialenLijstAdapter(private var filialenList: MutableList<Filiaal>) : RecyclerView.Adapter<FiliaalHolder>(),
    Filterable {


    private var filteredList = filialenList.toList()

    fun setAdapter(list: MutableList<Filiaal>) {
        filialenList = list
        filteredList = filialenList.toList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FiliaalHolder {
        val itemView = LayoutInflater.from(p0.context).inflate(R.layout.filialen_lijst_row, p0, false)
        return FiliaalHolder(itemView)
    }

    override fun getItemCount(): Int {
        return filialenList.size
    }

    override fun onBindViewHolder(p0: FiliaalHolder, p1: Int) {
        p0.onBind(filialenList[p1])

    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val bufferList = mutableListOf<Filiaal>()

                if (constraint == null || constraint.isEmpty()) {
                    bufferList.addAll(filteredList)
                } else {
                    val pattern: String = constraint.toString().trim().toLowerCase()
                    filteredList.forEach {
                        when {
                            it.filiaalnummer.toString().contains(pattern) -> bufferList.add(it)
                            it.Address.toLowerCase().contains(pattern) -> bufferList.add(it)
                            it.Postcode.toLowerCase().contains(pattern) -> bufferList.add(it)
                            it.telnum.toLowerCase().contains(pattern) -> bufferList.add(it)

                        }
                    }

                }

                val result = FilterResults()
                result.values = bufferList
                return result
            }


            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filialenList.clear()
                @Suppress("UNCHECKED_CAST")
                filialenList.addAll(results?.values as List<Filiaal>)
                notifyDataSetChanged()

            }

        }
    }

}


class FiliaalHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    fun onBind(filiaal: Filiaal) {

        val filiaalNummerView = itemView.findViewById<TextView>(R.id.rij_fil_nummer)
        val adresView = itemView.findViewById<TextView>(R.id.fil_adres)
        val postcodeView = itemView.findViewById<TextView>(R.id.fil_postcode)
        val telNumView = itemView.findViewById<TextView>(R.id.fil_tel)
        val infoView = itemView.findViewById<TextView>(R.id.fil_info)
        filiaalNummerView.text = filiaal.filiaalnummer.toString()
        adresView.text = "Adres: ${filiaal.Address}"
        postcodeView.text = "PostCode:  ${filiaal.Postcode}"
        telNumView.text = "Tel:  ${filiaal.telnum}"
        infoView.text = "Info:  ${filiaal.Info}"
    }


}