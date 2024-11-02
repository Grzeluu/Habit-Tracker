// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.android.library) apply false
    id(libs.plugins.android.hilt.get().pluginId) version libs.plugins.android.hilt.get().version.requiredVersion apply false
    id(libs.plugins.google.ksp.get().pluginId)  version libs.plugins.google.ksp.get().version.requiredVersion apply false
}
buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath(libs.hilt.android.gradle.plugin)
    }
}