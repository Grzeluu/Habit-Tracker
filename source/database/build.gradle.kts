plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.google.ksp.get().pluginId)
    id(libs.plugins.android.hilt.get().pluginId)
}

android {
    namespace = "com.grzeluu.habittracker.source.database"
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
    implementation(libs.kotlinx.serialization)
    implementation(libs.android.hilt)
    implementation(libs.android.hilt.navigation)
    ksp(libs.android.hilt.compiler)
    implementation(libs.kotlinx.metadata)
}