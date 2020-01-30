package com.matso.converter.schedulers

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton


@Singleton
class SchedulerProvider : BaseSchedulerProvider {
    override fun ui(): Scheduler = AndroidSchedulers.mainThread()
    override fun io() = Schedulers.io()
}
