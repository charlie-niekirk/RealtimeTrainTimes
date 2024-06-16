plugins {
    alias(libs.plugins.realtimetrains.android.library)
    alias(libs.plugins.realtimetrains.android.hilt)
    alias(libs.plugins.com.google.devtools.ksp)
}

android {
    namespace = "me.cniekirk.realtimetrains.core.network"

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    api(projects.core.common)

    implementation(libs.androidx.core.ktx)
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)
    implementation(libs.logging.interceptor)
    implementation(libs.mock.webserver)

    implementation(libs.moshi)
    implementation(libs.moshi.adapters)
    ksp(libs.moshi.codegen)

    testImplementation(libs.coroutines.test)
}