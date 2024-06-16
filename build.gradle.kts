// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.com.google.devtools.ksp) apply false
    alias(libs.plugins.org.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.org.jetbrains.kotlin.plugin.serialization) apply false
    alias(libs.plugins.parcelize) apply false
    alias(libs.plugins.com.google.gms.google.services) apply false
}