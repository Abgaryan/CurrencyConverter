package com.matso.converter

import java.text.DecimalFormat

object Constants{
    const val DEFAULT_VALUE = "EUR"
    const val BASE_URL = "https://revolut.duckdns.org"
    const val DEBOUNCE_TIMEOUT:Long = 300
    val DECIMAL_FORMAT= DecimalFormat("0.00")
}
