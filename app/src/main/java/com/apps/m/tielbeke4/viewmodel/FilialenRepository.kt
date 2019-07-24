package com.apps.m.tielbeke4.viewmodel

import android.util.Log
import com.apps.m.tielbeke4.QueryDatabase
import com.apps.m.tielbeke4.filialen.Filiaal

class FilialenRepository private constructor(private val fireBaseDao: FireBaseDao) {

    init {
        Log.d("TAGREPO", "repo init")
    }

    var mededelingAddedCallback
        get() = fireBaseDao.mededelingAddedCallback
        set(value) {
            fireBaseDao.mededelingAddedCallback = value
        }

    var mededelingDeletedCallback
        get() = fireBaseDao.mededelingDeletedCallback
        set(value) {
            fireBaseDao.mededelingDeletedCallback = value
        }


    /* fun addFiliaal(filiaal: Filiaal) {
         fireBaseDao.addFiliaal(filiaal)
     }

     fun deleteFiliaal(filiaal: Filiaal) {
         fireBaseDao.deleteFiliaal(filiaal)
     }*/

    fun addMededeling(filiaal: Filiaal) {
        fireBaseDao.addMededeling(filiaal)
    }

    fun deleteMededeling(filiaal: Filiaal) {
        fireBaseDao.deleteMededeling(filiaal)
    }

    suspend fun queryDb(filiaalnummer:Short) = fireBaseDao.queryDb(filiaalnummer)

    fun getFilialen() =
        fireBaseDao.getFilialen()

    companion object {

        @Volatile
        private var instance: FilialenRepository? = null

        fun getInstance(fireBaseDao: FireBaseDao) =
            instance ?: synchronized(this) {
                instance
                    ?: FilialenRepository(fireBaseDao).also {
                    instance = it
                }
            }
    }
}