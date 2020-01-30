package com.matso.converter.schedulers

import io.reactivex.schedulers.Schedulers

class TrampolineSchedulerProvider : BaseSchedulerProvider {
    override fun ui() = Schedulers.trampoline()
    override fun io() = Schedulers.trampoline()
}
