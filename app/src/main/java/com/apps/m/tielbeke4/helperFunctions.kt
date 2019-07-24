package com.apps.m.tielbeke4

import android.content.Context
import android.widget.Toast
import com.apps.m.tielbeke4.databinding.MainUiFragmentBinding
import com.apps.m.tielbeke4.filialen.Filiaal

fun addSorted(filiaal: Filiaal?, list: MutableList<Filiaal>) {

    if (list.isEmpty() && filiaal != null) {
        list.add(filiaal)
        return
    }

    val filteredIterator = list.listIterator()
    for (element in filteredIterator.withIndex()) {


        if (element.index == list.size - 1 && filiaal != null) {
            filteredIterator.add(filiaal)
            return
        }

        if (
            filteredIterator.hasNext() && filiaal != null &&
            element.value.filiaalnummer < filiaal.filiaalnummer &&
            filteredIterator.next().filiaalnummer > filiaal.filiaalnummer
        ) {
            filteredIterator.previous()
            filteredIterator.add(filiaal)
            return
        } else filteredIterator.previous()
    }
}

fun getIndex(filiaal: Filiaal?, list: MutableList<Filiaal>): Int {


    return list.binarySearch(filiaal,
        compareBy {
            it?.filiaalnummer?.compareTo(filiaal?.filiaalnummer ?: 0)
        })
}

fun Context.toast(message: String) = Toast.makeText(this,message,Toast.LENGTH_SHORT).show()

fun MainUiFragmentBinding.filiaal() = this.filiaalmodel?.filiaal
fun MainUiFragmentBinding.getFilialen() = this.filiaalmodel?.getFilialen()



