package com.matso.converter.di

import com.matso.converter.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
internal abstract class MainActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeConverterActivity(): MainActivity
}
