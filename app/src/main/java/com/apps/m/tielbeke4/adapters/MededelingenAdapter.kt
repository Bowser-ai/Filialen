package com.apps.m.tielbeke4.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apps.m.tielbeke4.R
import com.apps.m.tielbeke4.filialen.Filiaal


class MededelingenAdapter(private val filialenList: MutableList<Filiaal>) :
    RecyclerView.Adapter<MededelingenHolder>() {

    private var mededelingenList =
        filialenList.filter {
            it.mededeling != ""
        }


    fun setAdapter(list: MutableList<Filiaal>) {
        if (!list.isEmpty()) {
            mededelingenList = list.filter { it.mededeling != "" }
            notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(p0: MededelingenHolder, p1: Int) {
        p0.onBind(mededelingenList[p1] )
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MededelingenHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.mededelingen_row, p0, false)
        return MededelingenHolder(view)
    }

    override fun getItemCount(): Int {
        return mededelingenList.size
    }

}

class MededelingenHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun onBind(filiaal: Filiaal) {
        val filaalnummerText = view.findViewById<TextView>(R.id.filiaalnummer_view_id)
        val adresText = view.findViewById<TextView>(R.id.adres_view_id)
        val mededelingenText = view.findViewById<TextView>(R.id.mededeling_view_id)
        filaalnummerText.text = filiaal.filiaalnummer.toString()
        adresText.text = filiaal.Address
        mededelingenText.text = "Mededelingen: ${filiaal.mededeling}"
    }
}