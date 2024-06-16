package me.cniekirk.realtimetrains

import android.app.Application
import androidx.core.content.ContextCompat
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import me.cniekirk.realtimetrains.core.network.MockAppDispatcher
import okhttp3.mockwebserver.MockWebServer
import timber.log.Timber

@HiltAndroidApp
class RealTimeTrainsApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        val mockServer = MockWebServer().apply {
            dispatcher = MockAppDispatcher(applicationContext)
        }
        MainScope().launch(Dispatchers.IO) {
            mockServer.start(8080)
        }
    }
}