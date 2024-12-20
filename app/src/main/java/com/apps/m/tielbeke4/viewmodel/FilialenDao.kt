package com.apps.m.tielbeke4.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.apps.m.tielbeke4.data.FilialenIO
import com.apps.m.tielbeke4.filialen.Filiaal

class FilialenDao {
    var mededelingAddedCallback
        get() = FilialenIO.mededelingAddedCallback
        set(value) {
            FilialenIO.mededelingAddedCallback = value
        }

    var mededelingDeletedCallback
        get() = FilialenIO.mededelingDeletedCallback
        set(value) {
            FilialenIO.mededelingDeletedCallback = value
        }

    private val filialenList = mutableListOf<Filiaal>()
    private val filialen = MutableLiveData<List<Filiaal>>()

    init {
        filialen.postValue(filialenList)
    }

    fun addMededeling(filiaal: Filiaal) {
        FilialenIO.addMededeling(filiaal)
    }

    fun deleteMededeling(filiaal: Filiaal) {
        FilialenIO.deleteMededeling(filiaal)
    }

    fun getFilialen() = filialen as? LiveData<List<Filiaal>>

    suspend fun queryDb(filiaalnummer: Short) = FilialenIO.queryDb(filiaalnummer)

    suspend fun retrieveFromDb(listOp: suspend (MutableList<Filiaal>) -> Unit) {
        listOp(filialenList)
        filialen.postValue(filialenList)
    }
}
