package com.matso.converter

import android.app.Activity
import android.app.Application
import com.matso.converter.di.AppInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class ConverterApplication : Application(), HasActivityInjector {

    @set:Inject
    var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>? = null

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity>? {
        return dispatchingAndroidInjector
    }
}
