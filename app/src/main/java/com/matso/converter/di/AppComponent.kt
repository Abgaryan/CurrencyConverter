package com.matso.converter.di

import android.app.Application
import com.matso.converter.ConverterApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton


@Singleton
@Component(modules = [AndroidInjectionModule::class, AppModule::class, MainActivityModule::class, ViewModelModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(converterApplication: ConverterApplication)
}
