package com.matso.converter.schedulers

import io.reactivex.Scheduler

interface BaseSchedulerProvider {
    fun io(): Scheduler
    fun ui(): Scheduler
}
