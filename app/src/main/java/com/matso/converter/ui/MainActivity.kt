package com.matso.converter.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.matso.converter.R
import com.matso.converter.comman.toFlagEmoji
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.rate_view.*
import java.util.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(), RateItemClickListener {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    internal lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[MainViewModel::class.java]
        recyclerView.adapter = RecyclerViewAdapter(this)
        observeViewModel()
        editText.addTextChangedListener { s -> s?.let { viewModel.onBaseCurrencyValueChanged(it) } }
    }


    private fun observeViewModel() {
        viewModel.getBaseCurrencyPair.observe(this, Observer { currencyPair ->
            val currency: Currency = Currency.getInstance(currencyPair.first)
            tvSymbol.text = currencyPair.first.dropLast(1).toFlagEmoji()
            tvName.text = currencyPair.first
            tvDisplayName.text = currency.displayName
            editText.setText(currencyPair.second.toString())
        })
        viewModel.geRatesList.observe(this, Observer { rateList ->
            (recyclerView.adapter as RecyclerViewAdapter).setRates(rateList)
        })

        viewModel.getLoading.observe(this, Observer { isLoading ->
            if (isLoading) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        })

        viewModel.getError.observe(this, Observer {
            showGeneralError()
        })
    }

    private fun showGeneralError() =
        Snackbar.make(main, R.string.error_message, Snackbar.LENGTH_SHORT).show()


    override fun onRateItemClick(rate: Pair<String, Double>) =
        viewModel.updateRates(rate)


}
