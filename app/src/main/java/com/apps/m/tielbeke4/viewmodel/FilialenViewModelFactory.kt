package com.apps.m.tielbeke4.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class FilialenViewModelFactory(private val filialenRepository: FilialenRepository)
    : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FiliaalViewModel(filialenRepository) as T
    }
}