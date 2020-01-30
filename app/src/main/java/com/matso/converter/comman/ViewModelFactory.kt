package com.matso.converter.comman

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider


class ViewModelFactory @Inject constructor(private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>):
    ViewModelProvider.Factory {

    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        try {
            @Suppress("UNCHECKED_CAST") return viewModels[modelClass]?.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}
