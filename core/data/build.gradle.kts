plugins {
    alias(libs.plugins.realtimetrains.android.library)
    alias(libs.plugins.realtimetrains.android.hilt)
    alias(libs.plugins.com.google.devtools.ksp)
}

android {
    namespace = "me.cniekirk.realtimetrains.core.data"

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
//    api(projects.core.model)
    api(projects.core.common)
    api(projects.core.network)
    api(projects.core.database)

    implementation(libs.immutable)

    implementation(libs.moshi)
    implementation(libs.moshi.adapters)
    ksp(libs.moshi.codegen)

    implementation(libs.retrofit)
    implementation(libs.protobuf)
    implementation(libs.paging)

    testImplementation(libs.coroutines.test)
}