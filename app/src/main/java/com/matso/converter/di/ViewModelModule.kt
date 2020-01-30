package com.matso.converter.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.matso.converter.ui.MainViewModel
import com.matso.converter.comman.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun mainViewModel(viewModel:MainViewModel): ViewModel
}
