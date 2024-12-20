package com.apps.m.tielbeke4.viewmodel

import com.apps.m.tielbeke4.data.FilialenIO

object InjectorUtils {
    fun provideFilialenViewModelFactory(): FilialenViewModelFactory =
        FilialenViewModelFactory(
            FilialenRepository.getInstance(
                FilialenIO.dao,
            ),
        )
}
