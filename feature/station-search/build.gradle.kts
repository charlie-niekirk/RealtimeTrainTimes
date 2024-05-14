plugins {
    alias(libs.plugins.realtimetrains.android.feature)
    alias(libs.plugins.realtimetrains.android.library.compose)
}

android {
    namespace = "me.cniekirk.realtimetrains.feature.stationsearch"

    kotlinOptions {
        freeCompilerArgs = listOf("-Xcontext-receivers")
    }
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.data)
//    implementation(projects.governmentwatch.core.ui)

    implementation(libs.androidx.compose.foundation)

    implementation(libs.immutable)

    implementation(libs.coil.compose)
    implementation(libs.immutable)
}