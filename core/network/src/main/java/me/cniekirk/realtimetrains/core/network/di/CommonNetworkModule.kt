package me.cniekirk.realtimetrains.core.network.di

import android.content.Context
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.cniekirk.realtimetrains.core.network.apis.HuxleyApi
import me.cniekirk.realtimetrains.core.network.apis.RealtimeTrainsApi
import me.cniekirk.realtimetrains.core.network.utils.BasicAuthInterceptor
import me.cniekirk.realtimetrains.core.network.utils.SingleToArray
import okhttp3.Cache
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.time.Duration
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CommonNetworkModule {

    @Singleton
    @Provides
    fun providesCache(@ApplicationContext context: Context): Cache =
        Cache(context.cacheDir, 102400L)

    @Singleton
    @Provides
    fun providesOkHttpCallFactory(cache: Cache): Call.Factory {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(
                BasicAuthInterceptor(
                    username = "rttapi_cniekirk",
                    password = "e74893841ebdb96fe035ab783d044ffbb8b4d70e"
                )
            )
            .cache(cache)
            .callTimeout(Duration.ofMinutes(1))
            .connectTimeout(Duration.ofMinutes(1))
            .readTimeout(Duration.ofMinutes(1))
            .writeTimeout(Duration.ofMinutes(1))
            .build()
    }

    @Provides
    @Singleton
    fun providesMoshi(): Moshi {
        return Moshi.Builder().add(SingleToArray.Adapter.FACTORY).build()
    }

    @Provides
    @Singleton
    fun provideRealtimeTrainsApi(@Named("realtime") retrofit: Retrofit): RealtimeTrainsApi = retrofit.create(
        RealtimeTrainsApi::class.java)

    @Provides
    @Singleton
    fun provideHuxleyApi(@Named("huxley") retrofit: Retrofit): HuxleyApi = retrofit.create(HuxleyApi::class.java)
}