package com.matso.converter.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matso.converter.Constants
import com.matso.converter.Constants.DEBOUNCE_TIMEOUT
import com.matso.converter.data.RateDataSource
import com.matso.converter.schedulers.BaseSchedulerProvider
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainViewModel @Inject internal constructor(
    private val rateDataSource: RateDataSource,
    private val schedulerProvider: BaseSchedulerProvider
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val subject = PublishSubject.create<String>()

    private var baseRateList: List<Pair<String, Double>> = listOf()

    private var baseCurrencyPair = MutableLiveData(Pair(Constants.DEFAULT_VALUE, 1.0))

    val getBaseCurrencyPair: LiveData<Pair<String, Double>> get() = baseCurrencyPair

    private val rateList = MutableLiveData<List<Pair<String, Double>>>()

    val geRatesList: LiveData<List<Pair<String, Double>>>
        get() = rateList

    private val error = MutableLiveData<Exception>()

    val getError: LiveData<Exception> get() = error

    private val loading = MutableLiveData<Boolean>()

    val getLoading: LiveData<Boolean> get() = loading

    init {
        getCurrencyRates()
        disposables.add(
            Observable.interval(
                1,
                TimeUnit.MINUTES
            ).observeOn(schedulerProvider.ui()).subscribe { getCurrencyRates() }
        )
        disposables.add(
            subject
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.io())
                .map { text ->
                    if (text.trim().isEmpty()) {
                        0.0
                    } else {
                        text.trim().toDouble()
                    }
                }
                .debounce(DEBOUNCE_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe { baseValue ->
                    val oldBaseValue = baseCurrencyPair.value!!
                    val newBaseValue = oldBaseValue.copy(second = baseValue)
                    if (newBaseValue != oldBaseValue) {
                        baseCurrencyPair.postValue(newBaseValue)
                        getCurrencyRates()
                    }


                })
    }


    fun updateRates(ratePair: Pair<String, Double>) {
        baseCurrencyPair.value = ratePair
        getCurrencyRates()
    }

    fun onBaseCurrencyValueChanged(s: CharSequence) = subject.onNext(s.toString())


    private fun getCurrencyRates() {
        viewModelScope.launch {
            try {
                loading.value = true
                val currencyRates = rateDataSource.getRates(baseCurrencyPair.value!!.first)
                baseRateList = currencyRates.rates.toList()
                rateList.value =
                    baseRateList.map { pair -> pair.copy(second = pair.second * baseCurrencyPair.value!!.second) }
            } catch (e: Exception) {
                error.value = e
            } finally {
                loading.value = false
            }
        }

    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}
