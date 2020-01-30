package com.matso.converter.data

import com.matso.converter.model.CurrencyRates
import retrofit2.http.GET
import retrofit2.http.Query


interface RateDataSource {

    @GET("latest")
    suspend fun getRates(@Query("base") baseRate: String): CurrencyRates

}
