package com.apps.m.tielbeke4.viewmodel


import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.apps.m.tielbeke4.QueryDatabase
import com.apps.m.tielbeke4.filialen.Filiaal


class FireBaseDao {
    var mededelingAddedCallback
        get() = QueryDatabase.mededelingAddedCallback
        set(value) {
            QueryDatabase.mededelingAddedCallback = value
        }

    var mededelingDeletedCallback
        get() = QueryDatabase.mededelingDeletedCallback
        set(value) {
            QueryDatabase.mededelingDeletedCallback = value
        }


    private val filialenList = mutableListOf<Filiaal>()
    private val filialen = MutableLiveData<List<Filiaal>>()


    init {
        filialen.postValue(filialenList)
    }

    /* fun addFiliaal(filiaal: Filiaal) {

     }

     fun deleteFiliaal(filiaal: Filiaal) {

     }*/


    fun addMededeling(filiaal: Filiaal) {
        QueryDatabase.addMededeling(filiaal)
    }

    fun deleteMededeling(filiaal: Filiaal) {
        QueryDatabase.deleteMededeling(filiaal)
    }

    fun getFilialen() = filialen as? LiveData<List<Filiaal>>

    suspend fun queryDb(filiaalnummer:Short) = QueryDatabase.queryDb(filiaalnummer)


    suspend fun retrieveFromDb(listOp: suspend (MutableList<Filiaal>) -> Unit) {
        listOp(filialenList)
        filialen.postValue(filialenList)
    }
}