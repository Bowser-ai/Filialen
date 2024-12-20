package com.apps.m.tielbeke4.viewmodel

import android.util.Log
import com.apps.m.tielbeke4.filialen.Filiaal

class FilialenRepository private constructor(
    private val fireBaseDao: FilialenDao,
) {
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

    fun addMededeling(filiaal: Filiaal) {
        fireBaseDao.addMededeling(filiaal)
    }

    fun deleteMededeling(filiaal: Filiaal) {
        fireBaseDao.deleteMededeling(filiaal)
    }

    suspend fun queryDb(filiaalnummer: Short) = fireBaseDao.queryDb(filiaalnummer)

    fun getFilialen() = fireBaseDao.getFilialen()

    companion object {
        @Volatile
        private var instance: FilialenRepository? = null

        fun getInstance(fireBaseDao: FilialenDao) =
            instance ?: synchronized(this) {
                instance
                    ?: FilialenRepository(fireBaseDao).also {
                        instance = it
                    }
            }
    }
}
