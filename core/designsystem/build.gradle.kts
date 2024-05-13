plugins {
    alias(libs.plugins.realtimetrains.android.library)
    alias(libs.plugins.realtimetrains.android.library.compose)
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    namespace = "me.cniekirk.realtimetrains.core.designsystem"
}

dependencies {

    api(libs.androidx.core.ktx)
    api(libs.androidx.ui)
    api(libs.androidx.ui.tooling)
    api(libs.androidx.ui.tooling.preview)
    api(libs.androidx.material3)
    api(libs.material.icons)
}