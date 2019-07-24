package com.apps.m.tielbeke4.viewmodel

import com.apps.m.tielbeke4.QueryDatabase

object InjectorUtils {

    fun provideFilialenViewModelFactory() : FilialenViewModelFactory {
        return FilialenViewModelFactory(
            FilialenRepository.getInstance(
                QueryDatabase.dao
            )
        )

    }
}