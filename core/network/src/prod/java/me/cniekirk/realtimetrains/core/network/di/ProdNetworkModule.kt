package me.cniekirk.realtimetrains.core.network.di

import com.squareup.moshi.Moshi
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ProdNetworkModule {

    @Provides
    @Singleton
    @Named("realtime")
    fun providesRetrofit(okHttpCallFactory: Lazy<Call.Factory>, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.rtt.io/api/v1/json/")
            .callFactory { okHttpCallFactory.get().newCall(it) }
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    @Named("huxley")
    fun providesHuxleyRetrofit(okHttpCallFactory: Lazy<Call.Factory>, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://huxley2.azurewebsites.net/")
            .callFactory { okHttpCallFactory.get().newCall(it) }
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
}