package me.cniekirk.realtimetrains

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class RealTimeTrainsApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

//        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
//        val configSettings = remoteConfigSettings {
//            minimumFetchIntervalInSeconds = 3600
//        }
//        remoteConfig.setConfigSettingsAsync(configSettings)
//        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
//
//        remoteConfig.fetchAndActivate()
//            .addOnCompleteListener(ContextCompat.getMainExecutor(this)) { task ->
//                if (task.isSuccessful) {
//                    Timber.d("Updated config: ${task.result}")
//                } else {
//                    Timber.e("Error fetching remote config!")
//                }
//        }
    }
}