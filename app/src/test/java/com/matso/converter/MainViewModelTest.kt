package com.matso.converter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.matso.converter.Constants.DEBOUNCE_TIMEOUT
import com.matso.converter.comman.deepEquals
import com.matso.converter.data.RateDataSource
import com.matso.converter.model.CurrencyRates
import com.matso.converter.schedulers.TrampolineSchedulerProvider
import com.matso.converter.ui.MainViewModel
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.concurrent.TimeUnit

class MainViewModelTest {

    companion object {
        const val EUR = "EUR"
        const val USD = "USD"
        private const val CAD = "CAD"
        val EUR_PAIR = Pair(EUR, 1.0)
        val USD_PAIR = Pair(USD, 0.9)
        private val CAD_PAIR = Pair(CAD, 1.54)
        val currencyRatesWithEur = CurrencyRates(EUR, mapOf(USD_PAIR, CAD_PAIR))
        val currencyRatesWithUsd = CurrencyRates(USD, mapOf(EUR_PAIR, CAD_PAIR))
    }

    private val schedulerProvider = TrampolineSchedulerProvider()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var testCoroutineRule = TestCoroutineRule()

    @RelaxedMockK
    private lateinit var mockedRateDataSource: RateDataSource

    private lateinit var viewModel: MainViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        runBlockingTest {
            coEvery { mockedRateDataSource.getRates(EUR) } returns currencyRatesWithEur
        }
        createViewModel()
    }

    private fun createViewModel() {
        viewModel = MainViewModel(mockedRateDataSource, schedulerProvider)
    }

    @Test
    @Throws(Exception::class)
    fun testGetTheCurrencyList() {
        assertFalse(viewModel.getLoading.value!!)
        assertTrue(currencyRatesWithEur.rates.toList().deepEquals(viewModel.geRatesList.value!!))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testGetError() {
        clearMocks()
        runBlockingTest {
            coEvery { mockedRateDataSource.getRates(EUR) } throws Exception("d")
        }
        createViewModel()
        assertNotNull(viewModel.getError.value)
        assertFalse(viewModel.getLoading.value!!)
    }


    @ExperimentalCoroutinesApi
    @Test
    @Throws(Exception::class)
    fun testBaseCurrencyPair() {
        clearMocks()
        runBlockingTest {
            coEvery { mockedRateDataSource.getRates(EUR) } returns currencyRatesWithEur
        }
        createViewModel()
        assertTrue(viewModel.getBaseCurrencyPair.value!! == EUR_PAIR)
        assertFalse(viewModel.getLoading.value!!)
    }

    @ExperimentalCoroutinesApi
    @Test
    @Throws(Exception::class)
    fun testUpdateRates() {
        clearMocks()
        runBlockingTest {
            coEvery { mockedRateDataSource.getRates(EUR) } returns currencyRatesWithEur
        }
        runBlockingTest {
            coEvery { mockedRateDataSource.getRates(USD) } returns currencyRatesWithUsd
        }
        createViewModel()
        assertTrue(currencyRatesWithEur.rates.toList().deepEquals(viewModel.geRatesList.value!!))
        viewModel.updateRates(USD_PAIR)
        assertTrue(viewModel.getBaseCurrencyPair.value!! == USD_PAIR)
        assertFalse(viewModel.getLoading.value!!)
    }

    @ExperimentalCoroutinesApi
    @Test
    @Throws(Exception::class)
    fun testOnBaseCurrencyValueChanged() {
        clearMocks()
        runBlockingTest {
            coEvery { mockedRateDataSource.getRates(EUR) } returns currencyRatesWithEur
        }
        createViewModel()
        viewModel.onBaseCurrencyValueChanged("1.2")
        Thread.sleep(2*DEBOUNCE_TIMEOUT)
        assertEquals(1.2, viewModel.getBaseCurrencyPair.value!!.second, 0.0)

        //test if the empty input converts to 0
        viewModel.onBaseCurrencyValueChanged("")
        Thread.sleep(2*DEBOUNCE_TIMEOUT)
        assertEquals(0.0, viewModel.getBaseCurrencyPair.value!!.second, 0.0)

        //test the debounce
        viewModel.onBaseCurrencyValueChanged("1")
        viewModel.onBaseCurrencyValueChanged("0")
        Thread.sleep(2*DEBOUNCE_TIMEOUT)
        assertEquals(0.0, viewModel.getBaseCurrencyPair.value!!.second, 0.0)
        assertFalse(viewModel.getLoading.value!!)

    }
}
