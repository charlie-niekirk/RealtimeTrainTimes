plugins {
    alias(libs.plugins.realtimetrains.android.application)
    alias(libs.plugins.realtimetrains.android.application.compose)
    alias(libs.plugins.realtimetrains.android.application.flavors)
    alias(libs.plugins.realtimetrains.android.hilt)
    alias(libs.plugins.com.google.gms.google.services)
}

android {
    namespace = "me.cniekirk.realtimetrains"
    compileSdk = 34

    defaultConfig {
        applicationId = "me.cniekirk.realtimetrains"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    ksp {
        arg("room.generateKotlin", "true")
    }
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.network)

    implementation(projects.feature.liveTrains)
    implementation(projects.feature.departureBoard)
    implementation(projects.feature.stationSearch)
    implementation(projects.feature.serviceDetails)

    implementation(libs.androidx.animation)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material3.window.size)
    implementation(libs.androidx.window.window)

    implementation(libs.mock.webserver)

    implementation(platform(libs.firebase.bom))
    implementation(libs.analytics)
    implementation(libs.remote.config)

    implementation(libs.navigation.compose)
    implementation(libs.hilt.navigation)

    implementation(libs.timber)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}