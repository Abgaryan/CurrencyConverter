package com.matso.converter.di

import com.matso.converter.ConverterApplication

object AppInjector {
    fun init(converterApplication: ConverterApplication) {
        DaggerAppComponent.builder().application(converterApplication)
            .build().inject(converterApplication)
    }
}
