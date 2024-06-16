plugins {
    alias(libs.plugins.realtimetrains.android.library)
    alias(libs.plugins.realtimetrains.android.hilt)
    alias(libs.plugins.org.jetbrains.kotlin.plugin.serialization)
}

android {
    namespace = "me.cniekirk.realtimetrains.core.common"
}

dependencies {
    implementation(libs.org.jetbrains.kotlinx.serialization)
}