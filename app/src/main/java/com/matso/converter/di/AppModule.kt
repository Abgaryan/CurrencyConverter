package com.matso.converter.di

import com.matso.converter.Constants
import com.matso.converter.data.RateDataSource
import com.matso.converter.schedulers.BaseSchedulerProvider
import com.matso.converter.schedulers.SchedulerProvider
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
internal class AppModule {
    @Singleton
    @Provides
    fun provideRateDataSource(): RateDataSource {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RateDataSource::class.java)
    }

    @Provides
    fun providesSchedulerProvider(): BaseSchedulerProvider {
        return SchedulerProvider()
    }
}
