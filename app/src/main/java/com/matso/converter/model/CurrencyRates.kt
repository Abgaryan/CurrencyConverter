package com.matso.converter.model

data class CurrencyRates(
    val base: String,
    val rates: Map<String, Double>
)
