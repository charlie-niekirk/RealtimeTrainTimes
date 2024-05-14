plugins {
    alias(libs.plugins.realtimetrains.android.library)
    alias(libs.plugins.realtimetrains.android.hilt)
    alias(libs.plugins.com.google.devtools.ksp)
    alias(libs.plugins.parcelize)
}

android {
    namespace = "me.cniekirk.realtimetrains.core.database"

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    api(projects.core.common)

    implementation(libs.androidx.core.ktx)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    testImplementation(libs.coroutines.test)
    testImplementation(libs.room.testing)
}