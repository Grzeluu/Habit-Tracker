plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.android.hilt.get().pluginId)
    id(libs.plugins.google.ksp.get().pluginId)
}

android {
    namespace = "com.grzeluu.habittracker.source.preferences"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.serialization)
    implementation(libs.android.hilt)
    ksp(libs.android.hilt.compiler)
}